package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author Spencer
 */
public class Feeder {
    private static Feeder instance;
    
    private Talon motor;
    private Relay arms;
    
    public static final Relay.Value ARMS_UP = Relay.Value.kForward;
    public static final Relay.Value ARMS_DOWN = Relay.Value.kOff;
    
    public static final double FEED_SPEED = .6;
     
    private Feeder() {
        motor = new Talon(RobotMap.FEEDER_MOTOR_CHANNEL);
        arms = new Relay(RobotMap.FEEDER_SOLENOID_CHANNEL);
    }
    
    public static Feeder getInstance() {
        if (instance == null) {
            instance = new Feeder();
        }
        
        return instance;
    }
    
    public void setFeed(boolean feed) {
        arms.set((feed) ? ARMS_DOWN : ARMS_UP);
        motor.set((feed) ? FEED_SPEED : 0.0);
    }
    
    public void startFeeding() {
        setFeed(true);
    }
    
    public void stopFeeding() {
        setFeed(false);
    }
    
    public void lowerArms() {
        arms.set(ARMS_DOWN);
    }
    
    public void raiseArms() {
        arms.set(ARMS_UP);
    }
    
    public void setFeedMotor(double speed) {
        motor.set(speed);
    }
}
