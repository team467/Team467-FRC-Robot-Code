/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.templates.Camera467.CamData;
import edu.wpi.first.wpilibj.templates.Drive;
import edu.wpi.first.wpilibj.templates.Llamahead;
import edu.wpi.first.wpilibj.templates.PneumaticArm;
import edu.wpi.first.wpilibj.Kinect;
import edu.wpi.first.wpilibj.AnalogChannel;

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
    private static Camera467 cam;
    private static CamData cameraData;
    private static Llamahead llamahead;
    private static Drive drive;
    private static Driverstation driverstation;
    private static Kinect467 kinect;
    private static AnalogChannel ultrasonic;
    private static PneumaticArm arm;
    
    //Ticker to let launch wheel spin up
    private static int launchMotorTicker = 0;
    
    //Ticker to let llama neck spin
    private static int neckMotorTicker = 0;
    
    //Speed that the launcher runs at
    static final double SPEED = 47.0;//TBD
    
    //Robot will back up at this speed, this is the high speed
    private static final double BACKUP_FAST_SPEED = 0.0; //TBD
    
    //Ticker for time the robot bakcs up at high speed
    private static int backupHighSpeedTicker = 0;
    
    //Time for backup at high speed, this * 20ms for actual time
    private static final int BACKUP_FAST_TIME = 0; //TBD
    
    //Robot will back up at this speed, this is the low speed
    private static final double BACKUP_SLOW_SPEED = 0.0; //TBD

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
        cam = Camera467.getInstance();
        llamahead = Llamahead.getInstance();
        drive = Drive.getInstance();
        driverstation = Driverstation.getInstance();
        //kinect = Kinect467.getInstance();
        //arm = PneumaticArm.getInstance();
    }
    
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
        driverstation.println("Speed: " + llamahead.getLauncherSpeed(), 3);
        
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
                if (backupHighSpeedTicker <= BACKUP_FAST_TIME)
                {
                    //Starts the drive backward at a high speed
                    drive.crabDrive(BACKUP_FAST_SPEED, 0.0, false);
                    
                    backupHighSpeedTicker++;
                }
                else
                {
                    //If you are outside the value, 
                    if (ultrasonic.getValue() > 20)
                    {
                        //Starts the drive backward at lowerspeed, looking for ultrasonic
                        drive.crabDrive(BACKUP_SLOW_SPEED, 0.0, false);
                    }
                    else
                    {
                        //Stops the robot
                        drive.crabDrive(0.0, 0.0, false);
                        
                        //Moves the state to DEPLOY_ARM
                        state = DEPLOY_ARM;
                    }
                    break;
                }
          
                
            case DEPLOY_ARM:
                
                //drops the bridge arm
                arm.moveArm(PneumaticArm.ARM_DOWN);

                //Drops the bridge arm
                arm.moveArm(true);
                
                System.out.println("Autonomous is done");
                
                //Leaves the case statment
                state = DONE;
                break;
                
            case DONE:
                
                //Drive at 0 speed
                drive.crabDrive(0, 0, false);
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
        state = LAUNCH;
    }
}
