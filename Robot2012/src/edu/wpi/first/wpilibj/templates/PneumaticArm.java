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
    public static final int ARM_UP = 0;
    public static final int ARM_DOWN = 1;
    public static final int ARM_STOP = 2;
    
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
     * True puts the arm down, False picks the arm up
     * @param position 
     */
    public void moveArm(int position)
    {
        switch (position)
        {
            case ARM_UP:
                raise.set(true);
                lower.set(false);
                break;
            case ARM_DOWN:
                raise.set(false);
                lower.set(true);
                break;
            case ARM_STOP:
                raise.set(false);
                lower.set(false);
                break;
        }
    }
    
    /**
     * Periodic function to restore pneumatic pressure. 
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
        ticks = 0;
    }
}