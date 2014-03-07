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
 * @author shrewsburyrobotics
 */
public class Shooter
{
    //----------Variables to change-----------//
    //
    //ramp up value
    private final double RAMP_UP_ADDITIVE = 0.04; //TBD
    //Pneu Launcher position constants
    public static final Relay.Value PNEU_OUT = Relay.Value.kForward; //TBD
    public static final Relay.Value PNEU_IN = Relay.Value.kOff; //TBD
    //num iterations the deployer will be out
    public static final int DEPLOYER_PNEU_NUM_ITERATIONS = 5;//100 millis
    //sets the dir of the shooter wheel spin
    public static final int SHOOTER_SPIN_DIRECTION = -1; //must be -1 or 1 to set direction
    //
    //-----------------------------------------//

    //Output objects
    private Jaguar launchMotor;
    //Single shooter instance
    private static Shooter instance;
    private static Driverstation driverstation;
    private static Relay arm;

    public static final int PNEU_DEPLOY_OUT = 1;
    public static final int PNEU_STAY_OUT = 2;
    public static final int PNEU_PULLED_IN = 3;
    public static final int PNEU_IDLE = 4;

    //used in code
    private static double motorSpeed = 0.0;
    private static boolean atCommandedSpeed = false;
    private static int pneuDeployIterator = 0;
    private static int currentState = PNEU_IDLE;

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
        pneuDeployIterator = 0;
        currentState = PNEU_IDLE;
        driverstation.println("", 5);
    }
    

    /**
     * Constructor of the shooter object - Private constructor for singleton
     */
    private Shooter()
    {
        //Create motor and solonoid objects
        launchMotor = new Jaguar(RobotMap.SHOOTER_CHANNEL);
        arm = new Relay(RobotMap.SHOOTER_PNEU_CHANNEL);
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
        atCommandedSpeed = true;
        driverstation.println("Commanded Speed: " + desiredSpeed, 4);
        //SHOOTER_SPIN_DIRECTION set the direction the motor spins by being -1 or 1
        launchMotor.set(SHOOTER_SPIN_DIRECTION * desiredSpeed);
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
     * Fires the pneumatic frisbee pusher, use constants in Shooter for states to use
     *
     * @param desiredState state for in or out
     */
    public void deployFrisbeePneu(int desiredState)
    {
        if (desiredState == PNEU_DEPLOY_OUT && currentState == PNEU_IDLE)
        {
            currentState = PNEU_DEPLOY_OUT;
        }
        else if(desiredState == PNEU_IDLE && currentState == PNEU_PULLED_IN)
        {
            currentState = PNEU_IDLE;
        }
        switch(currentState)
        {
            case PNEU_DEPLOY_OUT:
                pneuDeployIterator = 0;
                arm.set(PNEU_OUT);
                currentState = PNEU_STAY_OUT;
                break;

            case PNEU_STAY_OUT:
                arm.set(PNEU_OUT);
                if (pneuDeployIterator > DEPLOYER_PNEU_NUM_ITERATIONS)
                {
                    currentState = PNEU_PULLED_IN;
                }
                pneuDeployIterator++;
                break;

            case PNEU_PULLED_IN:
                arm.set(PNEU_IN);
                break;

            case PNEU_IDLE:
                arm.set(PNEU_IN);
                break;
        }
    }
}
