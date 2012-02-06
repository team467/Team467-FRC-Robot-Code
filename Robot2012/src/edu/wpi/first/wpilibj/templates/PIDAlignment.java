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
 * @author aidan This class deals with pid control to turndrive the robot to a 
 * specified orientation
 */
public class PIDAlignment
{
    //PID Controller
    private PIDController pidController;
    
    //Drive object
    private Drive drive;
    
    //Gyro
    private Gyro467 gyro;
    
    //Drive speed
    private double driveSpeed = 0.0;
    
    /**
     * Make a new PIDAlignment object
     * @param p The P value of the PID
     * @param i The I value of the PID
     * @param d The D value of the PID
     */
    public PIDAlignment(double p, double i, double d)
    {
        //Make objects
        drive = Drive.getInstance();
        gyro = Gyro467.getInstance();
        pidController = new PIDController(p, i, d, new Input(), new Output());
        
        //Set PID Controller settings
        pidController.setContinuous(true);
        pidController.setInputRange(-1.0, 1.0);
        pidController.setOutputRange(-0.5, 0.5);
        pidController.setSetpoint(0.0);
        pidController.enable();
    }
    
    /**
     * Set the gyro orientation to go to from -1.0 to 1.0
     */
    public void setOrientation(double orientation)
    {
        pidController.setSetpoint(orientation);
        drive.turnDrive(-driveSpeed);
    }
    
    /**
     * PIDSource class to define the input value for the PID Controller
     */
    class Input implements PIDSource
    {
        public double pidGet()
        {           
            return gyro.getAngle();
        }      
    }
    
    /**
     * PID Output class to determine what to do with PID Output values
     */
    class Output implements PIDOutput
    {   
        public void pidWrite(double output)
        {
            driveSpeed = output;
            System.out.println(driveSpeed);
        }      
    }
}
