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
    
    /*
     * returns value of ball1
     */
    public boolean ball1Status()
    {
        return ball1.get();
        
    }
    
    /*
     * returns value of ball2
     */
    public boolean ball2Status()
    {
        return ball2.get();
    }
    
    /*
     * returns value of ball3
     */
    public boolean ball3Status()
    {
        return ball3.get();
    }
    /*
     * Will advance balls if there is no ball in the top slot
     */
    public void advanceBalls()
    {
        //this must be in a loop
        //assumes that if there is no ball the sensor will return false 
        
        //turns neck on
        //TODO - need to check direction to ensure this spins in the proper direction
        if (!ball1Status() && (ball2Status() || ball3Status()))
        {
            intakeMotor.setDirection(Relay.Direction.kForward);
        }
    }
    
    public void grabBalls()
    {
        //TODO - need to check direction to ensure this spins in the proper direction
        pickupMotor.setDirection(Relay.Direction.kForward);
    }
    /*
     * Takes the button to shoot the ball, needs to be fed a double for speed
     */
    public void shootBalls()
    {
        int ticker = 0;
        //25 is the numbers of ticks, 25 means 1/2 second
        if (ticker <= 25)
        {
            intakeMotor.setDirection(Relay.Direction.kForward);
            ticker++;
        }
    }
}
