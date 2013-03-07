/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 *
 * @author Cam da Man
 */
public class Shooter
{
    //Voltage ramp rate (in volts/second)
    private static final int RAMP_RATE = 3;
    
    //Output objects
    private CANJaguar launchMotor1;
    private CANJaguar launchMotor2;
    private CANJaguar turretRotator;
    private Relay feederMotor;
    
    //Sensor objects
    private DigitalInput frisbeeDeployerButton;
    private DigitalInput rightTurretLimitSwitch;
    private DigitalInput leftTurretLimitSwitch;
    //Single shooter instance
    
    private static Shooter instance;
    
    //Holds state of shooter deployment
    boolean ShooterOn = false;

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
     * Constructor of the shooter object - Private constructor for singleton
     */
    private Shooter()
    {
        try
        {
            //Create motor objects
            launchMotor1 = new CANJaguar(RobotMap.SHOOTER_LAUNCH_MOTOR_1_CHANNEL);
            launchMotor2 = new CANJaguar(RobotMap.SHOOTER_LAUNCH_MOTOR_2_CHANNEL);
            turretRotator = new CANJaguar(RobotMap.SHOOTER_TURRET_ROTATOR_MOTOR_CHANNEL);
            
            //Set voltage ramp rate (too much and the breaker trips)
            launchMotor1.setVoltageRampRate(RAMP_RATE);
            launchMotor2.setVoltageRampRate(RAMP_RATE);

        }
        catch (CANTimeoutException ex)
        {
            ex.printStackTrace();
        }

        feederMotor = new Relay(RobotMap.SHOOTER_INTAKE_MOTOR_CHANNEL);

        //Create sensor objects
        frisbeeDeployerButton = new DigitalInput(RobotMap.SHOOTER_FRISBEE_DEPLOYER_BUTTON_SENSOR_CHANNEL);
        rightTurretLimitSwitch = new DigitalInput(RobotMap.SHOOTER_TURRET_RIGHT_LIMIT_SWITCH_SENSOR_CHANNEL);
        leftTurretLimitSwitch = new DigitalInput(RobotMap.SHOOTER_TURRET_LEFT_LIMIT_SWITCH_SENSOR_CHANNEL);

    }

    /*
     * Gets status of the frisbee deployer button sensor
     * @return boolean of pressed or unpressed 
     */
    public boolean getFrisbeeDeployerButtonStatus()
    {
        return frisbeeDeployerButton.get();
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

    /**
     * Drives the motor at set speed
     *
     * @param speed double between 0.0 and 1.0 as these are the only valid PWM
     * values for the launcher
     */
    public void driveLaunchMotor(double speed)
    {
        speed = -Math.abs(speed);
        //Drive motor at speed "speed"
        try
        {
            launchMotor1.setX(speed);
            launchMotor2.setX(speed);

        }
        catch (CANTimeoutException ex)
        {
            ex.printStackTrace();
        }
        //Allows the frisbee deployer to know that the shooter is not spinning 
        //so it will not deploy a frisbee
        ShooterOn = speed != 0.0;
    }
    
    /**
     * Runs only the first launch motor so that it can be tested to be going the 
     * right direction
     * @param speed 
     */
    public void testLaunchMotor1(double speed)
    {
        try
        {
            launchMotor1.setX(speed);
        }
        catch (CANTimeoutException ex)
        {
            ex.printStackTrace();
        }
    }
    
    /**
     * Runs only the second launch motor so that it can be tested to be going the 
     * right direction
     * @param speed 
     */
    public void testLaunchMotor2(double speed)
    {
        try
        {
            launchMotor2.setX(speed);
        }
        catch (CANTimeoutException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Turns the motor the direction that is received from the main robot class
     */
    public void driveFeederMotor(int DirectionValue)
    {
        switch (DirectionValue)
        {
            //If the direction is set to forward then the intake motor will rotate forwards
            case RobotMap.FRISBEE_DEPLOY_FORWARD:
                if (ShooterOn)
                {
                    feederMotor.set(Relay.Value.kForward);
                }
                break;

            //If the intake motor direction is set to reverse then the intake motor will drive backwards  
            case RobotMap.FRISBEE_DEPLOY_REVERSE:
                if (ShooterOn)
                {
                    feederMotor.set(Relay.Value.kReverse);
                }
                break;

            //If the intake motor is set to stop then the intake motor will attempt to stop
            case RobotMap.FRISBEE_DEPLOY_STOP:
                //Stops the intake motor only if the frisbee deployer button is pressed
                if (!getFrisbeeDeployerButtonStatus())
                {
                    feederMotor.set(Relay.Value.kOff);
                }
                break;
        }

    }

    /**
     * Drives the turret rotator motor at the speed of the double rotate only if
     * neither of the limit switches are pressed
     *
     * @param rotateSpeed
     */
    public void driveTurretRotatorMotor(double rotateSpeed)
    {
        if (!getLeftTurretLimitSwitchStatus() && !getRightTurretLimitSwitchStatus())
        {
            //Drive motor at speed rotateSpeed
            try
            {
                turretRotator.setX(rotateSpeed);
            }
            catch (CANTimeoutException ex)
            {
                System.out.println("Rotate Motor Timed Out!");
                ex.printStackTrace();
            }
        }
        // if the left limit switch is pressed only allows the turret to rotate to the right
        //assumes turning left induces a neg output from joystick
        else if (getLeftTurretLimitSwitchStatus() && rotateSpeed > 0)
        {
            try
            {
                turretRotator.setX(rotateSpeed);
            }
            catch (CANTimeoutException ex)
            {
                ex.printStackTrace();
            }
        }
        //if the right limit switch is pressed only allows the turret to rotate to the left
        //assumes turning right induces a pos output from joystick
        else if (getRightTurretLimitSwitchStatus() && rotateSpeed < 0)
        {
            try
            {
                turretRotator.setX(rotateSpeed);
            }
            catch (CANTimeoutException ex)
            {
                ex.printStackTrace();
            }
        }
        else
        {
            try
            {
                turretRotator.setX(0.0);
            }
            catch (CANTimeoutException ex)
            {
                ex.printStackTrace();
            }
        }
    }
}
