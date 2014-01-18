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
    private static long starttime = 0;
    private static Drive drive = Drive.getInstance();
    
    /**
     * Autonomous initialization code
     */
    public static void init()
    {
       
    }
    
    /**
     * Periodic autonomous update function
     */
    public static void updateAutonomous(int mode)
    {   
        if ( starttime == 0)
        {
            starttime = System.currentTimeMillis();
            
        }
        long elapsedtime = System.currentTimeMillis()-starttime;
        if (elapsedtime > 5000)
        {
            
        }
        else {
           drive.drive(1.0, null);
        }
    }
    
    /**
     * Reset autonomous state
     */
    public static void resetState(int mode)
    {
        starttime = 0;
    }
}
