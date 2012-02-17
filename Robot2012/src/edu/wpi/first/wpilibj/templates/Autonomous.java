/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.templates.Camera467.CamData;
import edu.wpi.first.wpilibj.templates.Drive;
import edu.wpi.first.wpilibj.templates.Llamahead;

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
    
    /**
     * Autonomous initialization code
     */
    public static void init()
    {
        //Make objects
        cam = Camera467.getInstance();
        llamahead = Llamahead.getInstance();
        
        
    }
    
    static int targetCenter = 0;
    static double test = 0.0;
    /**
     * Periodic autonomous update function
     */
    public static void updateAutonomous()
    {
        targetCenter = cam.returnTopMostXValue();
        //amount off from center on both sides
        int difference = 5; //pixels
        
        //min threshold for center
        int centerMin = cam.returnImageWidth() - difference;
        //max threshold for center
        int centerMax = cam.returnImageWidth() + difference;
        //if the center of the topMost is withinn the threshold, fire
        
        if (targetCenter > centerMin && targetCenter < centerMax)
        {
            llamahead.setLauncherWheel(test);
        }
        
    }
}
