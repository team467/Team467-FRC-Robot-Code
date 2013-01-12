/*
 * SHS Team 467
 * Class to handle driving 2009 Robot with the code and DriverStation
 * from the 2010 build season.
 */
package edu.shs.first.team467;

import edu.wpi.first.wpilibj.*;

/**
 *
 * @author aidan
 */
public class Robot2010Inputs
{

    //Change Number of Controllers Used
    private static final int NUMBER_OF_CONTROLLERS = 1;
    // Controllers
    private static final int XBOX_DRV = 1;
    private static final int XBOX_NAV = 2;
    // Digital Inputs
    private static final int DI_FIREPIN_IN_SENSOR = 8;
    private static final int DI_FIREPIN_OUT_SENSOR = 7;
    private DigitalInput firePinOutSensor;
    private DigitalInput firePinInSensor;
    public boolean diFirePinInSensor = false;
    public boolean diFirePinOutSensor = false;
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
    private Joystick xboxControllerDriver;
    private Joystick xboxControllerNavigator;
    public boolean dsRollerIn = false;
    public boolean dsRollerOut = false;
    public boolean dsTurretRight = false;
    public boolean dsTurretLeft = false;
    public boolean dsFireButton = false;
    public boolean dsBallUp = false;
    public boolean dsBallDown = false;
    public double dsLeftStick = 0.0;
    public double dsRightStick = 0.0;

    /**
     * Implement a dead zone for joystick centering - and a non-linear
     * acceleration as the user moves away from the zero position.
     * @param input
     * @return
     */
    private double filterJoystickInput(double input)
    {
        // Ensure that there is a dead zone around zero
        if ((input > 0.0 && input < JOYSTICK_DEADZONE) || (input < 0.0 && input > -JOYSTICK_DEADZONE))
        {
            return 0.0;
        }
        // Simply square the input to provide acceleration
        // ensuring that the sign of the input is preserved
        return (input > 0.0 ? input * input : -1.0 * input * input);
    }

    public Robot2010Inputs()
    {
        xboxControllerDriver = new Joystick(XBOX_DRV);
        if (NUMBER_OF_CONTROLLERS == 2)
        {
            xboxControllerNavigator = new Joystick(XBOX_NAV);
        }
        else
        {
            xboxControllerNavigator = xboxControllerDriver;
        }

        firePinOutSensor = new DigitalInput(DI_FIREPIN_OUT_SENSOR);
        firePinInSensor = new DigitalInput(DI_FIREPIN_IN_SENSOR);
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
        dsTurretRight = XboxButtonStatus(xboxControllerNavigator, XBOX_RB);
        dsTurretLeft = XboxButtonStatus(xboxControllerNavigator, XBOX_LB);
        dsBallUp = xboxControllerNavigator.getRawAxis(XBOX_TRIGGER) > 0.5 ||
                   XboxButtonStatus(xboxControllerNavigator, XBOX_Y);
        dsBallDown = XboxButtonStatus(xboxControllerNavigator, XBOX_A);
        dsRollerIn = XboxButtonStatus(xboxControllerNavigator, XBOX_X);
        dsRollerOut = XboxButtonStatus(xboxControllerNavigator, XBOX_B);

        // Read Joysticks.  Use utility class to provide dead zone and
        // non-linear acceleration
        dsLeftStick = filterJoystickInput(xboxControllerDriver.getRawAxis(XBOX_LEFTSTICK_Y));
        dsRightStick = filterJoystickInput(xboxControllerDriver.getRawAxis(XBOX_RIGHTSTICK_Y));

        // Use Right Trigger as the fire button
        dsFireButton = xboxControllerNavigator.getRawAxis(XBOX_TRIGGER) < -0.5;
        diFirePinInSensor = firePinInSensor.get();
        diFirePinOutSensor = firePinOutSensor.get();
    }
}
