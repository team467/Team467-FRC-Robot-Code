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
 */
public class PIDJaguar
{
    //PID Controller
    private PIDController pidController;
    
    //Motor to drive
    private CANJaguar driveMotor;
    
    //Gear tooth sensor
    private GearTooth467 gearTooth;
    
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
     * the motor is ran at maximum)
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
        pidController.setSetpoint(speed);
    }
    
    /**
     * Get the difference between actual speed and set speed (returns to two 
     * decimal places
     * @return 
     */
    public double getError()
    {
        return decimalFormat(pidController.getError(), 2);
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
        //Motor drive speed
        double driveSpeed = 0.0;
        
        public void pidWrite(double output)
        {
            //Add output (delta) to drive speed
            driveSpeed += output;
            
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
