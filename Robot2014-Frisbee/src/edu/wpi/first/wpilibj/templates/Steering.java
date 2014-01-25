package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

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
     * Used when passed into the PID controller
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
     * @param range - range for this steering sensor, around 960
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

        //Set sterring sensor's max range
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
     * @return The sensor value, read from 0 to maximum range
     */
    public double getSensorValue()
    {
        return steeringRange - steeringSensor.getAverageValue();

    }

    /**
     * @return - setpoint of the PID controller
     */
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
     * @return - steering angle
     */
    public double getSteeringAngle()
    {
        //angle away from center, (sensor about 0 to 990) - (center about 0 to 990)
        //final value between 990 and -990
        double sensor = getSensorValue() - steeringCenter;
        
        //in the lowest quarter of possible sensor values
        if (sensor < (-steeringRange / 2))
        {
            //wrap around so that it is now in the second highest quarter
            sensor += steeringRange;
        }
        //in the highest quarter of possible sensor values
        if (sensor > (steeringRange / 2))
        {
            //wrap around so that it is now in the second lowest quarter
            sensor -= steeringRange;
        }
        
        //value is in quarter 2 or quarter 3, or +/- 1/2 steering range
        //value is divided to normalize it between 1 and -1
        return (sensor) / (steeringRange / 2);
    }

    /**
     * Print steering parameters
     */
    public void printSteeringStatus()
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
     * @param desiredAngle - any value between -1 and 1
     */
    public void setAngle(double desiredAngle)
    {
        double setPoint;
        
        //wrap around values to be between 1 and -1
        if (desiredAngle < -1.0)
        {
            desiredAngle += 2.0;
        }
        if (desiredAngle > 1.0)
        {
            desiredAngle -= 2.0;
        }
        
        //Calculate desired setpoint for PID based on known center position
        //setPoint with a posible range between ~ -990 and 990
        setPoint = steeringCenter + (desiredAngle * (steeringRange / 2));

        //Normalize setPoint into the 0 to +990 range
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
     * Change the center point of this steering motor, needs a value between 0 and ~990
     *
     * @param center
     */
    public void setCenter(double center)
    {
        steeringCenter = center;
    }
    
    /**
     * Get the steering center for each wheel, returns a value between 0 and ~990
     * @return center value
     */
    public double getSteeringCenter()
    {
        return steeringCenter;
    }

}
