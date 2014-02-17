//*****************************************************************************
//
// LED.c
//
// Copyright (c) 2012-2013 Texas Instruments Incorporated.  All rights reserved.
// Software License Agreement
// 
// Texas Instruments (TI) is supplying this software for use solely and
// exclusively on TI's microcontroller products. The software is owned by
// TI and/or its suppliers, and is protected under applicable copyright
// laws. You may not combine this software with "viral" open-source
// software in order to form a larger program.
// 
// THIS SOFTWARE IS PROVIDED "AS IS" AND WITH ALL FAULTS.
// NO WARRANTIES, WHETHER EXPRESS, IMPLIED OR STATUTORY, INCLUDING, BUT
// NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
// A PARTICULAR PURPOSE APPLY TO THIS SOFTWARE. TI SHALL NOT, UNDER ANY
// CIRCUMSTANCES, BE LIABLE FOR SPECIAL, INCIDENTAL, OR CONSEQUENTIAL
// DAMAGES, FOR ANY REASON WHATSOEVER.
// 
// This is part of revision 2.0.1.11577 of the EK-TM4C123GXL Firmware Package.
//
//*****************************************************************************

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

void
LED_on(void)
{
	// Turn on the BLUE LED.
	GPIOPinWrite(GPIO_PORTF_BASE, BLUE_LED, BLUE_LED);
}

void
LED_off(void)
{
	// Turn off the BLUE LED.
	GPIOPinWrite(GPIO_PORTF_BASE, BLUE_LED, 0);
}

void
LED_DoBlink(void)
{
	blinkCounter++;
	if (blinkCounter > 100)
	{
		GPIOPinWrite(GPIO_PORTF_BASE, BLUE_LED, GPIOPinRead(GPIO_PORTF_BASE,BLUE_LED) ^ BLUE_LED);
		blinkCounter = 0;
	}
}
