/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author shrewsburyrobotics This class consists of only static functions and 
 * variables, and simply acts as a container for all autonomous code.
 */
public class Autonomous 
{   
    private static Drive drive = Drive.getInstance();
    private static Camera467 cam;
    private static int particles = 0;
    private static Driverstation driverstation = Driverstation.getInstance();
    
    /**
     * Autonomous initialization code
     */
    public static void init()
    {
       cam = Camera467.getInstance();
       cam.startThread();
    }
    
    /**
     * Periodic autonomous update function
     */
    public static void updateAutonomous(int mode)
    {   
        // make sure camera is reading
        if (!cam.isReading()) cam.toggleReading();
        
        particles = cam.getNumParticles();
        driverstation.println("[AUTO] cam = " + particles, 4);
        
        drive.drive(.1, null);
        
        drive.stop();
    }
    
    /**
     * Reset autonomous state
     */
    public static void resetState(int mode)
    {
        particles = 1;
    }
}
