//----------------------------------------------------------------------------
// Copyright (c) 2014 by FIRST Team 467. All Rights Reserved.
// Open Source Software - may be modified and shared by FRC teams. The code
// must be accompanied by the FIRST BSD license file in the root directory of
// the project.
//----------------------------------------------------------------------------

#include <stdint.h>
#include <stdbool.h>
#include "inc/hw_memmap.h"
#include "inc/hw_types.h"
#include "driverlib/debug.h"
#include "driverlib/fpu.h"
#include "driverlib/gpio.h"
#include "driverlib/pin_map.h"
#include "driverlib/rom.h"
#include "driverlib/sysctl.h"
#include "driverlib/uart.h"
#include "utils/uartstdio.h"

#include "ports.h"
#include "uart.h"
#include "slave.h"
#include "gyro.h"
#include "led.h"
#include "switch.h"

#define DEBUG_MODE true

#define THRESHOLD_FILTER      5.0
#define SAMPLE_RATE_HZ		  100

#define GYRO_RATE 500        // can be 250, 500, or 2000

// Scale values taken from the Gyro's Datasheet
#if GYRO_RATE == 250
#define GYRO_GAIN 0.00875     // [deg/s/LSB]. This is for 250 deg/s mode.
#elif GYRO_RATE == 500
#define GYRO_GAIN 0.0175      // [deg/s/LSB]. This is for 500 deg/s mode.
#elif GYRO_RATE == 2000
#define GYRO_GAIN 0.070       // [deg/s/LSB]. This is for 2000 deg/s mode.
#else
#error Invalid GYRO_RATE
#endif

//*****************************************************************************
//
// The error routine that is called if the driver library encounters an error.
//
//*****************************************************************************
#ifdef DEBUG
void
__error__(char *pcFilename, uint32_t ui32Line)
{
}
#endif

