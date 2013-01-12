/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.shs.first.team467;

import edu.wpi.first.wpilibj.Gyro;

/**
 *
 * @author USFIRST
 */
public class Gyro2011 {

    //Single class instance
    private static Gyro2011 instance;

    //Gyro object
    private Gyro gyro;

    //Gyro channel
    private final int GYRO_CHANNEL = 1;

    //Private constructor so instances can't be created outside this class
    private Gyro2011()
    {
        gyro = new Gyro(GYRO_CHANNEL);
        gyro.reset();
    }

    /**
     * Returns the single instance of this class
     * @return
     */
    public static Gyro2011 getInstance()
    {
        if (instance == null)
        {
            instance = new Gyro2011();
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
        return gyroAngle/180.0;
    }

    /**
     * Resets the gyro so the current position reads as 0
     */
    public void reset()
    {
        gyro.reset();
    }

}
