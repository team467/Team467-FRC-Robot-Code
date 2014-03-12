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
    private Solenoid feeder;

    // time in milliseconds it takes arm to lower
    public static final int FEEDER_LOWER_TIME = 500;  
    
    /**
     * Private constructor only called by getInstance()
     */
    private Feeder()
    {
        motor = new Talon(RobotMap.FEEDER_MOTOR_CHANNEL);
        feeder = new Solenoid(RobotMap.FEEDER);
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

    long feederDownTime = 0;
    /**
     * Puts the feeder into feeding position
     */
    public void lowerFeeder()
    {
        //sets the arms on or off
        //these are the correct orientation
        feeder.set(false);
        // log the time the arm started to drop
        feederDownTime = System.currentTimeMillis();
    }

    /**
     * Puts the feeder up into storage position.
     */
    public void raiseFeeder()
    {
        //sets the arms on or off      
        //these are the correct orientation
        feeder.set(true);
    }

    /**
     * Enables the feeder motor to drive.
     *
     * @param drive <b>true</b>: drive the motor <b>false</b>:turn the motor off
     */
    public void driveFeederMotor(double speed)
    {
        motor.set(-speed);
    }
    
    public boolean feederReadyForFire()
    {
        if (feeder.get())
        {
            long feederDeltaTime = System.currentTimeMillis() - feederDownTime;
            if (feederDeltaTime >= FEEDER_LOWER_TIME)
            {
                return true;
            }
        }
        return false;
    }
}
