/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;

/**
 *
 * @author shrewsburyrobotics
 */
public class PneumaticArm {
    
    //Single class instance
    private static PneumaticArm instance;
    
    //Solenoid objects
    private Solenoid lower;
    private Solenoid raise;
    
    //Compressor object
    private Relay compressor;
    
    //Arm position constants
    public static final boolean ARM_UP = false;
    public static final boolean ARM_DOWN = true;
    
    //Reloading variables
    private int ticks = 0;
    private boolean loaded = true;
    private static final int RELOAD_TIME = 0; //TBD
    
    //Private constructor
    private PneumaticArm() 
    {
        raise = new Solenoid(RobotMap.ARM_RAISE_CHANNEL);
        lower = new Solenoid(RobotMap.ARM_LOWER_CHANNEL);
        compressor = new Relay(RobotMap.COMPRESSOR_CHANNEL);
    }
    
    //Returns instance of the class
    public static PneumaticArm getInstance()
    {
        if (instance == null)
        {
            instance = new PneumaticArm();
        }
        return instance;
    }
    
    /**
     * Set the position of the arm to either PneumaticArm.ARM_UP or PneumaticArm.ARM_DOWN
     * @param position 
     */
    public void moveArm(boolean position)
    {
        if(position == ARM_UP)
        {
            raise.set(true);
            lower.set(false);
        }
        else
        {
            raise.set(false);
            lower.set(true);
        }
    }
    
    /**
     * Periodic function to update reloading
     */
    public void updateReload()
    {
        if (!loaded)
        {
            compressor.set(Relay.Value.kOn);
            raise.set(false);
            lower.set(false);
        }
        else
        {
            compressor.set(Relay.Value.kOff);
        }
        if (ticks > RELOAD_TIME)
        {
            loaded = true;
        }
        else
        {
            ticks ++;
        }
    }
    
    /**
     * Starts compressor reloading
     */
    public void reload()
    {
        loaded = false;
    }
}