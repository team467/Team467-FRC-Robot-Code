/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

/**
 *
 * @author shrewsburyrobotics This class consists of only static functions and
 * variables, and simply acts as a container for all autonomous code.
 */
public class Autonomous
{
    //Robot objects   
    private static Drive drive;   


    /**
     * Autonomous initialization code
     */
    public static void init()
    {
        //Make objects
        drive = Drive.getInstance();
    }

    /**
     * Periodic autonomous update function
     */
    public static void updateAutonomous()
    {        
    }
}