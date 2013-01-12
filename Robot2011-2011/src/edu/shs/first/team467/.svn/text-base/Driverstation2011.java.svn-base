package edu.shs.first.team467;

import edu.wpi.first.wpilibj.*;
import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.DriverStationEnhancedIO.EnhancedIOException;
import edu.wpi.first.wpilibj.camera.AxisCamera;

/**
 * Singleton class to handle driverstation I/O on Team467 2010 Robot
 * @author aidan
 */
public class Driverstation2011
{
    //Singleton instance variable
    private static Driverstation2011 dsInstance;

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
    private static final int BUTTON_9= 9;
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

    //Driverstation button constants (stored in an array)
    private static final int[] BUTTON_CHANNELS = {11, 12, 15, 16, 5, 6, 7, 8, 9, 10};

    //Manual/automatic control switch constant
    private static final int MANUAL_CONTROL_SWITCH = 1;

    //Joystick deadzone constant
    private static final double JOYSTICK_DEADZONE = 0.1;

    //Joystick objects
    private Joystick joystick;

    //Public joystick button objects
    public boolean JoystickTrigger = false;
    public boolean JoystickButton2 = false;
    public boolean JoystickButton3 = false;
    public boolean JoystickButton4 = false;
    public boolean JoystickButton5 = false;
    public boolean JoystickButton6 = false;
    public boolean JoystickButton7 = false;
    public boolean JoystickButton8 = false;
    public boolean JoystickButton9 = false;
    public boolean JoystickButton10 = false;
    public boolean JoystickButton11 = false;
    public boolean JoystickButton12 = false;
    public boolean JoystickCalibrate = false;

    //Public joystick axis objects
    public double JoystickY = 0.0;
    public double JoystickX = 0.0;
    public double JoystickTwist = 0.0;
    public double SmallJoystickX = 0.0;
    public double SmallJoystickY = 0.0;

    //Boolean array to store button status of driverstation buttons
    private boolean[] buttonArray = new boolean[10];

    //Manual/automatic control switch object
    public boolean ManualControlSwitch = false;

    //Singleton so constructor is private
    private Driverstation2011()
    {
        joystick = new Joystick(1);
    }

    /**
     * Returns the single instance of this class
     * @return
     */
    public static Driverstation2011 GetInstance()
    {
	if (dsInstance == null)
        {
		dsInstance = new Driverstation2011();
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
    public void ReadInputs()
    {
        //Read joystick buttons
        JoystickTrigger = buttonStatus(joystick, TRIGGER);
        JoystickButton2 = buttonStatus(joystick, BUTTON_2);
        JoystickButton3 = buttonStatus(joystick, BUTTON_3);
        JoystickButton4 = buttonStatus(joystick, BUTTON_4);
        JoystickButton5 = buttonStatus(joystick, BUTTON_5);
        JoystickButton6 = buttonStatus(joystick, BUTTON_6);
        JoystickButton7 = buttonStatus(joystick, BUTTON_7);
        JoystickButton8 = buttonStatus(joystick, BUTTON_8);
        JoystickButton9 = buttonStatus(joystick, BUTTON_9);
        JoystickButton10 = buttonStatus(joystick, BUTTON_10);
        JoystickButton11 = buttonStatus(joystick, BUTTON_11);
        JoystickButton12 = buttonStatus(joystick, BUTTON_12);

        //Read joystick axis's
        JoystickCalibrate = !(joystick.getRawAxis(CALIBRATE) > 0.0);
        JoystickY = filterJoystickInput(joystick.getRawAxis(AXIS_Y));
        JoystickX = filterJoystickInput(joystick.getRawAxis(AXIS_X));
        JoystickTwist = filterJoystickInput(joystick.getRawAxis(TWIST));
        SmallJoystickX = joystick.getRawAxis(SMALL_AXIS_X);
        SmallJoystickY = joystick.getRawAxis(SMALL_AXIS_Y);

        //Read driverstation button status's
        for (int i = 0; i < buttonArray.length; i++)
        {
            try
            {
                buttonArray[i] = !driverstationEnhanced.getDigital(BUTTON_CHANNELS[i]);
            }
            catch (EnhancedIOException ex)
            {
                ex.printStackTrace();
            }
        }

        //Read manual/automatic control analog switch
        ManualControlSwitch = driverstation.getAnalogIn(MANUAL_CONTROL_SWITCH) > 1.0;

    }

    /**
     * Gets the status of the specified button number on the driverstation
     * @param buttonNumber The button number to check the status of
     * @return The status of the specified button
     */
    public boolean getButtonStatus(int buttonNumber)
    {
        //Use - 1 because button 1 is actually 0 in the array ect.
        return buttonArray[buttonNumber - 1];
    }

}
