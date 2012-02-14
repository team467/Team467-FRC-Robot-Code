/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.templates.Camera467.CamData;

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
    
    /**
     * Autonomous initialization code
     */
    public static void init()
    {
        //Make objects
        cam = Camera467.getInstance();
    }
    
    /**
     * Periodic autonomous update function
     */
    public static void updateAutonomous()
    {
        //TODO - work here
    }
}
