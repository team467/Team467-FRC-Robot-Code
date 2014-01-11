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
    private static LifterObject lifterobject;
    private static Drive drive;
    private static Compressor467 compressor;
    private static final double SPINUP_TIME = 2.0;//seconds
    private static final double RESPINUP_TIME = 1.5;//seconds
    private static final int BACKUP_NUM_TICKS = 20;//ticks
    private static final double BACKUP_PWM_VAL = 0.0;//ticks
    private static final double BACKUP_ANGLE = -1.0;//computed by assuming the joystick is pulled fully backward
    private static final int ITERATIONS_PER_SEC = 50; //every iteration is 20 milis
    private static int elapsedTimeCounter = 0;//Time elapsed, added every 20 milis
    private static double delayTimeCounter = SPINUP_TIME * ITERATIONS_PER_SEC;//Time elapsed


    /**
     * Autonomous initialization code
     */
    public static void init()
    {
        //Make objects
        shooter = Shooter.getInstance();
        drive = Drive.getInstance();
        lifterobject = LifterObject.getInstance();
        compressor = Compressor467.getInstance();
        elapsedTimeCounter = 0;
        delayTimeCounter = SPINUP_TIME * ITERATIONS_PER_SEC;//Time elapsed
        lifterobject.moveArms(LifterObject.ARM_DOWN);
    }

    /**
     * Periodic autonomous update function
     */
    public static void updateAutonomous()
    {
        //runs launcher motor at desired speed, ramping is handled inside
        shooter.driveLaunchMotor(RobotMap.SHOOTER_RUN_SPEED);
        //needs to be tested for validity, should back the robot up quickly to jerk the feeder down
        if (elapsedTimeCounter <= BACKUP_NUM_TICKS)
        {
            drive.crabDrive(BACKUP_ANGLE, BACKUP_PWM_VAL);
            lifterobject.moveArms(LifterObject.ARM_DOWN);
        }
        else
        {
            drive.crabDrive(BACKUP_ANGLE, 0.0);
            lifterobject.moveArms(LifterObject.ARM_UP);
        }

        //checks to see if the counter has reached the spinup or respin up tick
        if (elapsedTimeCounter >= delayTimeCounter) //in ticks
        {
            shooter.deployFrisbeePneu(Shooter.PNEU_DEPLOY_OUT);
            //updates the delay counter to add time so that the shooter has time to respin up
            delayTimeCounter += RESPINUP_TIME * ITERATIONS_PER_SEC;//in ticks
        }
        else
        {
            shooter.deployFrisbeePneu(Shooter.PNEU_IDLE);
        }
        compressor.update();
        elapsedTimeCounter++;
    }
}