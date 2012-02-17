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
    private Relay scoopMotor;
    private Relay neckMotor;
    private Relay intakeMotor;
    private DigitalInput ball1;
    private DigitalInput ball2;
    private DigitalInput ball3;
    
    private final int TEETH = 18;
    
    //Direction constants
    public static final int FORWARD = 0;
    public static final int BACKWARD = 1;
    public static final int STOP = 2;
    
    /**
     * Gets the single instance of this class
     * @return 
     */
    public static Llamahead getInstance()
    {
        if (instance == null)
        {
            instance = new Llamahead();
        }
     return instance;  
    }
    
    //Private constructor for singleton
    private Llamahead()
    {
        try
        {
            //Creating motor control objects
           //launchMotor = new PIDJaguar(0.001, 0.0, 0.0, RobotMap.LLAMAHEAD_LAUNCH_MOTOR_CHANNEL,
           //        RobotMap.LLAMAHEAD_LAUNCH_SPEED_SENSOR_CHANNEL, TEETH, 100.0);
           launchMotor = new CANJaguar(RobotMap.LLAMAHEAD_LAUNCH_MOTOR_CHANNEL);
        }
        catch (CANTimeoutException ex)
        {
            ex.printStackTrace();
        }
        scoopMotor = new Relay (RobotMap.LLAMAHEAD_SCOOP_MOTOR_CHANNEL);
        intakeMotor = new Relay (RobotMap.LLAMAHEAD_INTAKE_MOTOR_CHANNEL); 
        neckMotor = new Relay (RobotMap.LLAMAHEAD_NECK_MOTOR_CHANNEL);
        
        //Create sensor objects
//        ball1 = new DigitalInput(RobotMap.LLAMAHEAD_BALL_FIRST_SENSOR_CHANNEL);
//        ball2 = new DigitalInput(RobotMap.LLAMAHEAD_BALL_SECOND_SENSOR_CHANNEL);
//        ball3 = new DigitalInput(RobotMap.LLAMAHEAD_BALL_THIRD_SENSOR_CHANNEL);
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
//                if (!ball1Status() && (ball2Status() || ball3Status()))
//                {
                    neckMotor.set(Relay.Value.kReverse);
//                }
                break;
            case BACKWARD:
                System.out.println("Ball Advance does not drive backward!!");
                break;
            case STOP:
                neckMotor.set(Relay.Value.kOff);
                break;
        }
    }
    
    /**
     * Sets the direction of the ball pickup motor. Can be given Llamahead.FORWARD
     * Llamahead.REVERSE or Llamahead.STOP
     * @param value The value to set the ball pickup motor to
     */
    public void setBallIntake(int value)
    {
        switch (value)
        {
            case FORWARD:              
                scoopMotor.set(Relay.Value.kReverse);
                intakeMotor.set(Relay.Value.kForward);
                break;
            case BACKWARD:
                scoopMotor.set(Relay.Value.kReverse);
                intakeMotor.set(Relay.Value.kForward);
                break;
            case STOP:
                scoopMotor.set(Relay.Value.kOff);
                intakeMotor.set(Relay.Value.kOff);
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
            ex.printStackTrace();
        }
    }
}
