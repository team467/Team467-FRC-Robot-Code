/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.templates.Camera467.CamData;

/**
 *
 * @author shrewsburyrobotics
 */
public class Autonomous 
{
    //Camera objects
    private Camera467 cam;
    private CamData cameraData;
    
    //new instance of Autonomus
    private static Autonomous instance;
    
    //Private contructor for a singleton
    private Autonomous()
    {
        //Make objects
        cam = Camera467.getInstance();
    }
    
    /**
     * Get the single instance of this class
     * @return 
     */
    public static Autonomous getInstance()
    {
        if (instance == null)
        {
            instance = new Autonomous();
        }
        return instance;
    }
    
    /**
     * Periodic autonomous update function
     */
    public void updateAutonomous()
    {
        //TODO - work here
    }
}
