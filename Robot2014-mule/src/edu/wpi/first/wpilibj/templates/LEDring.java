/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Relay;

/**
 * @author Spencer
 */
public class LEDring {
    private static LEDring instance = null;
    private Relay light;
    
    private boolean on;
    
    private LEDring(int LEDchannel) {
        light = new Relay(LEDchannel);
        
        on = false;
    }
    
    public static LEDring getInstance() {
        if (instance == null) {
            instance = new LEDring(RobotMap.LED_CHANNEL);
        }
        
        return instance;
    }
    
    public void turnOff() {
        setLED(false);
    }
    
    public void turnOn() {
        setLED(true);
    }
    
    public void toggle() {
        setLED(!on);
    }
    
    public void setLED(boolean lightOn) {
        on = lightOn;
        
        if (on)
        {
            light.set(Relay.Value.kForward);
        }
        else
        {
            light.set(Relay.Value.kOff);
        }
    }
}
