package edu.shs.first.team467;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 * Class to control steering mechanism on Team467 2010 Robot
 * Uses WPI PID controller
 * @author callan
 */
public class Steering2011
{
    private double steeringCenter;

    private AnalogChannel steeringSensor;
    private PIDController steeringPID;
    private CANJaguar steeringMotor;

    class MyPIDSource implements PIDSource
    {
        public double pidGet()
        {
            return (steeringSensor.getAverageValue());
        }
    }

    public double GetSensorValue()
    {
        return steeringSensor.getAverageValue();

    }

    public double getSetPoint()
    {
        return steeringPID.getSetpoint();
    }
    

    public double getSteeringAngle()
    {
        double sensor = steeringSensor.getAverageValue() - steeringCenter;
        if (sensor < -480.0)
        {
            sensor += 960.0;
        }
        if (sensor > 480.0)
        {
            sensor -= 960.0;
        }
        return (sensor) / 480.0;
    }

    /**
     * Print steering parameters
     */
    public void Print()
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
     * Constructor for steering subsystem
     * @param p - P parameter to use in PID
     * @param i - I parameter to use in PID
     * @param d - D parameter to use in PID
     * @param motor - motor channel
     * @param sensor - analog sensor channel
     * @param center - sensor reading when wheels point forward
     */
    Steering2011(double p, double i, double d,
             int motor, int sensor, double center)
    {
        try
        {
            steeringMotor = new CANJaguar(motor);
        }
        catch (CANTimeoutException ex)
        {
            ex.printStackTrace();
        }
        steeringSensor = new AnalogChannel(sensor);

        steeringCenter = center;
        steeringPID = new PIDController(p, i, d,
                                        new MyPIDSource(),
                                        steeringMotor);

        steeringPID.setInputRange(0.0, 960.0);
        steeringPID.setSetpoint(steeringCenter);
        steeringPID.setContinuous(true);
        steeringPID.enable();
    }

    /**
     * Set angle of front steering.  -1.0 = 180 degrees Left, 0.0 = center, 1.0 = 180 degrees right
     * @param angle
     */
    public void SetAngle(double angle)
    {
        double setPoint;

        // normalize angle into the range -1 to +1
        double nAngle = angle % 2;
        if (angle < -1.0)
        {
            angle += 2.0;
        }
        if (angle > 1.0)
        {
            angle -= 2.0;
        }

        // calculate desired setpoint for PID based on known center position
        setPoint = steeringCenter + angle * 480.0;

        // normalize setPoint into the -960 to +960 range
        if (setPoint < 0.0)
        {
            setPoint += 960.0;
        }
        if (setPoint >= 960.0)
        {
            setPoint -= 960.0;
        }

        steeringPID.setSetpoint(setPoint);
    }

}
