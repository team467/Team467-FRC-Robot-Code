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
public class GyroAnalog467 
{
    //Single class instance
    private static GyroAnalog467 instance;

    //Gyro object
    private Gyro gyro;

    //Private constructor so instances can't be created outside this class
    private GyroAnalog467()
    {
        gyro = new Gyro(RobotMap.GYRO_CHANNEL);
        gyro.reset();
    }

    /**
     * Returns the single instance of this class
     * @return
     */
    public static GyroAnalog467 getInstance()
    {
        if (instance == null)
        {
            instance = new GyroAnalog467();
        }
        return instance;
    }

    /***
     * Return the gyro Angle - normalized to the range -180 to +180
     * Zero is the gyro initialization position.
     * @return The normalized gyro angle
     */
    public double getAngle()
    {
        double gyroAngle = gyro.getAngle() % 360;
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
