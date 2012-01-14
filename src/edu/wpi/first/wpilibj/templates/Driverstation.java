package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.camera.AxisCamera;

/**
 * Singleton class to handle driverstation I/O on Team467 2010 Robot
 * @author aidan
 */
public class Driverstation
{
    //Singleton instance variable
    private static Driverstation instance;

    //Driverstation object
    private DriverStation driverstation = DriverStation.getInstance();
    private DriverStationEnhancedIO driverstationEnhanced = driverstation.getEnhancedIO();
    
    //Get single instance of camera to enable camera display on driverstation
    private AxisCamera cam = AxisCamera.getInstance();

    //Joystick button constants
    private static final int TRIGGER = 1;
    private static final int BUTTON_2 = 2;
    private static final int BUTTON_3 = 3;
    private static final int BUTTON_4 = 4;
    private static final int BUTTON_5 = 5;
    private static final int BUTTON_6 = 6;
    private static final int BUTTON_7 = 7;
    private static final int BUTTON_8 = 8;
    private static final int BUTTON_9 = 9;
    private static final int BUTTON_10 = 10;
    private static final int BUTTON_11 = 11;
    private static final int BUTTON_12 = 12;

    //Joystick axis constants
    private static final int AXIS_X = 1;
    private static final int AXIS_Y = 2;
    private static final int TWIST = 3;
    private static final int CALIBRATE = 4;
    private static final int SMALL_AXIS_X = 5;
    private static final int SMALL_AXIS_Y = 6;

    //Joystick deadzone constant
    private static final double JOYSTICK_DEADZONE = 0.1;

    //Joystick objects
    private Joystick joystick;

    //Public joystick button objects
    public boolean joystickTrigger = false;
    public boolean joystickButton2 = false;
    public boolean joystickButton3 = false;
    public boolean joystickButton4 = false;
    public boolean joystickButton5 = false;
    public boolean joystickButton6 = false;
    public boolean joystickButton7 = false;
    public boolean joystickButton8 = false;
    public boolean joystickButton9 = false;
    public boolean joystickButton10 = false;
    public boolean joystickButton11 = false;
    public boolean joystickButton12 = false;
    public boolean joystickCalibrate = false;

    //Public joystick axis objects
    public double joystickY = 0.0;
    public double joystickX = 0.0;
    public double joystickTwist = 0.0;
    public double smallJoystickX = 0.0;
    public double smallJoystickY = 0.0;

    //Singleton so constructor is private
    private Driverstation()
    {
        joystick = new Joystick(1);
    }

    /**
     * Returns the single instance of this class
     * @return
     */
    public static Driverstation getInstance()
    {
	if (instance == null)
        {
		instance = new Driverstation();
	}
	return instance;
    }

    /**
     * Calculate the distance of the stick from the center position, preserving the sign of the Y
     * component
     * @param stickX
     * @param stickY
     * @return
     */
    public double getStickDistance(double stickX, double stickY)
    {
        double val = Math.sqrt(stickX * stickX + stickY * stickY);
        return val;
    }

    /**
     * Calculate the angle of a joystick, given a specific x and y input value.
     * @param stickX - X parameter - in the range -1.0 to 1.0
     * @param stickY - Y parameter - in the range -1.0 to 1.0 
     * @return Joystick Angle in range -1.0 to 1.0
     */
    public double getStickAngle(double stickX, double stickY)
    {
        // This shouldn't be necessary, deadzone filtering should already
        // be performed - however it doesn't hurt to make sure.
        if ((Math.abs(stickX) < JOYSTICK_DEADZONE) && (Math.abs(stickY) < JOYSTICK_DEADZONE))
        {
            stickX = 0.0;
            stickY = 0.0;
        }
        // If joystick is centered, return zero as the angle.
        if (stickX == 0.0 && stickY == 0.0)
        {
            return (0.0);
        }

        if (stickY == 0.0)
        {
            // in Y deadzone avoid divide by zero error
            // Note - may not be needed. Java handles this automatically
            // need to test if the cRIO Java also handles it correctly.
            if (stickX > 0.0)
            {
                return 0.5;
            }
            else
            {
                return -0.5;
            }
        }

        // Return value in range -1.0 to 1.0

        double stickAngle = MathUtils.atan(stickX / -stickY);

        if (stickY > 0)
        {
            if (stickX > 0)
            {
                stickAngle += Math.PI;
            }
            else
            {
                stickAngle -= Math.PI;
            }
        }

        return (stickAngle / (Math.PI));
    }

    /**
     * Implement a dead zone for joystick centering - and a non-linear
     * acceleration as the user moves away from the zero position.
     * @param input
     * @return
     */
    private double filterJoystickInput(double input)
    {
        // Ensure that there is a dead zone around zero
        if (Math.abs(input) < JOYSTICK_DEADZONE)
        {
            return 0.0;
        }
        // Simply square the input to provide acceleration
        // ensuring that the sign of the input is preserved
        return (input * Math.abs(input));
    }

    /**
     * returns status of specified Joystick buttons
     * @param button
     * @return
     */
    private boolean buttonStatus(Joystick j, int button)
    {
        return j.getRawButton(button);
    }

    /**
     * Read all Robot Inputs. Typically, this is called once per iteration of the main
     * event loop. Any soft filtering of inputs to remove noise or implement
     * non-linear acceleration is also done here.
     */
    public void readInputs()
    {
        //Read joystick buttons
        joystickTrigger = buttonStatus(joystick, TRIGGER);
        joystickButton2 = buttonStatus(joystick, BUTTON_2);
        joystickButton3 = buttonStatus(joystick, BUTTON_3);
        joystickButton4 = buttonStatus(joystick, BUTTON_4);
        joystickButton5 = buttonStatus(joystick, BUTTON_5);
        joystickButton6 = buttonStatus(joystick, BUTTON_6);
        joystickButton7 = buttonStatus(joystick, BUTTON_7);
        joystickButton8 = buttonStatus(joystick, BUTTON_8);
        joystickButton9 = buttonStatus(joystick, BUTTON_9);
        joystickButton10 = buttonStatus(joystick, BUTTON_10);
        joystickButton11 = buttonStatus(joystick, BUTTON_11);
        joystickButton12 = buttonStatus(joystick, BUTTON_12);

        //Read joystick axis's
        joystickCalibrate = !(joystick.getRawAxis(CALIBRATE) > 0.0);
        joystickY = filterJoystickInput(joystick.getRawAxis(AXIS_Y));
        joystickX = filterJoystickInput(joystick.getRawAxis(AXIS_X));
        joystickTwist = filterJoystickInput(joystick.getRawAxis(TWIST));
        smallJoystickX = joystick.getRawAxis(SMALL_AXIS_X);
        smallJoystickY = joystick.getRawAxis(SMALL_AXIS_Y);

    }

}
