#include <stdint.h>
#include <stdbool.h>
#include "inc/tm4c123gh6pm.h"  //for code searching; redundant with IDE setting
#include "inc/hw_i2c.h"
#include "inc/hw_memmap.h"
#include "inc/hw_types.h"
#include "driverlib/debug.h"
#include "driverlib/fpu.h"
#include "driverlib/gpio.h"
#include "driverlib/i2c.h"
#include "driverlib/pin_map.h"
#include "driverlib/rom.h"
#include "driverlib/sysctl.h"
#include "driverlib/uart.h"
#include "utils/uartstdio.h"

#include "gyro.h"


#define VERBOSE false


//*****************************************************************************
//
// Configures the I2C1 port as a master for connection to the Gyro
//
//*****************************************************************************
void
Gyro_Configure(void)
{
    // Enable and initialize the I2C1 master module.  Use the system clock for
    // the I2C1 module.  The last parameter sets the I2C data transfer rate:
    //   false =  100kbps, true = 400kbps
    I2CMasterInitExpClk(I2C1_BASE, SysCtlClockGet(), false);
}

//*****************************************************************************
//
// Writes a single gyro register.  This routine is blocking.
//
// \param ui8RegisterAddress is the register address
// \param ui8Data is the data to write
//
//*****************************************************************************
void
Gyro_RegWrite(uint8_t ui8RegisterAddress, uint8_t ui8Data)
{
	if (VERBOSE) UARTprintf("GryroWriteReg(0x%x, 0x%x)\n", ui8RegisterAddress, ui8Data);

    // Set the slave device address for WRITE
    //   false = this I2C Master is initiating a writes to the slave.
    //   true  = this I2C Master is initiating reads from the slave.
    I2CMasterSlaveAddrSet(I2C1_BASE, SLAVE_ADDRESS, false);

    //
    // Transaction #1: Send the register address
    //

    // Set the Gyro Register address to write
    I2CMasterDataPut(I2C1_BASE, ui8RegisterAddress);

    // Start, send device address, write one byte (register address)...
    I2CMasterControl(I2C1_BASE, I2C_MASTER_CMD_BURST_SEND_START);

    // Wait for completion
    while(I2CMasterBusy(I2C1_BASE))
    {
    	//spin wait
    }
    //TODO: Check I2CMasterErr(I2C1_BASE)

    //
    // Transaction #1 continued: Send the register data
    //

    //Set the Gyro Register address to write
    I2CMasterDataPut(I2C1_BASE, ui8Data);

    // ... write another byte (register data) and end the transaction
	I2CMasterControl(I2C1_BASE, I2C_MASTER_CMD_BURST_SEND_FINISH);

	// Wait for completion
	while(I2CMasterBusy(I2C1_BASE))
	{
		//spin wait
	}
    //TODO: Check I2CMasterErr(I2C1_BASE)
}

//*****************************************************************************
//
// Reads a single gyro register.  This routine is blocking.
//
// \param ui8RegisterAddress is the register address
// \return read data
//
//*****************************************************************************
uint8_t
Gyro_RegRead(uint8_t ui8RegisterAddress)
{
	uint8_t ui8Data = 0;

	if (VERBOSE) UARTprintf("GryroRegRead(0x%x)\n", ui8RegisterAddress);

    // Set the slave device address for WRITE
    //   false = this I2C Master is initiating a writes to the slave.
    //   true  = this I2C Master is initiating reads from the slave.
    I2CMasterSlaveAddrSet(I2C1_BASE, SLAVE_ADDRESS, false);

    //
    // Transaction #1: Send the register address
    //

    // Set the Gyro Register address to write
    I2CMasterDataPut(I2C1_BASE, ui8RegisterAddress);

    // Start, send device address, write one byte (register address), and end the transaction
    I2CMasterControl(I2C1_BASE, I2C_MASTER_CMD_SINGLE_SEND);

    // Wait for completion
    while(I2CMasterBusy(I2C1_BASE))
    {
    	//spin wait
    }
    //TODO: Check I2CMasterErr(I2C1_BASE)

    //
    // Transaction #2: Read the register data
    //

    // Set the slave device address for READ
    //   false = this I2C Master is initiating a writes to the slave.
    //   true  = this I2C Master is initiating reads from the slave.
    I2CMasterSlaveAddrSet(I2C1_BASE, SLAVE_ADDRESS, true);

    // Start, send device address, read one byte (register data), and end the transaction
	I2CMasterControl(I2C1_BASE, I2C_MASTER_CMD_SINGLE_RECEIVE);

    // Wait for completion
	while(I2CMasterBusy(I2C1_BASE))
	{
		//spin wait
	}
    //TODO: Check I2CMasterErr(I2C1_BASE)

	ui8Data = I2CMasterDataGet(I2C1_BASE);
	return ui8Data;
}

//*****************************************************************************
//
// Reads a pair of gyro registers and returns the value as a 16-bit value.
// The first register is low byte, second is high byte
// This routine is blocking.
//
// \param ui8RegisterAddress is the first register address
// \return read data as 16-bit value.
//
//*****************************************************************************
uint16_t
Gyro_RegRead2(uint8_t ui8RegisterAddress)
{
	uint16_t ui16Data = 0;
	uint16_t ui16Register = 0;

	if (VERBOSE) UARTprintf("GryroRegRead2(0x%x)\n", ui8RegisterAddress);

    // Set the slave device address for WRITE
    //   false = this I2C Master is initiating a writes to the slave.
    //   true  = this I2C Master is initiating reads from the slave.
    I2CMasterSlaveAddrSet(I2C1_BASE, SLAVE_ADDRESS, false);

    //
    // Transaction #1: Send the register address
    //

    // Set the Gyro Register address to write
    // Set the MSB to enable auto increment of register address
    I2CMasterDataPut(I2C1_BASE, ui8RegisterAddress | 0x80);

    // Start, send one byte (register address), and end the transaction
    I2CMasterControl(I2C1_BASE, I2C_MASTER_CMD_SINGLE_SEND);

    // Wait for completion
    while(I2CMasterBusy(I2C1_BASE))
    {
    	//spin wait
    }
    //TODO: Check I2CMasterErr(I2C1_BASE)

    //
    // Transaction #2: read the register[0] data
    //

    // Set the slave device address for READ
    //   false = this I2C Master is initiating a writes to the slave.
    //   true  = this I2C Master is initiating reads from the slave.
    I2CMasterSlaveAddrSet(I2C1_BASE, SLAVE_ADDRESS, true);

    // Start, send device address, read one byte (register[0] data)...
	I2CMasterControl(I2C1_BASE, I2C_MASTER_CMD_BURST_RECEIVE_START);

	// Wait for completion
	while(I2CMasterBusy(I2C1_BASE))
	{
		//spin wait
	}
    //TODO: Check I2CMasterErr(I2C1_BASE)

	//read this register[0] into the low byte; mask to 8 bits
	ui16Data = I2CMasterDataGet(I2C1_BASE) & 0xFF;

    //
    // Transaction #2 continued: read the register[1] data
    //

    // ...read one byte (register[1] data), and end the transaction
	I2CMasterControl(I2C1_BASE, I2C_MASTER_CMD_BURST_RECEIVE_FINISH);

	// Wait for completion
	while(I2CMasterBusy(I2C1_BASE))
	{
		//spin wait
	}
    //TODO: Check I2CMasterErr(I2C1_BASE)

	//read the register[1]; mask to 8 bits
	ui16Register = I2CMasterDataGet(I2C1_BASE) & 0xFF;

	//put this register[1] into the high byte
	ui16Data |= ui16Register << 8;

	return ui16Data;
}
