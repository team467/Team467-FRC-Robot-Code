/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.shs.first.team467;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 *
 * @author USFIRST
 */
public class LineFollow {
    
    //Instance
    private static LineFollow instance = null;

    //Drive object
    private Team467Drive drive;
    
    //Light sensor channels
    private final int LIGHT_SENSOR_RIGHT_CHANNEL = 2;
    private final int LIGHT_SENSOR_CENTER_CHANNEL = 4;
    private final int LIGHT_SENSOR_LEFT_CHANNEL = 6;

    //Dark sensor channels
    private final int DARK_SENSOR_RIGHT_CHANNEL = 1;
    private final int DARK_SENSOR_CENTER_CHANNEL = 3;
    private final int DARK_SENSOR_LEFT_CHANNEL = 5;

    //Light sensors
    private DigitalInput lightSensorRight;
    private DigitalInput lightSensorCenter;
    private DigitalInput lightSensorLeft;

    //Dark sensors
    private DigitalInput darkSensorRight;
    private DigitalInput darkSensorCenter;
    private DigitalInput darkSensorLeft;

    //Line follow constants
    private final double LINE_FOLLOW_ANGLE = 10.0 / 90.0;
    private final double INCREASED_LINE_FOLLOW_ANGLE = 10.0 / 90.0;

    //Line follow state constants
    private final int STATE_ON_LINE = 0;
    private final int STATE_RIGHT_OF_LINE = 1;
    private final int STATE_LEFT_OF_LINE = 2;
    private final int STATE_TURN_LAST = 3;

    private int lineState = STATE_ON_LINE;
    private boolean lastLeft = false;

    /**
     * Gets the single instance of this class
     * @return The instance
     */
    public static LineFollow getInstance() {
        if (instance == null) {
            instance = new LineFollow();
        }
        return instance;
    }

    private LineFollow()
    {
        //Get drive instance
        drive = Team467Drive.GetInstance();

        //Get light sensors
        lightSensorRight = new DigitalInput(LIGHT_SENSOR_RIGHT_CHANNEL);
        lightSensorCenter = new DigitalInput(LIGHT_SENSOR_CENTER_CHANNEL);
        lightSensorLeft = new DigitalInput(LIGHT_SENSOR_LEFT_CHANNEL);

        //Get dark sensors (not used)
        darkSensorRight = new DigitalInput(DARK_SENSOR_RIGHT_CHANNEL);
        darkSensorCenter = new DigitalInput(DARK_SENSOR_CENTER_CHANNEL);
        darkSensorLeft = new DigitalInput(DARK_SENSOR_LEFT_CHANNEL);
    }

    /**
     * Updates the line follow sequence. Call on a loop to have the robot 
     * follow a line.
     * @param speed The speed to follow at
     * @param fa Whether or not to use field aligned following
     */
    public void update(double speed, boolean fa)
    {
        // Set state based on light sensors
        if (lightSensorRight.get() && !lightSensorLeft.get())
        {
            lineState = STATE_LEFT_OF_LINE;
        }
        else if (lightSensorLeft.get() && !lightSensorRight.get())
        {
            lineState = STATE_RIGHT_OF_LINE;
        }
        else if (lightSensorCenter.get())
        {
            lineState = STATE_ON_LINE;
        }
        else
        {
            lineState = STATE_TURN_LAST;
        }
        // If to left drive right; if to right drive left; else drive straight
        switch (lineState)
        {
            case STATE_ON_LINE:
                drive(0, speed, fa);
                break;
            case STATE_LEFT_OF_LINE:
                drive(LINE_FOLLOW_ANGLE, speed, fa);
                lastLeft = false;
                break;
            case STATE_RIGHT_OF_LINE:
                drive(-LINE_FOLLOW_ANGLE, speed, fa);
                lastLeft = true;
                break;
            case STATE_TURN_LAST:
                if (lastLeft)
                {
                    drive(-INCREASED_LINE_FOLLOW_ANGLE, speed, fa);
                }
                else
                {
                    drive(INCREASED_LINE_FOLLOW_ANGLE, speed, fa);
                }
        }
    }

    /**
     * Drives the robot in either backStraightDrive or faDrive
     * @param angle Angle to drive at
     * @param speed Speed to drive at
     * @param fa Whether to use filed aligned or not
     */
    private void drive(double angle, double speed, boolean fa)
    {
        if (fa)
        {
            drive.faLineFollowDrive(angle, speed);
        }
        else
        {
            drive.backStraightDrive(angle, speed);
        }
    }

}
