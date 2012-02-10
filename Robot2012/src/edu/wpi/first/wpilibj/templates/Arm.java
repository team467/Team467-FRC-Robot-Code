/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Solenoid;

/**
 *
 * @author shrewsburyrobotics
 */
public class Arm {
    
    //Single class instance
    private static Arm instance;
    
    //Solenoid objects
    private Solenoid solenoid;
    
    //Arm position constants
    public static final boolean ARM_UP = false;
    public static final boolean ARM_DOWN = true;
    
    //Private constructor
    private Arm() 
    {
        solenoid = new Solenoid(RobotMap.ARM_MOVE_CHANNEL);
        solenoid.set(false);
    }
    
    //Returns instance of the class
    public static Arm getInstance()
    {
        if (instance == null)
        {
            instance = new Arm();
        }
        return instance;
    }
    
    /**
     * Set the position of the arm to either Arm.ARM_UP or Arm.ARM_DOWN
     * @param position 
     */
    public void setArm(boolean position)
    {
        solenoid.set(position);
    }   
}