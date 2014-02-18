package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author Spencer
 */
public class Feeder
{

    private static Feeder instance;

    private Talon motor;
    private Solenoid arm;
//    private Solenoid armRight;

    public static final Relay.Value ARMS_UP = Relay.Value.kForward;
    public static final Relay.Value ARMS_DOWN = Relay.Value.kOff;
        

    public static double feederSpeed = -0.6;

    /**
     * Private constructor only called by getInstance()
     */
    private Feeder()
    {
        motor = new Talon(RobotMap.FEEDER_MOTOR_CHANNEL);
        arm = new Solenoid(RobotMap.FEEDER);
//        armRight = new Solenoid(RobotMap.FEEDER_RIGHT);
//        arms = new Relay(RobotMap.FEEDER_SOLENOID_CHANNEL);
    }

    /**
     * Returns singleton instance of the feeder.
     *
     * @return Feeder
     */
    public static Feeder getInstance()
    {
        if (instance == null)
        {
            instance = new Feeder();
        }

        return instance;
    }

    /**
     * Sets if to feed
     *
     * @param feed <b>true</b>: arms down, feed motor on; <b>false</b>: arms uo, feed motor
     * off;
     */
    public void feed(boolean feed)
    {
        //sets the arms on or off. TODO: confirm direction
        arm.set(feed);
//        armRight.set(feed);
//        arms.set((feed) ? ARMS_DOWN : ARMS_UP);
        motor.set((feed) ? feederSpeed : 0.0);
    }

    
    
    /**
     * Puts the feeder into feeding position
     */
    public void lowerArms()
    {
        
        //sets the arms on or off. TODO: confirm direction
        arm.set(true);
//        armRight.set(true);
    }

    /**
     * Puts the feeder up into storage position.
     */
    public void raiseArms()
    {
        //sets the arms on or off. TODO: confirm direction        
        arm.set(false);
//        armRight.set(false);
    }

    /**
     * Sets the speed for feeding the motor . This sets the speed for future
     * motor actions, such as feed()
     *
     * @param speed neg vals intake, pos vals dispense
     */
    public void setFeedMotorSpeed(double speed)
    {
        feederSpeed = speed;
    }

    /**
     * Enables the feeder motor to drive.
     * 
     * @param drive  <b>true</b>: drive the motor <b>false</b>:turn the motor off
     */
    public void driveFeederMotor(boolean drive)
    {        
        motor.set((drive) ? feederSpeed : 0);
    }
}
