/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.shs.first.team467;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SmartDashboard;

/**
 *
 * @author USFIRST
 */
public class LineFollow2011 {
    
    //Instance
    private static LineFollow2011 instance = null;

    //Drive object
    private Drive2011 drive;
    
    //Light sensor channels
    private final int LIGHT_SENSOR_RIGHT_CHANNEL = 1;
    private final int LIGHT_SENSOR_CENTER_CHANNEL = 3;
    private final int LIGHT_SENSOR_LEFT_CHANNEL = 5;

    //Dark sensor channels
    private final int DARK_SENSOR_RIGHT_CHANNEL = 2;
    private final int DARK_SENSOR_CENTER_CHANNEL = 4;
    private final int DARK_SENSOR_LEFT_CHANNEL = 6;

    //Light sensors
    private DigitalInput lightSensorRight;
    private DigitalInput lightSensorCenter;
    private DigitalInput lightSensorLeft;

    //Dark sensors
    private DigitalInput darkSensorRight;
    private DigitalInput darkSensorCenter;
    private DigitalInput darkSensorLeft;

    //Line follow constants
    private final double LINE_FOLLOW_ANGLE = 5.0 / 180.0;
    private final double INCREASED_LINE_FOLLOW_ANGLE = 5.0 / 180.0;

    //Speed variable
    private double lineFollowSpeed = 0.5;

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
    public static LineFollow2011 getInstance() {
        if (instance == null) {
            instance = new LineFollow2011();
        }
        return instance;
    }

    private LineFollow2011()
    {
        //Get drive instance
        drive = Drive2011.getInstance();

        //Get light sensors
        lightSensorRight = new DigitalInput(LIGHT_SENSOR_RIGHT_CHANNEL);
        lightSensorCenter = new DigitalInput(LIGHT_SENSOR_CENTER_CHANNEL);
        lightSensorLeft = new DigitalInput(LIGHT_SENSOR_LEFT_CHANNEL);

        //Get dark sensors (not used)
        darkSensorRight = new DigitalInput(DARK_SENSOR_RIGHT_CHANNEL);
        darkSensorCenter = new DigitalInput(DARK_SENSOR_CENTER_CHANNEL);
        darkSensorLeft = new DigitalInput(DARK_SENSOR_LEFT_CHANNEL);
    }

    public void logSensors()
    {
        SmartDashboard.log(lightSensorRight.get(), "Light Sensor Right");
        SmartDashboard.log(lightSensorLeft.get(), "Light Sensor Left");
        SmartDashboard.log(lightSensorCenter.get(), "Light Sensor Center");
    }

    /**
     * Updates the line follow sequence. Call on a loop to have the robot 
     * follow a line.
     * @param speed The speed to follow at
     * @param fa Whether or not to use field aligned following
     */
    public void update()
    {
        //Log line following sensors for debugging
        logSensors();

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
                System.out.println("State On Line");
                drive.faDrive(0, lineFollowSpeed, 0.0);
                break;
            case STATE_LEFT_OF_LINE:
                System.out.println("State Left of Line");
                drive.faDrive(LINE_FOLLOW_ANGLE, lineFollowSpeed, 0.0);
                lastLeft = false;
                break;
            case STATE_RIGHT_OF_LINE:
                System.out.println("State Right of Line");
                drive.faDrive(-LINE_FOLLOW_ANGLE, lineFollowSpeed, 0.0);
                lastLeft = true;
                break;
            case STATE_TURN_LAST:
                System.out.println("State Turn Last");
                if (lastLeft)
                {
                    drive.faDrive(-INCREASED_LINE_FOLLOW_ANGLE, lineFollowSpeed, 0.0);
                }
                else
                {
                    drive.faDrive(INCREASED_LINE_FOLLOW_ANGLE, lineFollowSpeed, 0.0);
                }
        }
    }

    /**
     * Determines whether the robot is at the end of the line, signaled by all 3
     * light sensors being triggered at once.
     * @return
     */
    public boolean endOfLine()
    {
        return lightSensorLeft.get() && lightSensorRight.get() && lightSensorCenter.get();
    }

    public void setSpeed(double speed)
    {
        lineFollowSpeed = speed;
    }
}
