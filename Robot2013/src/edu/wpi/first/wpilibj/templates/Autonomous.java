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
    public static final int ITERATIONS_PER_SEC = 50;
    
    public static int elapsedTimeCounter = 0;//Time elapsed, added every 20 milis
    public static int delayTimeCounter = SPINUP_TIME * ITERATIONS_PER_SEC;//Time elapsed
    
    //Speed to run shooter at
    public static final double SHOOTER_RUN_SPEED = 1.0;        

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
        shooter.driveLaunchMotor(SHOOTER_RUN_SPEED);
        updateAutonomousLauncher();        
        elapsedTimeCounter++;
    }
    
    private static void updateAutonomousLauncher()
    {        
        if (elapsedTimeCounter >= delayTimeCounter) //in ticks
        {
            shooter.driveFeederMotor(RobotMap.FRISBEE_DEPLOY_FORWARD);            
            delayTimeCounter += RESPINUP_TIME * ITERATIONS_PER_SEC;//in ticks
        }
        else
        {
            shooter.driveFeederMotor(RobotMap.FRISBEE_DEPLOY_STOP);
        }
    }    
}
