/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.templates.Camera467.CamData;
import edu.wpi.first.wpilibj.templates.Drive;
import edu.wpi.first.wpilibj.templates.Llamahead;
import edu.wpi.first.wpilibj.Kinect;

/**
 *
 * @author shrewsburyrobotics This class consists of only static functions and 
 * variables, and simply acts as a container for all autonomous code.
 */
public class Autonomous 
{
    //Camera objects
    private static Camera467 cam;
    private static CamData cameraData;
    private static Llamahead llamahead;
    private static Drive drive;
    private static Kinect467 kinect;
    
    /**
     * Autonomous initialization code
     */
    public static void init()
    {
        //Make objects
        cam = Camera467.getInstance();
        llamahead = Llamahead.getInstance();
        drive = Drive.getInstance();
        kinect = Kinect467.getInstance();
        
    }
    //the target centeris intiated, used to take the center of the cameras
    static int targetCenterX = 0;
    //this is a completly useless number, will be romoved later.
    static double test = 0.0;
    //speed for turning
    static final double TURN_SPEED = 0.38;
    
    
    /**
     * Periodic autonomous update function
     */
    public static void updateAutonomous()
    {
        targetCenterX = cam.returnCenterX();
        //amount off from center on both sides
        int difference = 5; //pixels
        
        //min threshold for center
        int centerMin = cam.returnImageWidth() - difference;
        //max threshold for center
        int centerMax = cam.returnImageWidth() + difference;
        
        //if the center of the topMost is withinn the threshold, fire
        if (targetCenterX >= centerMin && targetCenterX <= centerMax)
        {
            llamahead.setLauncherWheel(test);
        }
        //turn left
        if (targetCenterX < centerMin)
        {
            //negitive turns left
            drive.turnDrive(-TURN_SPEED);
        }
        //turn right
        if (targetCenterX > centerMax)
        {
            //postive turns right
            drive.turnDrive(TURN_SPEED);
        }
    }
}
