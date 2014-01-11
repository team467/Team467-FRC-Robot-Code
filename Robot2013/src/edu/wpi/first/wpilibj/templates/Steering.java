package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 * Class to control steering mechanism on Team467 2010 Robot Uses WPI PID
 * controller
 *
 * @author shrewsburyrobotics
 */
public class Steering
{

    //Sensor used to determine angle
    private AnalogChannel steeringSensor;

    //PID Controller object
    private PIDController steeringPID;

    //Steering motor
    private Jaguar steeringMotor;

    //Center point of this steering motor
    private double steeringCenter;

    //Steering sensor range
    private double steeringRange;

    /**
     * Class which deals with value used when checking PID (sensor value)
     */
    class MyPIDSource implements PIDSource
    {

        public double pidGet()
        {
            return (getSensorValue());
        }
    }

    /**
     * Constructor for steering subsystem
     *
     * @param p - P parameter to use in PID
     * @param i - I parameter to use in PID
     * @param d - D parameter to use in PID
     * @param motor - motor channel
     * @param sensor - analog sensor channel
     * @param center - sensor reading when wheels point forward
     */
    Steering(double p, double i, double d,
            int motor, int sensor, double center, double range)
    {
        //Make steering motor        
        steeringMotor = new Jaguar(motor);

        //Make steering sensor
        steeringSensor = new AnalogChannel(sensor);

        //Set steering center
        steeringCenter = center;

        steeringRange = range;

        //Make PID Controller
        steeringPID = new PIDController(p, i, d, new MyPIDSource(), steeringMotor);

        //Set PID Controller settings
        steeringPID.setInputRange(0.0, steeringRange);
        steeringPID.setOutputRange(-1.0, 1.0);
        steeringPID.setSetpoint(steeringCenter);
        steeringPID.setContinuous(true);
        steeringPID.enable();
    }

    /**
     * Get directly the value of the sensor
     *
     * @return The sensor value, read from 0 to 990
     */
    public double getSensorValue()
    {
        return steeringRange - steeringSensor.getAverageValue();

    }

    public double getSetPoint()
    {
        return steeringPID.getSetpoint();
    }

    /**
     * Get the Jaguar motor of this steering object
     *
     * @return
     */
    public Jaguar getMotor()
    {
        return steeringMotor;
    }

    /**
     * Get the sensor angle normalized to a -1.0 to 1.0 range Implements the
     * steering center point to give an angle accurate to the robot's alignment.
     *
     * @return
     */
    public double getSteeringAngle()
    {
        double sensor = getSensorValue() - steeringCenter;
        if (sensor < (-steeringRange / 2))
        {
            sensor += steeringRange;
        }
        if (sensor > (steeringRange / 2))
        {
            sensor -= steeringRange;
        }
        return (sensor) / (steeringRange / 2);
    }

    /**
     * Print steering parameters
     */
    public void print()
    {
        System.out.print("Steering:");
        System.out.print(" P: " + steeringPID.getP());
        System.out.print(" I: " + steeringPID.getI());
        System.out.print(" D: " + steeringPID.getD());
        System.out.print(" M:" + steeringCenter);
        System.out.print(" V:" + getSensorValue());
        System.out.print(" S: " + steeringPID.getSetpoint());
        System.out.println();
    }

    /**
     * Set angle of front steering. -1.0 = 180 degrees Left, 0.0 = center, 1.0 =
     * 180 degrees right
     *
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
        setPoint = steeringCenter + (angle * (steeringRange / 2));

        //Normalize setPoint into the -990 to +990 range
        if (setPoint < 0.0)
        {
            setPoint += steeringRange;
        }
        if (setPoint >= steeringRange)
        {
            setPoint -= steeringRange;
        }

        steeringPID.setSetpoint(setPoint);
    }

    /**
     * Change the center point of this steering motor
     *
     * @param center
     */
    public void setCenter(double center)
    {
        steeringCenter = center;
    }

}
