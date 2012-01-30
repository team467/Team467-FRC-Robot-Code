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
public class Autonomous467 
{
    //Camera objects
    private Camera467 cam;
    private CamData cameraData;
    
    //new instance of Autonomus
    private static Autonomous467 instance;
    
    //Private contructor for a singleton
    private Autonomous467()
    {
        //Make objects
        cam = Camera467.getInstance();
    }
    
    /**
     * Get the single instance of this class
     * @return 
     */
    public static Autonomous467 getInstance()
    {
        if (instance == null)
        {
            instance = new Autonomous467();
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
