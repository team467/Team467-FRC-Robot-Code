/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Kinect;
import edu.wpi.first.wpilibj.Skeleton;

/**
 *
 * @author aidan
 */
public class Kinect467
{
    //Single instance
    private static Kinect467 instance;
    
    //Objects
    private Kinect kinect;
    private Skeleton skeleton;
    
    /**
     * Get the single instance of this class
     */
    public static Kinect467 getInstance()
    {
        if (instance == null)
        {
            instance = new Kinect467();
        }
        return instance;
    }
    
    //Private constructor for singleton
    private Kinect467()
    {
        kinect = Kinect.getInstance();
        skeleton = kinect.getSkeleton();
    }
    
}
