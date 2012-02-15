/*
 * LlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlama
 * LlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlama
 * LlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlama
 * 
 */


//MORE LLAMAS
//ALL THE LLAMAS


package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.can.CANNotInitializedException;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 * @author Llama
 */
public class Llamahead
{
    //Creates class instance
    private static Llamahead instance;
    
    //CANJaguar objects
    private CANJaguar launchMotor;
    private Relay pickupMotor;
    private Relay advanceMotor;
    private DigitalInput ball1;
    private DigitalInput ball2;
    private DigitalInput ball3;
    
    public static final int FORWARD = 0;
    public static final int BACKWARD = 1;
    public static final int STOP = 2;
    
    public static Llamahead getInstance()
    {
        
        if (instance == null)
        {
            instance = new Llamahead();
        }
     return instance;  
    }
    
    private Llamahead()
    {
         //Creating motor control objects
//        launchMotor = new PIDJaguar(0, 0, 0, 
//                RobotMap.LLAMAHEAD_LAUNCH_MOTOR_CHANNEL, 
//                RobotMap.LLAMAHEAD_LAUNCH_SPEED_SENSOR_CHANNEL, 
//                RobotMap.LLAMAHEAD_TEETH, 1.0);
        try
        {
            launchMotor = new CANJaguar(RobotMap.LLAMAHEAD_LAUNCH_MOTOR_CHANNEL);
        }
        catch (CANTimeoutException ex)
        {
            System.out.println("CAN TIMEOUT!! Jaguar: " + RobotMap.LLAMAHEAD_LAUNCH_MOTOR_CHANNEL);
        }
        pickupMotor = new Relay (RobotMap.LLAMAHEAD_PICKUP_MOTOR_CHANNEL);
        advanceMotor = new Relay (RobotMap.LLAMAHEAD_ADVANCE_MOTOR_CHANNEL);
    }
    
    /**
     * Gets status of ball1 sensor
     * @return 
     */
    public boolean ball1Status()
    {
        return false;
//        return ball1.get();        
    }
    
    /**
     * Gets status of ball2 sensor
     * @return 
     */
    public boolean ball2Status()
    {
        return true;
//        return ball2.get();
    }
    
    /**
     * Gets status of ball3 sensor
     * @return 
     */
    public boolean ball3Status()
    {
        return true;
//        return ball3.get();
    }
    
    /**
     * Advances balls along. Can be given either Llamahead.FORWARD or Llamahead.STOP
     * @param value The value to set the ball advance motor to
     */
    public void setBallAdvance(int value)
    {
        switch (value)
        {
            case FORWARD:
                //assumes that if there is no ball the sensor will return false 

                //turns neck on
                //TODO - need to check direction to ensure this spins in the proper direction
                //if (!ball1Status() && (ball2Status() || ball3Status()))
               // {
                    advanceMotor.set(Relay.Value.kReverse);
                //}
                break;
            case BACKWARD:
                System.out.println("Ball Advance does not drive backward!!");
                break;
            case STOP:
                advanceMotor.set(Relay.Value.kOff);
                break;
        }
    }
    
    /**
     * Sets the direction of the ball pickup motor. Can be given Llamahead.FORWARD
     * Llamahead.REVERSE or Llamahead.STOP
     * @param value The value to set the ball pickup motor to
     */
    public void setBallPickup(int value)
    {
        switch (value)
        {
            case FORWARD:              
                //TODO - need to check direction to ensure this spins in the proper direction
                pickupMotor.set(Relay.Value.kForward);
                break;
            case BACKWARD:
                pickupMotor.set(Relay.Value.kReverse);
                break;
            case STOP:
                pickupMotor.set(Relay.Value.kOff);
                break;
        }
    }
    
    int ticker = 0;
    
    /**
     * Drives the wheel that launches the ball at the given speed (speed range is
     * from 0.0 to 1.0
     * @param speed The speed
     */
    public void setLauncherWheel(double speed)
    {
        if (speed < 0.0) speed = 0.0;
        if (speed > 1.0) speed = 1.0;
        try
        {
            launchMotor.setX(speed);
        }
        catch (CANTimeoutException ex)
        {
            System.out.println("CAN TIMEOUT!! Jaguar: " + RobotMap.LLAMAHEAD_LAUNCH_MOTOR_CHANNEL);
        }
    }
}
