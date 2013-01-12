package edu.shs.first.team467;

import edu.wpi.first.wpilibj.*;

/**
 * Class to control steering mechanism on Team467 2010 Robot
 * Uses WPI PID controller
 * @author callan
 */
public class Steering
{
    private double steeringLeftLimit;
    private double steeringMidpoint;
    private double steeringRightLimit;

    private AnalogChannel steeringSensor;
    private PIDController steeringPID;
    private Victor steeringMotor;

    private boolean steeringSensorWrap = false;

    class MyPIDSource implements PIDSource
    {
        public double pidGet()
        {
            return (SensorWrap(steeringSensor.getAverageValue()));
        }
    }

    /**
     * If steering range of sensor crosses the zero point
     * then adjust values to move into valid range. 
     * @param sensorValue - input sensor value
     * @return - adjusted value
     */
    private double SensorWrap(double sensorValue)
    {
        if (steeringSensorWrap == true)
        {
            sensorValue += 512.0;
            if (sensorValue > 1024.0)
            {
                sensorValue -= 1024.0;
            }
        }
        return sensorValue;
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
        System.out.print(" L:" + steeringLeftLimit);
        System.out.print(" M:" + steeringMidpoint);
        System.out.print(" R:" + steeringRightLimit);
        System.out.print(" V:" + steeringSensor.getAverageValue());
        System.out.print(" W:" + SensorWrap(steeringSensor.getAverageValue()));
        System.out.print(" O: " + steeringMotor.get());
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
     * @param left - leftmost limit sensor reading
     * @param mid - midpoint sensor reading
     * @param right - rightmost limit sensor reading
     */
    Steering(double p, double i, double d, int motor, int sensor,
                double left, double mid, double right)
    {
        steeringMotor = new Victor(motor);
        steeringSensor = new AnalogChannel(sensor);

        if ((left > mid) && (mid > right))
        {
            // Left through Right sweep doesn't cross zero threshold
            steeringSensorWrap = false;
        }
        else
        {
            // Left through Right sweep does cross zero threshold
            steeringSensorWrap = true;
        }

        steeringLeftLimit = SensorWrap(left);
        steeringRightLimit = SensorWrap(right);
        steeringMidpoint = SensorWrap(mid);
        steeringPID = new PIDController(p, i, d,
                                             new MyPIDSource(),
                                             steeringMotor);

        steeringPID.setInputRange(0.0, 1024.0);
        steeringPID.setSetpoint(steeringMidpoint);
        steeringPID.enable();
    }

    /**
     * Set angle of front steering.  -1.0 = 90 degrees Left, 0.0 = center, 1.0 = 90 degrees right
     * @param angle
     */
    public void SetAngle(double angle)
    {
        double setPoint;
        double range;

        if ((angle < -1.0) || angle > 1.0)
        {
            // out of range value - ignore
            System.out.println("Invalid angle passed to SetAngle() - " + angle);
            return;
        }

        // Sensors may be non linear so treat either side of midpoint differently
        if (angle < 0)
        {
            range = -(steeringLeftLimit - steeringMidpoint);
        }
        else
        {
            range = steeringRightLimit - steeringMidpoint;
        }
        setPoint = steeringMidpoint + angle * range;

        steeringPID.setSetpoint(setPoint);
    }

    //
    // Test code - used to tune PID steering controllers.
    // Not part of final robot code
    private boolean buttonDebounceX = false;
    private boolean buttonDebounceY = false;
    private boolean buttonDebounceA = false;
    private boolean buttonDebounceB = false;

    /**
     * Used to tune the PID subsystem
     * @param pidDelta - delta value to use. Should be negative for rear steering
     */
    void Tune(double pidDelta)
    {
        Team467DriverStation driverstation = Team467DriverStation.GetInstance();
        double setAngle = 0.0;

        double p = steeringPID.getP();
        double i = steeringPID.getI();
        double d = steeringPID.getD();

        // Hard code a number of test positions accessible
        // via button press
        if (driverstation.XboxLB)
        {
            setAngle = -0.2;
        }
        else if (driverstation.XboxRB)
        {
            setAngle = 0.2;
        }
        else if (driverstation.XboxRightTrigger)
        {
            setAngle = 1.0;
        }
        else if (driverstation.XboxLeftTrigger)
        {
            setAngle = -1.0;
        }
        else
        {
            setAngle = 0.0;
        }

        SetAngle(setAngle);

        if (driverstation.XboxX)
        {
            if (!buttonDebounceX)
            {
                p += pidDelta;
                buttonDebounceX = true;
            }
        }
        else
        {
            buttonDebounceX = false;
        }

        if (driverstation.XboxA)
        {
            if (!buttonDebounceA)
            {
                i += pidDelta;
                buttonDebounceA = true;
            }
        }
        else
        {
            buttonDebounceA = false;
        }

        if (driverstation.XboxB)
        {
            if (!buttonDebounceB)
            {
                d += pidDelta;
                buttonDebounceB = true;
            }
        }
        else
        {
            buttonDebounceB = false;
        }

        if (driverstation.XboxY)
        {
            if (!buttonDebounceY)
            {
                p = 0.006;
                d = 0.0;
                i = 0.0;
                buttonDebounceY = true;
            }
        }
        else
        {
            buttonDebounceY = false;
        }

        if (driverstation.XboxA || driverstation.XboxB ||
            driverstation.XboxX || driverstation.XboxY)
        {
            steeringPID.reset();
            steeringPID.setPID(p, i, d);
            steeringPID.enable();
        }
        Print();
    }
}
