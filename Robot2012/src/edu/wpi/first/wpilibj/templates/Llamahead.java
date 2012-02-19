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
    private PIDJaguar launchMotor;
    private Relay scoopMotor;
    private Relay neckMotor;
    private Relay intakeMotor;
    private DigitalInput ball;
    
    //Number of teeth on the gear measuring speed
    private final int TEETH = 1;
    
    //Direction constants
    public static final int FORWARD = 0;
    public static final int BACKWARD = 1;
    public static final int STOP = 2;
    
    //Perportional gain (p in PID)
    final double GAIN = 1.0 / 256.0;
     
    //Threshold of acceptability for pIDJaguar speed
    final double THRESHOLD = 2.0;
    
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
        //Creating motor control objects
        launchMotor = new PIDJaguar(GAIN, 0.0, 0.0, RobotMap.LLAMAHEAD_LAUNCH_MOTOR_CHANNEL,
                RobotMap.LLAMAHEAD_LAUNCH_SPEED_SENSOR_CHANNEL, TEETH, 50.0);
        //launchMotor = new CANJaguar(RobotMap.LLAMAHEAD_LAUNCH_MOTOR_CHANNEL);
        scoopMotor = new Relay (RobotMap.LLAMAHEAD_SCOOP_MOTOR_CHANNEL);
        intakeMotor = new Relay (RobotMap.LLAMAHEAD_INTAKE_MOTOR_CHANNEL); 
        neckMotor = new Relay (RobotMap.LLAMAHEAD_NECK_MOTOR_CHANNEL);
        //Create sensor object
        //ball = new DigitalInput(RobotMap.LLAMAHEAD_BALL_SENSOR_CHANNEL);
    }
    
    /**
     * Gets status of ball sensor
     * @return 
     */
    public boolean ballStatus()
    {
        return false;
//        return ball.get();        
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
                //Assumes that if there is no ball the sensor will return false 

                //Turns neck on
//                if (!ballStatus())
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
    
    //Whether or not the luanch motor is at the correct speed
    private boolean atSpeed = false;
    
    /**
     * Drives the wheel that launches the ball at the given speed (speed range is
     * from 0.0 to 1.0
     * @param speed The speed in revolutions per second
     */
    public void setLauncherWheel(double targetSpeed)
    {
        //dont allow neg speeds
        if (targetSpeed < 0.0) targetSpeed = 0.0;
        
        //Drive to target speed
        launchMotor.setSpeed(targetSpeed);
        
        //Register correct speed if error is less than threshold
        if (Math.abs(launchMotor.getError()) < THRESHOLD)
        {
            //Motor has reached speed
            atSpeed = true;
        }
        else
        {
            atSpeed = false;
        }
    }
    
    /**
     * Whether or not the launch motor is at the target speed
     * @return 
     */
    public boolean atSpeed()
    {
        return atSpeed;
    }
}
