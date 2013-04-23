/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;

/**
 *
 * @author Team 467
 */
public class Shooter
{
    //Output objects

    private Jaguar launchMotor;
    //Single shooter instance
    private static Shooter instance;
    private static double motorSpeed = 0.0;
    private static boolean atCommandedSpeed = false;
    private static Driverstation driverstation;
    //Pneu Launcher position constants
    //sets if "on" or not
    public static final boolean PNEU_OUT = true; //TBD
    public static final boolean PNEU_IN = false; //TBD
    //Solenoid objects
    private static Solenoid arm;
    //ramp up value
    private final double RAMP_UP_ADDITIVE = 0.04; //TBD

    /**
     * Returns the single instance of the shooter
     *
     * @return Shooter instance
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
        motorSpeed = 0.0;
        atCommandedSpeed = false;
        driverstation.println("", 5);
    }

    /**
     * Constructor of the shooter object - Private constructor for singleton
     */
    private Shooter()
    {
        //Create motor objects
        launchMotor = new Jaguar(RobotMap.SHOOTER_LAUNCH_MOTOR_CHANNEL);
        arm = new Solenoid(RobotMap.SHOOTER_PNEU_DEPLOYER_CHANNEL);
    }

    /**
     * Returns if the launcher wheel has reached the desired commanded speed
     *
     * @return Returns boolean for at commanded speed
     */
    public boolean atCommandedSpeed()
    {
        return atCommandedSpeed;
    }

    /**
     * Drives the motor at set speed
     *
     * @param desiredSpeed double between 0.0 and 1.0 as these are the only
     * valid PWM values for the launcher
     */
    public void driveLaunchMotor(double desiredSpeed)
    {
        desiredSpeed = Math.abs(desiredSpeed);
        //if going less than desired speed, ramp up
        if (motorSpeed < desiredSpeed)
        {
            motorSpeed += RAMP_UP_ADDITIVE;
            atCommandedSpeed = false;
        }
        else
        {
            motorSpeed = desiredSpeed;
            atCommandedSpeed = true;
        }
        driverstation.println("Commanded Speed: " + motorSpeed, 4);
        launchMotor.set(-motorSpeed);
    }

    /**
     * Runs only the launch motor so that it can be tested to be going the right
     * direction
     *
     * @param speed double between -1.0 and 1.0
     */
    public void testLaunchMotor(double speed)
    {
        launchMotor.set(speed);
    }

    /**
     * Fires the pneumatic frisbee pusher True pushes the arm out, False pulls
     * the arm in Use constants in shooter for direction
     *
     * @param position boolean for in or out
     */
    public void deployFrisbeePneu(boolean position)
    {
        arm.set(position);
    }
}
