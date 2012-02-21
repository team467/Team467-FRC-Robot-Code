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
    //State Constants
    private static final int LAUNCH = 0;
    private static final int BACKUP = 1;
    private static final int DEPLOY_ARM = 2;
    private static final int DONE = 3;
    private static int state = LAUNCH;
    
    //Camera objects
    private static Llamahead llamahead;
    private static Drive drive;
    private static Driverstation driverstation;
    private static AnalogChannel ultrasonic;
    private static PneumaticArm arm;
    
    //Ticker to let launch wheel spin up
    private static int launchMotorTicker = 0;
    
    //Ticker to let llama neck spin
    private static int neckMotorTicker = 0;
    
    //Speed that the launcher runs at
    static final double SPEED = 43.0;//TBD
        
    //Robot will back up at this speed, this is the low speed
    private static final double BACKUP_SLOW_SPEED = 0.35; //TBD
    
    //Robot will back up at this speed, this is the high speed
    private static final double BACKUP_FAST_SPEED = BACKUP_SLOW_SPEED;//TBD
    
    //Robot will back up at this speed to get last couple of inches to bridge
    private static final double FINE_ADJUST_SPEED = 0.25;
    
    //Ticker for time the robot bakcs up at high speed
    private static int backupHighSpeedTicker = 0;
    
    //Time for backup at high speed, this * 20ms for actual time
    private static final int BACKUP_FAST_TIME = 0; //TBD
    
    //Ultrasonic reading that will trigger switch from fast backup to slow backup
    private static final int BACKUP_CHANGE_POINT = 40;//TBD

    //Ultrasonic reading that will trigger the robot to stop
    private static final int STOP_POINT = 10;//TBD
    
//    //The target center is intiated, used to take the center of the cameras
//    static int targetCenterX = 0;
//    
//    //Speed for turning
//    static final double TURN_SPEED = 0.38;
    
    /**
     * Autonomous initialization code
     */
    public static void init()
    {
        //Make objects
        llamahead = Llamahead.getInstance();
        drive = Drive.getInstance();
        driverstation = Driverstation.getInstance();
        ultrasonic = new AnalogChannel(RobotMap.ULTRASONIC_CHANNEL);
        //arm = PneumaticArm.getInstance();
    }
    
    static double testTicks = 0.0;
    
    /**
     * Periodic autonomous update function
     */
    public static void updateAutonomous()
    {
        //        targetCenterX = cam.returnCenterX();
//        //Amount off from center on both sides
//        int difference = 5; //Pixels
//        
//        //Min threshold for center
//        int centerMin = cam.returnImageWidth() - difference;
//        //Max threshold for center
//        int centerMax = cam.returnImageWidth() + difference;
//        
//        //If the center of the topMost is withinn the threshold, fire
//        if (targetCenterX >= centerMin && targetCenterX <= centerMax)
//        {
//            llamahead.setLauncherWheel(speed);
//        }
//        //Turn left
//        if (targetCenterX < centerMin)
//        {
//            //Negative turns left
//            drive.turnDrive(-TURN_SPEED);
//        }
//        //Turn right
//        if (targetCenterX > centerMax)
//        {
//            //Postive turns right
//            drive.turnDrive(TURN_SPEED);
//        }
        
        //Print geartooth speed to driverstation
        driverstation.println("Speed: " + llamahead.getLauncherSpeed(), 4);
        
        switch (state)
        {
            case LAUNCH:
                
                //Drive at 0 speed
                drive.crabDrive(0.0, 0.0, false);
                
                //Launch balls
                llamahead.launch(SPEED);
                
                //Moves to DONE if laucher has been active for enough time
                if (llamahead.getLaunchTime() > 75)
                {
                    state = DONE;
                }
                break;
                
                
            case BACKUP:
                //Backs up fast for specified time
//                if (backupHighSpeedTicker <= BACKUP_FAST_TIME)
//                {
//                    //Starts the drive backward at a high speed
//                    drive.crabDrive(0.0, BACKUP_FAST_SPEED, false);
//                    
//                    backupHighSpeedTicker++;
//                }

                //Drives at high speed then slows down on approach to bridge
                if (ultrasonic.getValue() > BACKUP_CHANGE_POINT)
                {
                    drive.crabDrive(0.0, -BACKUP_FAST_SPEED, false);
                }
                else
                {
                    //Starts the drive backward at lowerspeed, looking for ultrasonic
                    drive.crabDrive(0.0, -BACKUP_SLOW_SPEED, false);
                }
                if (ultrasonic.getValue() <= STOP_POINT)
                {
                    //Slowest speed to get arm in range
                    drive.crabDrive(0.0, -FINE_ADJUST_SPEED, false);
                        
                    //Moves the state to DEPLOY_ARM
                    state = DEPLOY_ARM;
                }
                break;
          
            case DEPLOY_ARM:
                
                //Drops the bridge arm if within range
                if (ultrasonic.getValue() <= 8)
                {
                    //Stops robot and drops arm
                    drive.crabDrive(0.0, 0.0, false);
                    arm.moveArm(PneumaticArm.ARM_DOWN);
                }
                System.out.println("Autonomous is done");
                
                //Leaves the case statment
                state = DONE;
                break;
                
            case DONE:
                
                //Drive at 0 speed
                drive.crabDrive(0.0, 0.0, false);
                llamahead.stopLauncherWheel();
                break;
                
        }
    }
    
    /**
     * Reset autonomous state
     */
    public static void resetState()
    {
        llamahead.stopLauncherWheel();
        neckMotorTicker = 0;
        launchMotorTicker = 0;
        state = BACKUP;
    }
}
