/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

/**
 *
 * @author shrewsburyrobotics This class consists of only static functions and
 * variables, and simply acts as a container for all autonomous code.
 */
public class Autonomous
{
    //Robot objects

    private static Shooter shooter;
    public static final int SPINUP_TIME = 5;//seconds
    public static final int RESPINUP_TIME = 3;//seconds
    public static final int ITERATIONS_PER_SEC = 50; //every iteration is 20 milis
    public static int elapsedTimeCounter = 0;//Time elapsed, added every 20 milis
    public static int delayTimeCounter = SPINUP_TIME * ITERATIONS_PER_SEC;//Time elapsed
    public static int deployerPneuIterator = 0;
    public static final int DEPLOYER_PNEU_NUM_ITERATIONS = 5;//100 millis

    /**
     * Autonomous initialization code
     */
    public static void init()
    {
        //Make objects
        shooter = Shooter.getInstance();
    }

    /**
     * Periodic autonomous update function
     */
    public static void updateAutonomous()
    {
        //runs launcher motor at desired speed, ramping is handled inside
        shooter.driveLaunchMotor(RobotMap.SHOOTER_RUN_SPEED);

        //checks to see if the counter has reached the spinup or respin up code
        if (elapsedTimeCounter >= delayTimeCounter) //in ticks
        {
            deployerPneuIterator = 0;
            //updates the delay counter to add time so that the shooter has time to respin up
            delayTimeCounter += RESPINUP_TIME * ITERATIONS_PER_SEC;//in ticks
        }
        else
        {
            deployerPneuIterator++;
        }

        //handles out vs in for deployer, holds the deployer out for 100 millis between firings
        if (deployerPneuIterator <= DEPLOYER_PNEU_NUM_ITERATIONS)
        {
            //deploys frisbee pusher
            shooter.deployFrisbeePneu(Shooter.PNEU_OUT);
        }
        else
        {
            shooter.deployFrisbeePneu(Shooter.PNEU_IN);
        }
        //updates the counter on total elapsed time
        elapsedTimeCounter++;
    }
}
