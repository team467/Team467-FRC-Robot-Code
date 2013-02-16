/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author aidan
 */
public class Shooter
{
    //Single shooter instance
    private static Shooter instance;
    
    /**
     * Returns the single instance of the shooter
     * @return 
     */
    public static Shooter getInstance()
    {
        if (instance == null)
        {
            instance = new Shooter();
        }
        return instance;
    }
    
    /**
     * Makes a new shooter object - Private constructor for singleton
     */
    private Shooter()
    {
        
    }
}
