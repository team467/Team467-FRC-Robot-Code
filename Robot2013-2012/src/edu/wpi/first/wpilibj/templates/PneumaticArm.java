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
    private Solenoid arm;
    
    //Arm position constants
    public static final boolean ARM_UP = false;
    public static final boolean ARM_DOWN = true;
    
    //Private constructor
    private PneumaticArm() 
    {
        arm = new Solenoid(RobotMap.ARM_CHANNEL);
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
    public void moveArm(boolean position)
    {
        arm.set(position);
    }

}