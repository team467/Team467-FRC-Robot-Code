#ifndef __GYRO_H__
#define __GYRO_H__

//*****************************************************************************
//
// Defines
//
//*****************************************************************************
#define SLAVE_ADDRESS 0x69  //in 7 lsb of byte, address lsb=1

// register map for the L3G4200D Gyro
#define GYRO_CTRL_REG1      0x20
#define GYRO_CTRL_REG2      0x21
#define GYRO_CTRL_REG3      0x22
#define GYRO_CTRL_REG4      0x23
#define GYRO_CTRL_REG5      0x24
#define GYRO_REFERENCE      0x25
#define GYRO_OUT_TEMP       0x26
#define GYRO_STATUS_REG     0x27
#define GYRO_OUT_X_L        0x28
#define GYRO_OUT_X_H        0x29
#define GYRO_OUT_Y_L        0x2A
#define GYRO_OUT_Y_H        0x2B
#define GYRO_OUT_Z_L        0x2C
#define GYRO_OUT_Z_H        0x2D
#define GYRO_FIFO_CTRL_REG  0x2E
#define GYRO_FIFO_SRC_REG   0x2F
#define GYRO_INT1_CFG       0x30
#define GYRO_INT1_SRC       0x31
#define GYRO_INT1_TSH_XH    0x32
#define GYRO_INT1_TSH_XL    0x33
#define GYRO_INT1_TSH_YH    0x34
#define GYRO_INT1_TSH_YL    0x35
#define GYRO_INT1_TSH_ZH    0x36
#define GYRO_INT1_TSH_ZL    0x37
#define GYRO_INT1_DURATION  0x38

//I2C buffer size
#define I2C_BUFFER_SIZE 6


//*****************************************************************************
//
// Function prototypes
//
//*****************************************************************************

void		Gyro_Configure(void);
void		Gyro_RegWrite(uint8_t ui8RegisterAddress, uint8_t ui8Data);
uint8_t		Gyro_RegRead(uint8_t ui8RegisterAddress);
uint16_t	Gyro_RegRead2(uint8_t ui8RegisterAddress);
void		Gyro_Main();


#endif // __GYRO_H__
