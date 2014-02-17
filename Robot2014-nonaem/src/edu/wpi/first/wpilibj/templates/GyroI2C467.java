/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalModule;
import edu.wpi.first.wpilibj.I2C;

/**
 *
 * @author Team467
 */
public class GyroI2C467
{

    private static final int TIVA_I2C_ADDRESS = 0x78;
    private static final boolean DEBUG_OUT = false;
    
    private static float angleDeltaToZero = 0;

    //instance used in the singleton
    private static GyroI2C467 instance = null;
    //instance of i2c used throughout for coms with the TIVA C
    private I2C i2c;

    //buffer that the values read from the TIVA C are placed into
    //16 bits, and defaults to 0000 so if transaction succeds but values are not properly read,
    //crab drive should still work
    private byte[] bufferRecieve = new byte[]
    {
        0x00, 0x00
    };

    /**
     * Private constructor to be called by singleton. Inits the I2C bus for
     * communication with the TIVA C.
     */
    private GyroI2C467()
    {
        //creates a new I2C master to read from the TIVA C        
        i2c = new I2C(DigitalModule.getInstance(1), TIVA_I2C_ADDRESS);
        //disables compatability mode to increase BOD rate on the I2C
        i2c.setCompatabilityMode(false);
        angleDeltaToZero = 0;
    }

    /**
     * Gets/sets up the singleton instance of this class.
     *
     * @return instance of GyroI2C467
     */
    public static GyroI2C467 getInstance()
    {
        if (instance == null)
        {
            instance = new GyroI2C467();
        }
        return instance;
    }
    
    /**
     * Sets the current angle to be zero. Equivlent to the reset() in the analog gyro.
     */
    public void setCurrentAngleAsZero()
    {
        angleDeltaToZero = clampToWithin360(angleDeltaToZero + getAngleDegrees());
    }
    
    private float angleDegrees; //returned by getAngleDegrees()
    /**
     * Gets the angle from the Gyro in degrees. 
     * Does account for a user defined zero value that can be set/reset on the fly.
     * 
     * Note: This may take a few milis to run as it does an I2C transaction.
     * 
     * @return Angle in degrees between 0 (inclusive) and 360 (exclusive). Value increases when spun clockwise.
     */
    public float getAngleDegrees()
    {
        //transaction read from the TIVA C the 16 bit value, placing it into the bufferRecieve buffer
        if (!i2c.transaction(null, 0, bufferRecieve, bufferRecieve.length))
        {
            //success in reading
            if (DEBUG_OUT)
            {
                System.out.print("s ");
                System.out.println(bufferRecieve[0] + ":" + bufferRecieve[1]);
            }
            
            //output is an integer, where value/100 = the angle in degrees           
            //passed into a 32bit int, meaning angleRawFromI2C will be positive. 
            //High byte is sent second, so the 1th val is shifted right 8 bits
            int angleRawFromI2C = (bufferRecieve[1] << 8) + bufferRecieve[0];
            //angle is now in degrees as it is divided by 100. This is due to the fact that the val
            //on the TIVA C was a float, *= 100, cast to an int, then sent over
            angleDegrees = ((float)angleRawFromI2C / 100.0f)  - angleDeltaToZero;            
            angleDegrees = clampToWithin360(angleDegrees);
            return angleDegrees;            
        }
        else
        {
            //fail in reading
            if (DEBUG_OUT)
            {
                System.out.println("f");
            }
            return 0;
        }        
    }
    
    /**
     * Clamps input from zero(inclusive) to 360 (exclusive)
     * @param f
     * @return clamped 
     */
    private float clampToWithin360(float f)
    {        
        //ensures that f is pos val
        if (f < 0)
        {
            f = 360 + f;
        }
        //I double checked, this is valid in Java, does as expected if f was an int in C
        return f % 360;
    }
}
