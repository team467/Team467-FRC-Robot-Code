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

#include "led.h"

#define RED_LED   GPIO_PIN_1
#define BLUE_LED  GPIO_PIN_2
#define GREEN_LED GPIO_PIN_3


//*****************************************************************************
//
// LED Routines
//
//*****************************************************************************


uint32_t  blinkCounter = 0;

// Turn on Blue LED
void LED_on(void)
{
	// Turn on the BLUE LED.
	GPIOPinWrite(GPIO_PORTF_BASE, BLUE_LED, BLUE_LED);
}

// Turn off Blue LED
void LED_off(void)
{
	// Turn off the BLUE LED.
	GPIOPinWrite(GPIO_PORTF_BASE, BLUE_LED, 0);
}

// Blink the Blue LED.  Call this periodically to blink
void LED_DoBlink(void)
{
	blinkCounter++;
	if (blinkCounter > 100)
	{
		GPIOPinWrite(GPIO_PORTF_BASE, BLUE_LED, GPIOPinRead(GPIO_PORTF_BASE,BLUE_LED) ^ BLUE_LED);
		blinkCounter = 0;
	}
}

