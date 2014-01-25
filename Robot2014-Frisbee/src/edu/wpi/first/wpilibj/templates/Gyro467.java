/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Gyro;

/**
 *
 * @author USFIRST
 */
public class Gyro467 {

    //Single class instance
    private static Gyro467 instance;

    //Gyro object
    private Gyro gyro;

    //Private constructor so instances can't be created outside this class
    private Gyro467()
    {
        gyro = new Gyro(RobotMap.GYRO_CHANNEL);        
        gyro.reset();
        gyro.setSensitivity(0.005);
    }

    /**
     * Returns the single instance of this class
     * @return
     */
    public static Gyro467 getInstance()
    {
        if (instance == null)
        {
            instance = new Gyro467();
        }
        return instance;
    }

    /**
     * Return the gyro Angle - normalized to the range -1 to +1
     * Zero is the gyro initialization position.
     * 
     * Decreases clockwise, increases counterclockwise.
     * 
     * @return The normalized gyro angle
     */
    public double getAngle()
    {
        double gyroAngle = gyro.getAngle() % 360;
        //normalizes the value between -180 and 180
        if (gyroAngle > 180.0)
        {
            gyroAngle -= 360.0;
        }
        if (gyroAngle < -180.0)
        {
            gyroAngle += 360.0;
        }
        return gyroAngle / 180.0;        
    }

    /**
     * Resets the gyro so the current position reads as 0
     */
    public void reset()
    {
        gyro.reset();       
    }
}
