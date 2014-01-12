/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 *
 * @author aidan
 */
public class PIDTuning
{
    //Robot objects
    private static Drive drive;
    
    //Wheel angle PID calibration variables
    private static double angle = 0.0;
    private static int ticks = 0;
    
    /**
     * Initialize the PID tuning functions
     */
    public static void init()
    {
        drive = Drive.getInstance();
    }
    
    /**
     * Tune the PID on the wheel angles. This sends a graph of actual wheel angle
     * vs set wheel angle to the custom dashboard.
     */
    public static void updateWheelAngleTune()
    {
        //Drive the wheels at the given angle
        drive.individualSteeringDrive(angle, 0, RobotMap.FRONT_LEFT);
        
        //Send actual angle and set angle to custom dashboard
        double actualAngle = drive.getNormalizedSteeringAngle(RobotMap.FRONT_LEFT);
        NetworkTable.getTable("wheel angles").putNumber("set angle", angle);
        NetworkTable.getTable("wheel angles").putNumber("actual angle", actualAngle);
        
        //Increment the angle after a time period
        if (ticks == 200)
        {
            angle = 0.0;
        }
        ticks ++;
    }
    
}
