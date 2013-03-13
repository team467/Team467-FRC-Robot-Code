/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Relay;

/**
 *
 * @author Cam da Man
 */
public class Shooter
{
    //Output objects
    private Jaguar launchMotor1;
    private Jaguar launchMotor2;
    private Relay feederMotor;
    //Sensor objects
    private DigitalInput frisbeeLimitSwitch;
    private static int debounceIterator = 0;
    //Single shooter instance
    private static Shooter instance;
    private static double motorSpeed = 0.0;
    private static boolean atCommandedSpeed = false;
    private static int currentState = RobotMap.FRISBEE_DEPLOY_IDLE;
    private static Driverstation driverstation;
    private static int disabledCounter = 0;    

    /**
     * Returns the single instance of the shooter
     *
     * @return
     */
    public static Shooter getInstance()
    {
        if (instance == null)
        {
            instance = new Shooter();
            instance.init();
        }
        return instance;
    }

    /**
     * Resets all variables used in shooter object
     */
    public void init()
    {
        driverstation = Driverstation.getInstance();
        debounceIterator = 0;
        motorSpeed = 0.0;
        disabledCounter = 0;  
        atCommandedSpeed = false;        
        driverstation.println("", 5);
        feederMotor.set(Relay.Value.kOff);
        currentState = RobotMap.FRISBEE_DEPLOY_IDLE;
    }

    /**
     * Constructor of the shooter object - Private constructor for singleton
     */
    private Shooter()
    {
        //Create motor objects
        launchMotor1 = new Jaguar(RobotMap.SHOOTER_LAUNCH_MOTOR_1_CHANNEL);
        launchMotor2 = new Jaguar(RobotMap.SHOOTER_LAUNCH_MOTOR_2_CHANNEL);

        feederMotor = new Relay(RobotMap.SHOOTER_INTAKE_MOTOR_CHANNEL);

        //Create sensor objects
        frisbeeLimitSwitch = new DigitalInput(RobotMap.SHOOTER_FRISBEE_DEPLOYER_BUTTON_SENSOR_CHANNEL);
    }

    /*
     * Gets status of the frisbee deployer button sensor
     * @return boolean of pressed or unpressed 
     */
    private boolean getFrisbeeLimitSwitch()
    {
        return frisbeeLimitSwitch.get();
    }

    public boolean atCommandedSpeed()
    {
        return atCommandedSpeed;
    }

    /**
     * Drives the motor at set speed
     *
     * @param speed double between 0.0 and 1.0 as these are the only valid PWM
     *              values for the launcher
     */
    public void driveLaunchMotor(double speed)
    {
        speed = Math.abs(speed);
        if (motorSpeed < speed)
        {
            motorSpeed += 0.005; // 4 secs to 0.80 power
            atCommandedSpeed = false;
        }
        else
        {
            motorSpeed = speed;
            atCommandedSpeed = true;
        }
        driverstation.println("Commanded Speed: " + motorSpeed, 4);
        launchMotor1.set(-motorSpeed);
        launchMotor2.set(-motorSpeed);   
    }

    /**
     * Runs only the first launch motor so that it can be tested to be going the
     * right direction
     *
     * @param speed
     */
    public void testLaunchMotor1(double speed)
    {
       launchMotor1.set(speed);
    }

    /**
     * Runs only the second launch motor so that it can be tested to be going
     * the right direction
     *
     * @param speed
     */
    public void testLaunchMotor2(double speed)
    {
        launchMotor2.set(speed);
    }

//    public void updateFeederMotor
    /**
     * Turns the motor the direction that is received from the main robot class
     */
    public void driveFeederMotor(int desiredState)
    {
        switch (currentState)
        {
            case RobotMap.FRISBEE_DEPLOY_FORWARD:
                System.out.println("State: deploy forward");
                feederMotor.set(Relay.Value.kForward);
                disabledCounter = 0;
                debounceIterator = 0;
                currentState = RobotMap.FRISBEE_CHECK_FOR_STOP_FORWARD;  
                break;

            case RobotMap.FRISBEE_CHECK_FOR_STOP_FORWARD:
                System.out.println("State: check for stop forward");
                disabledCounter++;
                debounceIterator++;
                if (disabledCounter < RobotMap.DISABLED_COUNTER_NUM_ITERATIONS)
                {
                    if (debounceIterator >= RobotMap.SHOOTER_LIMIT_SWITCH_DEBOUNCE_ITERATIONS)
                    {
                        //if limit switch pressed
                        if (!getFrisbeeLimitSwitch())
                        {
                            debounceIterator = 0;
                            disabledCounter = 0;
                            feederMotor.set(Relay.Value.kOff);
                            currentState = RobotMap.FRISBEE_DEPLOY_IDLE;
                        }
                    }
                }
                else
                {
                    currentState = RobotMap.FRISBEE_DISABLED;
                }
                break;

            case RobotMap.FRISBEE_DEPLOY_REVERSE:
                System.out.println("State: deploy reverse");
                feederMotor.set(Relay.Value.kReverse);
                disabledCounter = 0;
                debounceIterator = 0;
                currentState = RobotMap.FRISBEE_CHECK_FOR_STOP_REVERSE;
                break;

            case RobotMap.FRISBEE_CHECK_FOR_STOP_REVERSE:
                System.out.println("State: check for stop reverse");
                disabledCounter++;
                if (disabledCounter < RobotMap.DISABLED_COUNTER_NUM_ITERATIONS)
                {
                    //if limit switch pressed
                    if (!getFrisbeeLimitSwitch())
                    {
                        debounceIterator = 0;
                        feederMotor.set(Relay.Value.kOff);
                        currentState = RobotMap.FRISBEE_DEPLOY_IDLE;
                        disabledCounter = 0;
                    }
                }
                else
                {
                    currentState = RobotMap.FRISBEE_DISABLED;
                }

                break;

            case RobotMap.FRISBEE_DEPLOY_IDLE:
                System.out.println("State: idle");
                if (desiredState == RobotMap.FRISBEE_DEPLOY_FORWARD)
                {
                    currentState = RobotMap.FRISBEE_DEPLOY_FORWARD;
                }
                break;

            case RobotMap.FRISBEE_DISABLED:
                System.out.println("State: disabled");
                feederMotor.set(Relay.Value.kOff);
                driverstation.println("Feeder Jammed", 5);
                if (driverstation.JoystickNavigatorButton4)
                {
                    currentState = RobotMap.FRISBEE_DEPLOY_FORWARD;
                    disabledCounter = 0;
                }
                if (driverstation.JoystickNavigatorButton2)
                {
                    currentState = RobotMap.FRISBEE_DEPLOY_REVERSE;
                    disabledCounter = 0;
                }
                break;
        }
    }
}
