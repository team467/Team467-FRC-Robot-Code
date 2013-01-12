/*
 * SHS Team 467
 * Class to handle driving 2009 Robot with the the java libraries
 * from the 2010 build season.
 */
package edu.shs.first.team467;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.GenericHID.Hand;

/**
 * Class to read Team467 2009 Robot inputs.
 * This class maps provides an abstract interface to the digital inputs, ensuring each input
 * is read only once per time tick and providing software filtering as needed on the inputs.
 *
 * TBD - This class should be implemented as a singleton. There's probably no harm can
 * be caused by having multiple instances - but there's no value either
 * 
 */
public class Robot2009Inputs
{
    // Digital Inputs

    private static final int DI_FIREPIN_IN_SENSOR = 8;
    private static final int DI_FIREPIN_OUT_SENSOR = 7;
    private DigitalInput firePinOutSensor;
    private DigitalInput firePinInSensor;
    public boolean diFirePinInSensor = false;
    public boolean diFirePinOutSensor = false;
    // Drive Station Inputs
    private static final int DS_ROLLER_REVERSE = 1;
    private static final int DS_ROLLER_FORWARD = 2;
    private static final int DS_BALL_UP = 3;
    private static final int DS_BALL_DOWN = 4;
    private static final int DS_TURRET_FORWARD = 6;
    private static final int DS_TURRET_REVERSE = 7;
    private static final int DS_FIRE_BUTTON = 5;
    //Robot Constants
    private static final double JOYSTICK_DEADZONE = 0.1;
    private static final int ROLLER_REVERSE_TICKS_THRESHOLD = 100;
    private Joystick leftStick;
    private Joystick rightStick;
    public boolean dsRollerReverse = false;
    public boolean dsRollerForward = false;
    public boolean dsTurretForward = false;
    public boolean dsTurretReverse = false;
    public boolean dsFireButton = false;
    public boolean dsBallUp = false;
    public boolean dsBallDown = false;
    public double dsLeftStick = 0.0;
    public double dsRightStick = 0.0;
    private int rollerReverseTicks = 0;

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

    public Robot2009Inputs()
    {
        leftStick = new Joystick(1);
        leftStick.setAxisChannel(Joystick.AxisType.kX, 1);
        rightStick = new Joystick(1);
        rightStick.setAxisChannel(Joystick.AxisType.kX, 4);
        firePinOutSensor = new DigitalInput(DI_FIREPIN_OUT_SENSOR);
        firePinInSensor = new DigitalInput(DI_FIREPIN_IN_SENSOR);
    }

    /**
     * Read all Robot Inputs. Typically, this is called once per iteration of the main
     * event loop. Any soft filtering of inputs to remove noise or implement
     * non-linear acceleration is also done here. 
     */
    public void ReadInputs()
    {
        dsTurretForward = !DriverStation.getInstance().getDigitalIn(DS_TURRET_FORWARD);
        dsTurretReverse = !DriverStation.getInstance().getDigitalIn(DS_TURRET_REVERSE);
        dsRollerForward = !DriverStation.getInstance().getDigitalIn(DS_ROLLER_FORWARD);
        dsBallUp = !DriverStation.getInstance().getDigitalIn(DS_BALL_UP);
        dsBallDown = !DriverStation.getInstance().getDigitalIn(DS_BALL_DOWN);

        // Roller Reverse switch is noisy. Filter input
        if (!DriverStation.getInstance().getDigitalIn(DS_ROLLER_REVERSE))
        {
            rollerReverseTicks++; // increment as long as switch is enabled
        }
        else
        {
            rollerReverseTicks = 0; // reset if switch is disabled
        }
        dsRollerReverse = (rollerReverseTicks > ROLLER_REVERSE_TICKS_THRESHOLD) ? true : false;

        dsFireButton = !DriverStation.getInstance().getDigitalIn(DS_FIRE_BUTTON);

        // Read Joysticks.  Use utility class to provide dead zone and
        // non-linear acceleration
        dsLeftStick = filterJoystickInput(leftStick.getY(Hand.kRight));
        dsRightStick = filterJoystickInput(rightStick.getX(Hand.kRight));

        diFirePinInSensor = firePinInSensor.get();
        diFirePinOutSensor = firePinOutSensor.get();
    }
}
