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
    private static Kinect467 kinect;
    private static AnalogChannel ultrasonic;
    private static PneumaticArm arm;
    
    //ticker to let launch wheel spin up
    private static int launchMotorTicker = 0;
    
    //ticker to let llama neck spin
    private static int neckMotorTicker = 0;
    
    //speed that the launcher runs at
    static double speed = 48.0;//TBD
    
    //robot will back up at this speed, this is the high speed
    private static final double BACKUP_FAST_SPEED = 0.0; //TBD
    
    //ticker for time the robot bakcs up at high speed
    private static int backupHighSpeedTicker = 0;
    
    //time for backup at high speed, this * 20ms for actual time
    private static final int BACKUP_FAST_TIME = 0; //TBD
    
    //robot will back up at this speed, this is the low speed
    private static final double BACKUP_SLOW_SPEED = 0.0; //TBD

//    //the target centeris intiated, used to take the center of the cameras
//    static int targetCenterX = 0;
//    
//    //speed for turning
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
        //kinect = Kinect467.getInstance();
        //arm = PneumaticArm.getInstance();
    }
    
    /**
     * Periodic autonomous update function
     */
    public static void updateAutonomous()
    {
        //        targetCenterX = cam.returnCenterX();
//        //amount off from center on both sides
//        int difference = 5; //pixels
//        
//        //min threshold for center
//        int centerMin = cam.returnImageWidth() - difference;
//        //max threshold for center
//        int centerMax = cam.returnImageWidth() + difference;
//        
//        //if the center of the topMost is withinn the threshold, fire
//        if (targetCenterX >= centerMin && targetCenterX <= centerMax)
//        {
//            llamahead.setLauncherWheel(speed);
//        }
//        //turn left
//        if (targetCenterX < centerMin)
//        {
//            //negitive turns left
//            drive.turnDrive(-TURN_SPEED);
//        }
//        //turn right
//        if (targetCenterX > centerMax)
//        {
//            //postive turns right
//            drive.turnDrive(TURN_SPEED);
//        }
        
        switch (state)
        {
            case LAUNCH:
                
                //Drive at 0 speed
                drive.crabDrive(0, 0, false);
                
                //Run launcher at desired speed
                llamahead.setLauncherWheel(speed);
                
                //Waits 1/2 second before firing the ball
                if (!llamahead.atSpeed())
                {
                    //Turns neck motor off untill at speed for launch motor
                    llamahead.setBallAdvance(Llamahead.STOP);
                }
                else
                {
                    
                    //Spins neck motor for 1.5 seconds
                    if (neckMotorTicker <= 75)
                    {
                        //Turns neck motor on
                        llamahead.setBallAdvance(Llamahead.FORWARD);
                        
                        neckMotorTicker++;                        
                    }
                    else
                    {
                        //Turns motor on llamahead off after alloted time
                        llamahead.setBallAdvance(Llamahead.STOP);
                        
                        //Moves the state to BACKUP
                        state = DONE;          
                    }
                }
                break;
                
                
            case BACKUP:
                //backs up fast for specified time
                if (backupHighSpeedTicker <= BACKUP_FAST_TIME)
                {
                    //starts the drie backward at a high speed
                    drive.crabDrive(BACKUP_FAST_SPEED, 0.0, false);
                    
                    backupHighSpeedTicker++;
                }
                else
                {
                    //if you are outside the value, 
                    if (ultrasonic.getValue() > 20)
                    {
                        //starts the drive backward at lowerspeed, looking for ultrasonic
                        drive.crabDrive(BACKUP_SLOW_SPEED, 0.0, false);
                    }
                    else
                    {
                        //stops the robot
                        drive.crabDrive(0.0, 0.0, false);
                        
                        //moves the state to DEPLOY_ARM
                        state = DEPLOY_ARM;
                    }
                    break;
                }
          
                
            case DEPLOY_ARM:
                
                //drops the bridge arm
                arm.moveArm(true);
                
                System.out.println("Autonomous is done");
                
                //leaves the case statment
                state = DONE;
                break;
                
            case DONE:
                
                //Drive at 0 speed
                drive.crabDrive(0, 0, false);
                llamahead.setLauncherWheel(0.0);
                break;
                
        }
    }
    
    /**
     * Reset autonomous state
     */
    public static void resetState()
    {
        neckMotorTicker = 0;
        state = LAUNCH;
    }
}
