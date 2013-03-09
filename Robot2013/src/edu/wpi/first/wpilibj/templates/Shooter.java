/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

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
        try
        {
            //Create motor objects
            launchMotor1 = new Jaguar(RobotMap.SHOOTER_LAUNCH_MOTOR_1_CHANNEL);
            launchMotor2 = new Jaguar(RobotMap.SHOOTER_LAUNCH_MOTOR_2_CHANNEL);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        feederMotor = new Relay(RobotMap.SHOOTER_INTAKE_MOTOR_CHANNEL);

        //Create sensor objects
        frisbeeLimitSwitch = new DigitalInput(RobotMap.SHOOTER_FRISBEE_DEPLOYER_BUTTON_SENSOR_CHANNEL);

    }

    /*
     * Gets status of the frisbee deployer button sensor
     * @return boolean of pressed or unpressed 
     */
    public boolean getFrisbeeLimitSwitch()
    {
        return frisbeeLimitSwitch.get();
    }

    /*
     * Gets status of the left turret limit switch sensor
     * @return boolean of pressed or unpressed 
     */
    public boolean getLeftTurretLimitSwitchStatus()
    {
        return false; //leftTurretLimitSwitch.get();
    }

    /*
     * Gets status of the right turret limit switch sensor
     * @return boolean of pressed or unpressed
     */
    public boolean getRightTurretLimitSwitchStatus()
    {
        return false;//rightTurretLimitSwitch.get();
    }

    public boolean returnAtCommandedSpeed()
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

        //Drive motor at speed "speed"
        try
        {
            speed = Math.abs(speed);
            if (motorSpeed < speed)
            {
                motorSpeed += 0.004;
                atCommandedSpeed = false;
            }
            else
            {
                motorSpeed = speed;
                atCommandedSpeed = true;
            }
            driverstation.println("Commanded Speed: " + motorSpeed, 4);
//            System.out.println("Speed: " + speed + " COmmanded Speed:" + motorSpeed);
            launchMotor1.set(-motorSpeed);
//            launchMotor1.setX(speed);
            launchMotor2.set(-motorSpeed);
//            launchMotor2.setX(speed);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
//        catch (CANTimeoutException ex)
//        {
//            ex.printStackTrace();
//        }        
    }

    /**
     * Runs only the first launch motor so that it can be tested to be going the
     * right direction
     *
     * @param speed
     */
    public void testLaunchMotor1(double speed)
    {
        try
        {
            launchMotor1.set(speed);
//            launchMotor1.setX(speed);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
//        catch (CANTimeoutException ex)
//        {
//            ex.printStackTrace();
//        }
    }

    /**
     * Runs only the second launch motor so that it can be tested to be going
     * the right direction
     *
     * @param speed
     */
    public void testLaunchMotor2(double speed)
    {
        try
        {
            launchMotor2.set(speed);
//            launchMotor2.setX(speed);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
//        catch (CANTimeoutException ex)
//        {
//            ex.printStackTrace();
//        }
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
                currentState = RobotMap.FRISBEE_CHECK_FOR_STOP_FORWARD;                
                disabledCounter = 0;
                debounceIterator = 0;
                break;

            case RobotMap.FRISBEE_CHECK_FOR_STOP_FORWARD:
                System.out.println("State: check for stop forward");
                if (disabledCounter < RobotMap.DISABLED_COUNTER_NUM_ITERATIONS)
                {
                    if (debounceIterator < RobotMap.SHOOTER_LIMIT_SWITCH_DEBOUNCE_ITERATIONS)
                    {
                        debounceIterator++;
                    }
                    else
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
                    disabledCounter++;
                }
                else
                {
                    currentState = RobotMap.FRISBEE_DISABLED;
                }

                break;

            case RobotMap.FRISBEE_DEPLOY_REVERSE:
                System.out.println("State: deploy reverse");
                feederMotor.set(Relay.Value.kReverse);
                currentState = RobotMap.FRISBEE_CHECK_FOR_STOP_REVERSE;
                disabledCounter = 0;
                debounceIterator = 0;
                break;

            case RobotMap.FRISBEE_CHECK_FOR_STOP_REVERSE:
                System.out.println("State: check for stop reverse");
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
                if (driverstation.JoystickNavigatorButton7)
                {
                    currentState = RobotMap.FRISBEE_DEPLOY_FORWARD;
                    disabledCounter = 0;
                }
                if (driverstation.JoystickNavigatorButton8)
                {
                    currentState = RobotMap.FRISBEE_DEPLOY_REVERSE;
                    disabledCounter = 0;
                }
                 break;
        }
    }
}
