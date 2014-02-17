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

#include "uart.h"

//*****************************************************************************
//
// Configure the UART and its pins.  This must be called before UARTprintf().
//
// UART0, connected to the Virtual Serial Port and running at
// 115.2K, 8-N-1, is used to display messages from this application.
//
//*****************************************************************************
void
UART_Configure(void)
{
    // Use the internal 16MHz oscillator as the UART clock source.
    UARTClockSourceSet(UART0_BASE, UART_CLOCK_PIOSC);

    // Initialize the UART for console I/O.
    UARTStdioConfig(0, 115200, SysCtlClockGet());
}

