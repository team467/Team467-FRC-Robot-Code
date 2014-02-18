#include <stdbool.h>
#include <stdint.h>
#include "inc/hw_i2c.h"
#include "inc/hw_ints.h"
#include "inc/hw_memmap.h"
#include "inc/hw_types.h"
#include "driverlib/gpio.h"
#include "driverlib/i2c.h"
#include "driverlib/interrupt.h"
#include "driverlib/pin_map.h"
#include "driverlib/sysctl.h"
#include "driverlib/uart.h"
#include "utils/uartstdio.h"


#define MY_SLAVE_ADDRESS           0x3C


volatile uint32_t LatestValue = 0x1234;        //flag to indicate un-initialized value
uint32_t transmitBuffer = 0;
uint32_t transmitIndex = 0;


//*****************************************************************************
//
// The interrupt handler for the for I2C3 data slave interrupt.
//
//*****************************************************************************
void
I2C3IntHandler(void)
{
    uint8_t data;

    //capture the cause of this interrupt
    uint32_t intCause =  HWREG(I2C3_BASE + I2C_O_SMIS);

    // Clear the I2C3 interrupt flag.
    //I2CSlaveIntClear(I2C3_BASE); // Problem: this does not clear I2C_SICR_STARTIC
    HWREG(I2C3_BASE + I2C_O_SICR) = I2C_SICR_DATAIC | I2C_SICR_STARTIC;

    // If this is an I2C start, then load the transmit buffer with
    // the latest value and set-up to send the first byte
    if (intCause & I2C_SICR_STARTIC)
    {
        transmitBuffer = LatestValue;
        transmitIndex = 0;
    }

    // Alternate sending low / high byte of data
    // If Master requests more than 2 bytes, they will get the same 2 bytes over and over
    if (transmitIndex == 0)
    {
        //transmit the low byte first
        data = (uint8_t) ( transmitBuffer & 0x00ff );
        I2CSlaveDataPut(I2C3_BASE, data);
        transmitIndex = 1;
    }
    else
    {
        //transmit the high byte second
        data = (uint8_t) ( (transmitBuffer >> 8) & 0x00ff );
        I2CSlaveDataPut(I2C3_BASE, data);
        transmitIndex = 0;
    }

    return;
}



//*****************************************************************************
//
// Configure the I2C3 module as a slave
//
//*****************************************************************************
void
Slave_Configure(void)
{
    // Enable the I2C3 slave module.
    I2CSlaveEnable(I2C3_BASE);

    // Set my own slave address
    I2CSlaveInit(I2C3_BASE, MY_SLAVE_ADDRESS);

    //Register the function above to handle the I2C3 interrupt
    I2CIntRegister(I2C3_BASE, &I2C3IntHandler);

    // Enable the I2C3 interrupt on the AMR processor (NVIC).
    IntEnable(INT_I2C3);

    // Configure and turn on the I2C3 slave interrupt for Data and Start
    I2CSlaveIntEnableEx(I2C3_BASE, I2C_SLAVE_INT_DATA);
    I2CSlaveIntEnableEx(I2C3_BASE, I2C_SLAVE_INT_START);

    return;
}


//*****************************************************************************
//
// Set the latest value.  This is held for now and then copied to the
// transmit buffer when a Start bit is received
//
//*****************************************************************************
void
Slave_SetLatestValue(uint32_t value)
{
    LatestValue = value;
    return;
}
