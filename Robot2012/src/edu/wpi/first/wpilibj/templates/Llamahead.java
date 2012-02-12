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
    private PIDJaguar launchMotor;
    private Relay pickupMotor;
    private Relay intakeMotor;
    private DigitalInput ball1;
    private DigitalInput ball2;
    private DigitalInput ball3;
    
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
        launchMotor = new PIDJaguar(0, 0, 0, 
                RobotMap.LLAMAHEAD_LAUNCH_MOTOR_CHANNEL, 
                RobotMap.LLAMAHEAD_LAUNCH_SPEED_SENSOR_CHANNEL, 
                RobotMap.LLAMAHEAD_TEETH, 1.0);
        pickupMotor = new Relay (RobotMap.LLAMAHEAD_PICKUP_MOTOR_CHANNEL);
        intakeMotor = new Relay (RobotMap.LLAMAHEAD_INTAKE_MOTOR_CHANNEL);
    }
}
