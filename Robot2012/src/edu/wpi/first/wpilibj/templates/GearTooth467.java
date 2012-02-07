/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.GearTooth;

/**
 *
 * @author aidan
 * Modified geartooth sensor class. In essence, all this class does is contains
 * functions for getting angular speed in rotations/second and linear speed in 
 * inches per second of the wheel the geartooth sensor is reading from.
 */
public class GearTooth467
{
    //Gear tooth sensor
    private GearTooth gearTooth;
    
    //Number of teeth on gear being read
    private int teeth = 0;
    
    //Diameter of the wheel attached to the gear (used for calculating linear 
    //speed if used) in inches
    private double diameter = 0.0;
    
    /**
     * Creates a new GearTooth467 object
     * @param channel The Digital Input channel of the geartooth sensor
     * @param teeth The number of teeth on the gear being read
     */
    public GearTooth467(int channel, int teeth)
    {
        gearTooth = new GearTooth(channel);
        this.teeth = teeth;
    }
    
    /**
     * Creates a new GearTooth467 object
     * @param channel The Digital Input channel of the geartooth sensor
     * @param teeth The number of teeth on the gear being read
     * @param diameter The diameter of the wheel attached to this unit (in inches)
     */
    public GearTooth467(int channel, int teeth, int diameter)
    {
        gearTooth = new GearTooth(channel);
        this.teeth = teeth;
        this.diameter = diameter;
    }
    
    /**
     * Starts the gear tooth sensor counting
     */
    public void start()
    {
        gearTooth.start();
    }
    
    /**
     * Stops the gear tooth sensor counting
     */
    public void stop()
    {
        gearTooth.stop();
    }
    
    /**
     * Resets the gear tooth counter to 0
     */
    public void reset()
    {
        gearTooth.reset();
    }
    
    /**
     * Gets the angular speed of the motor being measured in rotations/second
     * @return 
     */
    public double getAngularSpeed()
    {
        double speed = 1.0 / (gearTooth.getPeriod() * teeth);
        return speed;
    }
    
    /**
     * Gets the linear speed in inches/second
     * @return 
     */
    public double getLinearSpeed()
    {
        return getAngularSpeed() * diameter * Math.PI;
    }
}
