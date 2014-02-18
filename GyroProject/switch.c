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

#include "switch.h"

#define SWITCH_1   GPIO_PIN_4
#define SWITCH_2   GPIO_PIN_0


//*****************************************************************************
//
// Push button switch Routines
//
//*****************************************************************************

//Get state of Switch1: true = pushed; false = not pushed
bool Switch_GetSwitch1()
{
    return (GPIOPinRead(GPIO_PORTF_BASE, SWITCH_1)  == 0);
}

//Get state of Switch2: true = pushed; false = not pushed
bool Switch_GetSwitch2()
{
    return (GPIOPinRead(GPIO_PORTF_BASE, SWITCH_2)  == 0);
}
