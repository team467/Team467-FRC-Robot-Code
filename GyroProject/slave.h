#ifndef __SLAVE_H__
#define __SLAVE_H__

extern void I2C3IntHandler(void);
extern void Slave_Configure(void);
extern void Slave_SetLatestValue(uint32_t value);

#endif //  __SLAVE_H__
