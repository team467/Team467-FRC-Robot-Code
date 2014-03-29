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
    private Solenoid solenoid2;
    private Solenoid solenoid3;

    //needed to ensure the feeder arms are pushed forward prior to firing
    private Feeder feeder = null;

    /**
     * Private constructor called by getInstance()
     */
    private Launcher()
    {
        feeder = Feeder.getInstance();
        solenoid1 = new Solenoid(RobotMap.LAUNCHER1);
        solenoid2 = new Solenoid(RobotMap.LAUNCHER2);
        solenoid3 = new Solenoid(RobotMap.LAUNCHER3);
    }

    /**
     * Fires the launcher. Handles the feeder arms to ensure they don't ensnare the
     * ball as it is fired.
     */
    public void fireLauncher()
    {
//        if (feeder.feederReadyForFire())
        {
            //need to wait to ensure the arms are actuall down before firing
            solenoid1.set(false);
            solenoid2.set(false);
            solenoid3.set(false);
        }
    }

    /**
     * Brings the launcher back into ready position
     */
    public void pullBackLauncher()
    {
        solenoid1.set(true);
        solenoid2.set(true);
        solenoid3.set(true);
    }
    
    /**
     * Allows for the setting of an individual solenoid, mostly for testing
     * purposes.
     */
    public void setIndividualPiston() 
    {
        
    }

    /**
     * Gets the single instance of this class
     *
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
