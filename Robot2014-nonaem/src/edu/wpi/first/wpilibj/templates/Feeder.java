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

    public static final int NUM_WAIT_CYCLE = 3;
    
    public static final Relay.Value ARMS_UP = Relay.Value.kForward;
    public static final Relay.Value ARMS_DOWN = Relay.Value.kOff;

    /**
     * Private constructor only called by getInstance()
     */
    private Feeder()
    {
        motor = new Talon(RobotMap.FEEDER_MOTOR_CHANNEL);
        arm = new Solenoid(RobotMap.FEEDER);
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
     * Enables the feeder motor to drive.
     *
     * @param drive <b>true</b>: drive the motor <b>false</b>:turn the motor off
     */
    public void driveFeederMotor(double speed)
    {
        motor.set(speed);
    }

    int readyIterator = 0;
    public boolean armsReadyForFire()
    {
        //if arm is not down
        if (!arm.get() && readyIterator == 0)
        {
            //arm set to down
            arm.set(true);
            readyIterator ++;
            return false;
        }
        else if (readyIterator <= NUM_WAIT_CYCLE)
        {
            //arm set to down
            arm.set(true);
            readyIterator ++;
            return false;
        }
        //arm down
        else
        {
            readyIterator = 0;
            return true;
        }
    }
}
