/*
 * LlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlama
 * LlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlama
 * LlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlama
 * 
 */


//MORE LLAMAS
//ALL THE LLAMAS


package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.can.CANNotInitializedException;

/**
 * @author Llama
 */
public class Llamahead
{
    //Creates class instance
    private static Llamahead instance;
    
    //CANJaguar objects
    public PIDJaguar launchMotor;
    public Relay pickupMotor;
    public Relay liftMotor;
    public Relay loadMotor;
    
    private Llamahead()
    {    
        //Creating motor control objects
        launchMotor = new PIDJaguar(0, 0, 0, 
                RobotMap.LLAMAHEAD_LAUNCH_MOTOR_CHANNEL, 
                RobotMap.LLAMAHEAD_LAUNCH_SPEED_SENSOR_CHANNEL, 
                RobotMap.LLAMAHEAD_TEETH, 1.0);
        pickupMotor = new Relay (RobotMap.LLAMAHEAD_PICKUP_MOTOR_CHANNEL);
        liftMotor = new Relay (RobotMap.LLAMAHEAD_LIFT_MOTOR_CHANNEL);
        loadMotor = new Relay (RobotMap.LLAMAHEAD_LOAD_MOTOR_CHANNEL);   
    }
}