//*****************************************************************************
//
// main() -- Execution Starts Here
//
//*****************************************************************************
int main(void)
{
	// Enable lazy stacking for interrupt handlers.  This allows floating-point
	// instructions to be used within interrupt handlers, but at the expense of
	// extra stack usage.
	ROM_FPULazyStackingEnable ();

	// Set the clocking to run directly from the crystal.
	SysCtlClockSet(
			SYSCTL_SYSDIV_1 | SYSCTL_USE_OSC | SYSCTL_OSC_MAIN
					| SYSCTL_XTAL_16MHZ);

	// Initialize the I/O Ports (pins)
	PortFunctionInit();

	// Initialize the UART.
	UART_Configure();

	// Initialize the I2C3 port as a slave to the cRIO
	Slave_Configure();

	// Initialize the I2C1 port as master to Gyro
	Gyro_Configure();

	// Say Hello on debug port
	UARTprintf("\nReady.\n");

	int16_t 	i16Data = 0;			// holds data read from a gyro register
	uint8_t 	ui8Status = 0;			// holds data read from gyro status register
	uint32_t 	ui32Iteration = 0;		// iterations counter used to average first N samples
	float		fAvgDrift = 0.0;		// average of 1st N samples, used as an offset to subtract
	int16_t		i16MaxDrift = 0.0;
	int16_t		i16MinDrift = 0.0;
	float 		fRate = 0.0;			// rate of change in Z in degs/sec
	float		fAngleDegrees = 0.0;	// running Z orientation in degrees
	uint16_t	ui16AngleScaled = 0.0;	// running Z orientation scaled to LSB = 0.01 degrees (i.e. value = degrees * 100)

	// Configure the L3G4200D Gyro chip
	Gyro_RegWrite(GYRO_CTRL_REG5, 0x80);    //BOOT:1 (reboot memory)
	SysCtlDelay(SysCtlClockGet() / 200); 	//delay ~1/200 sec
	Gyro_RegWrite(GYRO_CTRL_REG1, 0x0C); 	//DR:00 (100 Hz) PD:1 (enabled) XYZen:100 (Z only enabled)
	SysCtlDelay(SysCtlClockGet() / 200); 	//delay ~1/200 sec
	Gyro_RegWrite(GYRO_CTRL_REG4, 0x10);  	//FS:01 (500dps)
	SysCtlDelay(SysCtlClockGet() / 200); 	//delay ~1/200 sec
	Gyro_RegWrite(GYRO_CTRL_REG5, 0x42); 	//FIFO_EN:1 (enable FIFO) OUT_SEL:10 (LPF1+LPF2)
	SysCtlDelay(SysCtlClockGet() / 200); 	//delay ~1/200 sec
	Gyro_RegWrite(GYRO_FIFO_CTRL_REG, 0x4F);  //FM:010 (Stream Mode), WTM:01111
	SysCtlDelay(SysCtlClockGet() / 200); 	//delay ~1/200 sec


	//Main Gyro processing loop
	while (1)
	{
		ui8Status = Gyro_RegRead(GYRO_FIFO_SRC_REG);
		//UARTprintf("FIFO_SRC_REG: 0x%x\n", ui8Status);

		// if Gyro's FIFO is not empty
		if (!(ui8Status & 0x20))
		{
			//report the depth of FIFO:
			//if (DEBUG_MODE) UARTprintf("N:0x%02x : ", ui8Status & 0x1F);

			// Read X,Y, and Z values.
			// NOTE: Found that the FIFo does not empty unless all 3 are read
			// TODO: Create a Gyro_RegRead6() to read all 3 values at once
			// Cast from unsigned16 to signed16
			i16Data = (int16_t) Gyro_RegRead2(GYRO_OUT_X_L);  // read and throw away value
			i16Data = (int16_t) Gyro_RegRead2(GYRO_OUT_Y_L);  // read and throw away value
			i16Data = (int16_t) Gyro_RegRead2(GYRO_OUT_Z_L);  // read and keep value

			//Average the first 100 iterations
			if (ui32Iteration < 500)
			{

				//Note min/max
				if (i16Data < i16MinDrift) i16MinDrift = i16Data;
				if (i16Data > i16MaxDrift) i16MaxDrift = i16Data;

				// compute the running average by taking (average * iteration), which is the sum of all prior iterations,
				// adding this iteration's value, and dividing by (iteration+1).
				fAvgDrift = ( (fAvgDrift * (float)ui32Iteration) + (float)i16Data ) / (float)(ui32Iteration + 1);
				ui32Iteration++;

				if (DEBUG_MODE)
				{
					UARTprintf("Z: %d :", i16Data);
					UARTprintf("A: %d :", (int16_t)(fAvgDrift * 100.0));
					UARTprintf("m: %d :", i16MinDrift);
					UARTprintf("M: %d\n", i16MaxDrift);
				}
			}
			else
			{
				//remove static drift
				fRate = (float)i16Data - fAvgDrift;

				//threshold filter (ignore samples below a threshold)
				if ( (-THRESHOLD_FILTER < fRate) && (fRate < THRESHOLD_FILTER) )
				{
					fRate = 0.0;
				}

				// Convert rate to ( degrees / sec), scale by 1/SAMPLE_RATE_HZ, and sum into degrees
				fAngleDegrees += (fRate * GYRO_GAIN / SAMPLE_RATE_HZ) / 2.0;

				// wrap angle to be between 0 and 360
				//TODO: is there a better way to do this??
				while (fAngleDegrees < 0.0) fAngleDegrees += 360.0;
				while (fAngleDegrees >= 360.0) fAngleDegrees -= 360.0;

				// scale value to LSB = 0.01 degrees (i.e. value = degrees * 100)
				ui16AngleScaled = (uint16_t)(fAngleDegrees * 100.0);

				// send scaled value out slave i2c port
				Slave_SetLatestValue( ui16AngleScaled );

				if (DEBUG_MODE)
				{
					UARTprintf("Z: %d : ", ui16AngleScaled);
					UARTprintf("D: %d\n", (int16_t)(fAvgDrift * 100.0));
				}

			}

			LED_DoBlink();
		}
	}

}
