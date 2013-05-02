/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;

/**
 *
 * @author aidan
 */
public class Compressor467
{
    //Single instance
    private static Compressor467 instance = null;

    //Compressor objects
    private Relay compressor;
    private DigitalInput pressureSwitch;

    //Private constructor for singleton
    private Compressor467()
    {
        compressor = new Relay(RobotMap.COMPRESSOR_CHANNEL);
        pressureSwitch = new DigitalInput(RobotMap.PRESSURE_SWITCH_CHANNEL); //relay 2 comprressor
    }//1

    /**
     * Gets the single instance of this class
     * @return
     */
    public static Compressor467 getInstance()
    {
        if (instance == null)
        {
            instance = new Compressor467();
        }
        return instance;
    }

    /**
     * Periodic function to restore pneumatic pressure.
     */
    public void update()
    {

        //Run compressor if pressure switch indicates pressure is too low
        if (!pressureSwitch.get())
        {
            compressor.set(Relay.Value.kForward);
        }
        else
        {
            compressor.set(Relay.Value.kOff);
        }
    }

    public boolean compressionFinished()
    {
        return pressureSwitch.get();
    }
}
