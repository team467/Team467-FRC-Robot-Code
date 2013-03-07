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
public class Autonomous {
    //Robot objects

    private static Drive drive;
    private static Driverstation driverstation;
    private static Shooter shooter;
    public static int elapsedTime = 0;//Time elapsed
    public static int RESPIN_ITERATION = 1;
    public static final int SPINUP_TIME = 5;//seconds
    public static final int RESPINUP_TIME = 3;//seconds
    public static final int ITERATIONS_PER_SEC = 50;
    public static final double SHOOTER_RUN_SPEED = 1.0;
    //STATES
    public static final int STATE_SPINNING_UP = 0;
    public static final int STATE_RESPINNING_UP = 1;
    public static int state = STATE_SPINNING_UP;

    /**
     * Autonomous initialization code
     */
    public static void init() {
        //Make objects
        drive = Drive.getInstance();
        driverstation = Driverstation.getInstance();
        shooter = Shooter.getInstance();
    }

    /**
     * Periodic autonomous update function
     */
    public static void updateAutonomous() {

        shooter.driveLaunchMotor(SHOOTER_RUN_SPEED);
        updateAutonomousLauncher(state);
        elapsedTime++;
    }
    
    private static void updateAutonomousLauncher(int delay){
        if (elapsedTime >= delay * ITERATIONS_PER_SEC)
                {
                    shooter.driveFeederMotor(RobotMap.FRISBEE_DEPLOY_FORWARD);
                    state = STATE_RESPINNING_UP;
                    elapsedTime = 0;
                }
                else
                {
                    shooter.driveFeederMotor(RobotMap.FRISBEE_DEPLOY_STOP);
                }
    }
    
    /**
     * Reset autonomous state
     */
    public static void resetState(int mode)
    {
   
    }
}
