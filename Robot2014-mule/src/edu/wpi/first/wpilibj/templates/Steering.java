package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

/**
 * Class to control steering mechanism on Team467 2010 Robot
 * Uses WPI PID controller
 * @author shrewsburyrobotics
 */
public class Steering
{
    //Sensor used to determine angle
    private AnalogChannel steeringSensor;
    
    //PID Controller object
    private PIDController steeringPID;
    
    //Steering motor
    private Talon steeringMotor;
    
    //Center point of this steering motor
    private double steeringCenter;

    /**
     * Class which deals with value used when checking PID (sensor value)
     */
    class SteeringPIDSource implements PIDSource
    {
        public double pidGet()
        {
            return (steeringSensor.getAverageValue());
        }
    }
    
    /**
     * Constructor for steering subsystem
     * @param p - P parameter to use in PID
     * @param i - I parameter to use in PID
     * @param d - D parameter to use in PID
     * @param motor - motor channel
     * @param sensor - analog sensor channel
     * @param center - sensor reading when wheels point forward
     */
    Steering(double p, double i, double d,
             int motor, int sensor, double center)
    {
        //Make steering motor
        steeringMotor = new Talon(motor);        
        
        //Make steering sensor
        steeringSensor = new AnalogChannel(sensor);

        //Set steering center
        steeringCenter = center;
        
        //Make PID Controller
        steeringPID = new PIDController(p, i, d, new SteeringPIDSource(), steeringMotor);

        //Set PID Controller settings
        steeringPID.setInputRange(0.0, RobotMap.STEERING_RANGE);
        steeringPID.setSetpoint(steeringCenter);
        steeringPID.setContinuous(true);
        steeringPID.enable();
    }

    /**
     * Get directly the value of the sensor
     * @return The sensor value, read from 0 to 990
     */
    public double getSensorValue()
    {
        return steeringSensor.getAverageValue();
    }

    public double getSetPoint()
    {
        return steeringPID.getSetpoint();
    }
    
    /**
     * Get the Talon motor of this steering object
     * @return 
     */
    public Talon getMotor()
    {
        return steeringMotor;
    }
    
    /**
     * Get the sensor angle normalized to a -1.0 to 1.0 range
     * Implements the steering center point to give an angle accurate to the
     * robot's alignment.
     * @return 
     */
    public double getSteeringAngle()
    {
        double sensor = steeringSensor.getAverageValue() - steeringCenter;
        if (sensor < (-RobotMap.STEERING_RANGE / 2))
        {
            sensor += RobotMap.STEERING_RANGE;
        }
        if (sensor > (RobotMap.STEERING_RANGE / 2))
        {
            sensor -= RobotMap.STEERING_RANGE;
        }
        return (sensor) / (RobotMap.STEERING_RANGE / 2);
    }

    /**
     * Print steering parameters
     */
    public void printParameters()
    {
        System.out.print("Steering:");
        System.out.print(" P: " + steeringPID.getP());
        System.out.print(" I: " + steeringPID.getI());
        System.out.print(" D: " + steeringPID.getD());
        System.out.print(" M:" + steeringCenter);
        System.out.print(" V:" + steeringSensor.getAverageValue());
        System.out.print(" S: " + steeringPID.getSetpoint());
        System.out.println();
    }

    /**
     * Set angle of front steering.  -1.0 = 180 degrees Left, 0.0 = center, 1.0 = 180 degrees right
     * @param angle
     */
    public void setAngle(double angle)
    {
        double setPoint;

        if (angle < -1.0)
        {
            angle += 2.0;
        }
        if (angle > 1.0)
        {
            angle -= 2.0;
        }

        //Calculate desired setpoint for PID based on known center position
        setPoint = steeringCenter + angle * (RobotMap.STEERING_RANGE / 2);

        //Normalize setPoint into the -990 to +990 range
        if (setPoint < 0.0)
        {
            setPoint += RobotMap.STEERING_RANGE;
        }
        if (setPoint >= RobotMap.STEERING_RANGE)
        {
            setPoint -= RobotMap.STEERING_RANGE;
        }

        steeringPID.setSetpoint(setPoint);
    }
    
    /**
     * Change the center point of this steering motor
     * @param center 
     */
    public void setCenter(double center)
    {
        steeringCenter = center;
    }
}
