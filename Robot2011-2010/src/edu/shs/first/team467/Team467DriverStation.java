package edu.shs.first.team467;

import edu.wpi.first.wpilibj.*;
import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.camera.AxisCamera;

/**
 * Singleton class to handle driverstation I/O on Team467 2010 Robot
 * @author aidan
 */
public class Team467DriverStation
{
    private DriverStation driverstation = DriverStation.getInstance();
    
    // get one instance of camera to enable camera display on driverstation
    private AxisCamera cam = AxisCamera.getInstance();

    //Xbox Buttons
    private static final int XBOX_A = 1;
    private static final int XBOX_B = 2;
    private static final int XBOX_X = 3;
    private static final int XBOX_Y = 4;
    private static final int XBOX_LB = 5;
    private static final int XBOX_RB = 6;
    private static final int XBOX_BACK = 7;
    private static final int XBOX_START = 8;
    private static final int XBOX_LEFTSTICK_CLICK = 9;
    private static final int XBOX_RIGHTSTICK_CLICK = 10;
    private static final int XBOX_LEFTSTICK_X = 1;
    private static final int XBOX_LEFTSTICK_Y = 2;
    private static final int XBOX_TRIGGER = 3;  // both triggers on same channel
    private static final int XBOX_RIGHTSTICK_X = 4;
    private static final int XBOX_RIGHTSTICK_Y = 5;
    /* 6 appears to be partially for the D-Pad - not mapped yet */

    //Robot Constants
    private static final double JOYSTICK_DEADZONE = 0.1;
    private Joystick xboxController;

    //Public driverstation variables
    public boolean XboxX = false;
    public boolean XboxB = false;
    public boolean XboxY = false;
    public boolean XboxA = false;
    public boolean XboxRB = false;
    public boolean XboxLB = false;
    public boolean XboxRightTrigger = false;
    public boolean XboxLeftTrigger = false;
    public double XboxTrigger = 0.0;
    public double LeftStickY = 0.0;
    public double RightStickY = 0.0;
    public double LeftStickX = 0.0;
    public double RightStickX = 0.0;

    // Singleton instance variable
    private static Team467DriverStation dsInstance;

    // Singleton so constructor is private
    private Team467DriverStation()
    {
        xboxController = new Joystick(1);
    }

    // return single instance of this class
	public static synchronized Team467DriverStation GetInstance()
    {
		if (dsInstance == null)
        {
			dsInstance = new Team467DriverStation();
		}
		return dsInstance;
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
        return (stickY < 0) ? -val : val;
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
        if (Math.abs(stickX) < JOYSTICK_DEADZONE)
        {
            stickX = 0.0;
        }
        if (Math.abs(stickY) < JOYSTICK_DEADZONE)
        {
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
                return 1.0;
            }
            else
            {
                return -1.0;
            }
        }

        // Return value in range -1.0 to 1.0
        return (2 * MathUtils.atan(stickX / Math.abs(stickY)) / (Math.PI));
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
     * returns status of specified Xbox Controller buttons
     * @param button
     * @return
     */
    private boolean XboxButtonStatus(Joystick j, int button)
    {
        return j.getRawButton(button);
    }

    /**
     * Read all Robot Inputs. Typically, this is called once per iteration of the main
     * event loop. Any soft filtering of inputs to remove noise or implement
     * non-linear acceleration is also done here.
     */
    public void ReadInputs()
    {
        //Read Buttons
        XboxRB = XboxButtonStatus(xboxController, XBOX_RB);
        XboxLB = XboxButtonStatus(xboxController, XBOX_LB);
        XboxY = XboxButtonStatus(xboxController, XBOX_Y);
        XboxA = XboxButtonStatus(xboxController, XBOX_A);
        XboxX = XboxButtonStatus(xboxController, XBOX_X);
        XboxB = XboxButtonStatus(xboxController, XBOX_B);
     
        // Read Joysticks.  Use utility class to provide dead zone and
        // non-linear acceleration
        LeftStickY = filterJoystickInput(xboxController.getRawAxis(XBOX_LEFTSTICK_Y));
        RightStickY = filterJoystickInput(xboxController.getRawAxis(XBOX_RIGHTSTICK_Y));
        LeftStickX = filterJoystickInput(xboxController.getRawAxis(XBOX_LEFTSTICK_X));
        RightStickX = filterJoystickInput(xboxController.getRawAxis(XBOX_RIGHTSTICK_X));

        // Read Xbox Triggers
        XboxTrigger = xboxController.getRawAxis(XBOX_TRIGGER);
        XboxRightTrigger = XboxTrigger < -0.5;
        XboxLeftTrigger = XboxTrigger > 0.5;

    }
}
