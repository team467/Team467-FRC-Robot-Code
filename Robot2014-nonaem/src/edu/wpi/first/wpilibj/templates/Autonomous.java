/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author Team467 This class consists of only static functions and 
 * variables, and simply acts as a container for all autonomous code.
 */
public class Autonomous 
{   
    private static Drive drive = Drive.getInstance();
    private static Camera467 cam;
    private static Launcher launcher;
    private static int particles = 0;
    private static Driverstation driverstation = Driverstation.getInstance();
    
    private static boolean iSawSomething = false;
    static long persistantTimerInMilis = 0;
    
    /**
     * Autonomous initialization code
     */
    public static void init()
    {
        cam = Camera467.getInstance();
        cam.startThread();
        launcher = Launcher.getInstance();
        iSawSomething = false;
        persistantTimerInMilis = 0;
    }
    
        
    /**
     * Periodic autonomous update function
     */
    public static void updateAutonomous(int mode)
    {   
        // make sure camera is reading
        if (!cam.isReading())
        {
            cam.toggleReading();
        }
        

        if (!cam.isTargetDetected())
        {
            //drive forward at 50% power
            drive.crabDrive(0, .5, false);
            //pull the launcher in the down position
            launcher.pullBackLauncher();
        }
        else
        {
            //stop and launch the ball
            launcher.fireLauncher();
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
