/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 *
 * @author aidan 
 * This class allows speed control of a Jaguar by the use of a geartooth sensor.
 * Instead of feeding in a value which is directly written to the motor, a speed
 * is given and the PID controller drives the Jaguar to that speed based on
 * the speed reading from the geartooth sensor.
 */
public class PIDJaguar
{
    //PID Controller
    private PIDController pidController;
    
    //Motor to drive
    private CANJaguar driveMotor;
    
    //Gear tooth sensor
    private GearTooth467 gearTooth;
    
    //Motor drive speed
    private double driveSpeed = 0.0;
    
    private static final int TYPE_SPEED = 0;
    private static final int TYPE_PWM = 1;
    
    private int type = TYPE_SPEED;
    
    /**
     * Make a new PIDJaguar object
     * @param p The P value of the PID
     * @param i The I value of the PID
     * @param d The D value of the PID
     * @param motorChannel The channel of the drive motor
     * @param sensorChannel The channel of the gear tooth sensor
     * @param teeth Number of teeth on the gear being read
     * @param inputMax The maximum input the PID Controller can receive (this 
     * should be equivalent to the speed read by the gear tooth sensor when 
     * the motor is run at maximum)
     */
    public PIDJaguar(double p, double i, double d, 
            int motorChannel, int sensorChannel, int teeth, double inputMax )
    {
        //Make objects
        try
        {
            driveMotor = new CANJaguar(motorChannel);
        }
        catch (CANTimeoutException ex)
        {
            ex.printStackTrace();
        }
        pidController = new PIDController(p, i, d, new Input(), new Output());
        gearTooth = new GearTooth467(sensorChannel, teeth);
        
        //Set PID Controller settings
        pidController.setInputRange(0.0, inputMax);
        pidController.setOutputRange(-1.0, 1.0);
        pidController.setSetpoint(0.0);
        pidController.enable();
        
        //Start the geartooth sensor
        gearTooth.start();
    }
    
    /**
     * Set the angular speed of this motor in rotations/second
     */
    public void setSpeed(double speed)
    {
        type = TYPE_SPEED;
        pidController.setSetpoint(speed);
    }
    
    /**
     * Directly set PWM value of the motor
     * @param speed 
     */
    public void setPWM(double speed)
    {
        if (speed > 1.0)
        {
            speed = 1.0;
        }
        type = TYPE_PWM;
        try
        {
            driveSpeed = getSpeed() / 53.0;
            driveMotor.setX(speed);
        }
        catch (CANTimeoutException ex)
        {
            ex.printStackTrace();
        }
    }
    
    /**
     * Get the last set value of the motor
     * @return 
     */
    public double getPWM()
    {
        try
        {
            return driveMotor.getX();
        }
        catch (CANTimeoutException ex)
        {
            ex.printStackTrace();
        }
        return 0.0;
    }
    
    /**
     * Get the difference between actual speed and set speed (returns to two 
     * decimal places
     * @return 
     */
    public double getError()
    {
        return pidController.getError();
    }
    
    /**
     * Get the speed as read by the geartooth sensor
     * @return Speed in rotations/second
     */
    public double getSpeed()
    {
        return gearTooth.getAngularSpeed();
    }
    
    /**
     * Enables the pid controller
     */
    public void enable()
    {
        pidController.reset();
        pidController.enable();
        driveSpeed = 0.0;
    }
    
    /**
     * Disables the pid controller
     */
    public void disable()
    {
        pidController.disable();
    }
    
    /**
     * Returns the given double as a double to the number of specified places
     * @param d The given double
     * @param i The number of decimal places
     * @return 
     */
    private double decimalFormat(double d, int i)
    {
        double p = 1;
        for (int j = 0; j < i; j++)
        {
            p *= 10.0;
        }
        return Math.floor(d * p) / p;
    }
    
    /**
     * PIDSource class to define the input value for the PID Controller
     */
    class Input implements PIDSource
    {
        public double pidGet()
        {           
            return gearTooth.getAngularSpeed();
        }      
    }
    
    /**
     * PID Output class to determine what to do with PID Output values
     */
    class Output implements PIDOutput
    {   
        int iterationTicker = 0;
        
        public void pidWrite(double output)
        {
            //Only use PID controller if not using pwm control
            if (type == TYPE_SPEED)
            {
                //Add output (delta) to drive speed
                if (iterationTicker >= 15)
                {
                    driveSpeed += output;
                    iterationTicker = 0;
                }
                iterationTicker++;
                System.out.println("Output: " + output + " Speed: " + driveSpeed);

                //Stop the motor completely if the setpoint is 0
                if (pidController.getSetpoint() == 0.0)
                {
                    driveSpeed = 0.0;
                }
                try
                {
                    //Drive at speed with delta added
                    driveMotor.setX(driveSpeed);
                }
                catch (CANTimeoutException ex)
                {
                    ex.printStackTrace();
                }
            }
        }      
    }
    
}
