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
    
    private static boolean iSawSomething = false;
    
    /**
     * Autonomous initialization code
     */
    public static void init()
    {
        cam = Camera467.getInstance();
        cam.startThread();
        iSawSomething = false;
    }
    
    /**
     * Periodic autonomous update function
     */
    public static void updateAutonomous(int mode)
    {   
        long starttime = 0;
        if ( starttime == 0)
        {
            starttime = System.currentTimeMillis();
            
        }
        long elapsedtime = System.currentTimeMillis()-starttime;
        if (elapsedtime > 5000)
        {
            
        }
        else 
        {
            drive.carDrive(.01, -.4);
        }
        // make sure camera is reading
        if (!cam.isReading()) cam.toggleReading();
        
        particles = cam.getNumParticles();
        driverstation.println("[AUTO] cam = " + particles, 4);
        
        if (particles == 0) 
        {
            drive.drive(.1, null);
        }
        
        if (!iSawSomething) 
        {
            drive.crabDrive(0, .4, false);
        }
    }
    
    /**
     * Reset autonomous state
     */
    public static void resetState(int mode)
    {
        particles = 1;
    }
}
