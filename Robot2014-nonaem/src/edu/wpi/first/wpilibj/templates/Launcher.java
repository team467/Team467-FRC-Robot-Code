/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Solenoid;

/**
 *
 * @author Team467
 */
public class Launcher
{
    //Single instance
    private static Launcher instance = null;
    
    private Solenoid solenoid1;

    //needed to ensure the feeder arms are pushed forward prior to firing
    private Feeder feeder = null;
    
    /**
     * Private constructor called by getInstance()
     */
    private Launcher()
    {
        feeder = Feeder.getInstance();
        solenoid1 = new Solenoid(RobotMap.LAUNCHER);
    }
    
    /**
     * Fires the arm. Handles the feeder arms to ensure they don't ensnare the ball as it is fired.
     */
    public void fireLauncher()
    {
        feeder.lowerArms();
        //need to wait to ensure the arms are actuall down before firing        
        //TODO: check it does fire the launcher
        solenoid1.set(true);
    }
    
    /**
     * Brings the arm back into ready firing position
     */
    public void pullBackLauncher()
    {
        //TODO: check it does pull the launcher back
        solenoid1.set(true);
    }        
    
    /**
     * Gets the single instance of this class
     * @return
     */
    public static Launcher getInstance()
    {
        if (instance == null)
        {
            instance = new Launcher();
        }
        return instance;
    }
}
