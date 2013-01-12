/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.shs.first.team467;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.can.CANNotInitializedException;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 *
 * @author USFIRST
 */
public class Drive2011 extends RobotDrive
{
    //Single instance of this class
    private static Drive2011 instance = null;

    //Gyro object
    private static Gyro2011 gyro;

    //Drive motors
    private static final int FRONT_RIGHT_MOTOR_CHANNEL = 5; 
    private static final int FRONT_LEFT_MOTOR_CHANNEL = 7; 
    private static final int BACK_RIGHT_MOTOR_CHANNEL = 2;
    private static final int BACK_LEFT_MOTOR_CHANNEL = 6; 

    //Steering motors
    private static final int FRONT_STEERING_MOTOR_CHANNEL = 3;
    private static final int BACK_STEERING_MOTOR_CHANNEL = 4;

    //Steering sensors
    private static final int FRONT_STEERING_SENSOR_CHANNEL = 2;
    private static final int BACK_STEERING_SENSOR_CHANNEL = 3;

    //Front PID values
    private static final double FRONT_STEERING_PID_P = 0.04;
    private static final double FRONT_STEERING_PID_I = 0.0; 
    private static final double FRONT_STEERING_PID_D = 0.0; 

    //Back PID values
    private static final double BACK_STEERING_PID_P = 0.03;
    private static final double BACK_STEERING_PID_I = 0.0; 
    private static final double BACK_STEERING_PID_D = 0.0; 

    //Steering midpoint channels
    private static final double FRONT_STEERING_CENTER = 442.0;
    private static final double BACK_STEERING_CENTER = 435.0; 

    //Steering objects
    private Steering2011 frontSteering;
    private Steering2011 backSteering;

    //Line ollowing object instance

    //Private constuctor
    private Drive2011(CANJaguar frontLeftMotor, CANJaguar backLeftMotor, CANJaguar frontRightMotor,
            CANJaguar backRightMotor)
    {
        super(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor);
        frontSteering = new Steering2011(FRONT_STEERING_PID_P, FRONT_STEERING_PID_I, FRONT_STEERING_PID_D,
                        FRONT_STEERING_MOTOR_CHANNEL, FRONT_STEERING_SENSOR_CHANNEL, FRONT_STEERING_CENTER);
        backSteering = new Steering2011(BACK_STEERING_PID_P, BACK_STEERING_PID_I, BACK_STEERING_PID_D,
                        BACK_STEERING_MOTOR_CHANNEL, BACK_STEERING_SENSOR_CHANNEL, BACK_STEERING_CENTER);
        setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        gyro = Gyro2011.getInstance();
        gyro.reset();
    }

    /**
     * Gets the single instance of this class.
     * @return The single instance.
     */
    public static Drive2011 getInstance()
    {
        if (instance == null)
        {
            try
            {
                instance = new Drive2011(new CANJaguar(FRONT_LEFT_MOTOR_CHANNEL),
                                         new CANJaguar(BACK_LEFT_MOTOR_CHANNEL),
                                         new CANJaguar(FRONT_RIGHT_MOTOR_CHANNEL),
                                         new CANJaguar(BACK_RIGHT_MOTOR_CHANNEL));
            }
            catch (CANTimeoutException ex)
            {
                // TODO - print an error to the console here and retry
                ex.printStackTrace();
            }
        }
        return instance;
    }

    /**
     * Drives with the front wheels at the specified angle and the back wheels
     * at the opposite angle so turn the robot
     * @param angle The angle for the robot to turn at (maximum of 45 degrees
     * when angle 1.0 or -1.0 is given)
     * @param speed the speed to drive at
     */
    public void carDrive(double angle, double speed)
    {
        //Set maximum angle to 45 degrees
        angle /= 4;

        //Set steering angle
        frontSteering.SetAngle(angle);
        backSteering.SetAngle(-angle);

        //Drive at specified speed
        this.drive(speed, 0);
    }

    double lastSpeed;

    /**
     * called to log interesting data from the drive system to the Smart Dashboard
     */
    public void logDrive()
    {
        SmartDashboard.log(gyro.getAngle(), "GyroAngle");
        SmartDashboard.log(frontSteering.GetSensorValue(), "FrontSteeringSensor");
        SmartDashboard.log(backSteering.GetSensorValue(), "BackSteeringSensor");
    }
    /**
     * Field aligned drive. Assumes Gyro angle 0 is facing downfield
     * @param angle  value corresponding to the field direction to move in
     * @param speed
     */
    public void faDrive(double angle, double speed, double orientation)
    {
        double gyroAngle = gyro.getAngle();
        
        //Calculate the wheel angle necessary to drive in the required direction.
        double steeringAngle = angle + gyroAngle;

        if (wrapAroundDifference(frontSteering.getSteeringAngle(), steeringAngle) > 0.5)
        {
            speed = -speed;
            steeringAngle = steeringAngle - 1.0;
            if (steeringAngle < -1.0)
            {
                steeringAngle += 2.0;
            }
        }

        frontSteering.SetAngle(steeringAngle);
        backSteering.SetAngle(steeringAngle);
        if (Math.abs(speed - lastSpeed) > 0.2 && Math.abs(lastSpeed) > 0.6)
        {
            if (speed > lastSpeed)
            {
                speed = lastSpeed + 0.2;
            }
            else
            {
                speed = lastSpeed - 0.2;
            }
        }
        this.drive(speed, orientation - gyroAngle);
        lastSpeed = speed;
    }

    /**
     * Function to determine the wrapped around difference from the joystick
     * angle to the steering angle.
     * @param value1 The first angle to check against
     * @param value2 The second angle to check against
     * @return The normalized wrap around difference
     */
    private double wrapAroundDifference(double value1, double value2)
    {
        double diff = Math.abs(value1 - value2);
        if (diff > 1.0)
        {
            diff = 2.0 - diff;
        }
        return diff;
    }

    static final double SPEED_CORRECTION = 20.0;
    static final double CORRECT_LIMIT = 0.2;

    /**
     * New drive function. Allows for wheel correction using speed based on
     * a specified correction angle
     * @param speed The speed to drive at
     * @param angleCorrection the angle of correction
     */
    public void drive(double speed, double angleCorrection)
    {
        // if any of the motors doesn't exist then exit
        if (m_rearLeftMotor == null || m_rearRightMotor == null ||
              m_frontLeftMotor == null || m_rearLeftMotor == null  )
        {
            throw new NullPointerException("Null motor provided");
        }

        // magic number copied from WPI code
        byte syncGroup = (byte)0x80;

        // correct speed to each motor to allow for motor wiring
        // and orientation
        double frontLeftSpeed = speed * -1.0;  
        double frontRightSpeed = speed * -1.0;
        double rearLeftSpeed = speed * 1.0;
        double rearRightSpeed = speed * -1.0;

        // read current wheel orientation. This is used to determine
        // how much compensation to apply to each wheel
        double wheelAngle = frontSteering.getSteeringAngle();

        // based on the orientation of the wheels relative to the robot,
        // determine how much weighting to apply to the left vs the front drive system.
        // Note:  right is negative left and rear is negative front so don't need
        // to be calculated separately
        
        double leftFactor = Math.cos(wheelAngle * Math.PI);
        double frontFactor = Math.sin(wheelAngle * Math.PI);

        double frontLeftFactor = limit(leftFactor + frontFactor) * angleCorrection * SPEED_CORRECTION;
        double frontRightFactor = limit(-leftFactor + frontFactor) * angleCorrection * SPEED_CORRECTION;
        double rearLeftFactor = limit(leftFactor - frontFactor) * angleCorrection * SPEED_CORRECTION;
        double rearRightFactor = limit(-leftFactor - frontFactor) * angleCorrection * SPEED_CORRECTION;

        // Limit individual wheel speed variation to 20% maximim
        if (frontLeftFactor > CORRECT_LIMIT) frontLeftFactor = CORRECT_LIMIT;
        if (frontRightFactor > CORRECT_LIMIT) frontRightFactor = CORRECT_LIMIT;
        if (rearLeftFactor > CORRECT_LIMIT) rearLeftFactor = CORRECT_LIMIT;
        if (rearRightFactor > CORRECT_LIMIT) rearRightFactor = CORRECT_LIMIT;
        if (frontLeftFactor < -CORRECT_LIMIT) frontLeftFactor = -CORRECT_LIMIT;
        if (frontRightFactor < -CORRECT_LIMIT) frontRightFactor = -CORRECT_LIMIT;
        if (rearLeftFactor < -CORRECT_LIMIT) rearLeftFactor = -CORRECT_LIMIT;
        if (rearRightFactor < -CORRECT_LIMIT) rearRightFactor = -CORRECT_LIMIT;
        
        if (speed < 0.0)
        {
            // Forward direction, add compensation to speed
            frontLeftSpeed *= 1.0 + frontLeftFactor;
            frontRightSpeed *= 1.0 + frontRightFactor;
            rearLeftSpeed *= 1.0 + rearLeftFactor;
            rearRightSpeed *= 1.0 + rearRightFactor;
        }
        else
        {
            // Reverse direction, subtract compensation from speed
            frontLeftSpeed *= 1.0 - frontLeftFactor;
            frontRightSpeed *= 1.0 - frontRightFactor;
            rearLeftSpeed *= 1.0 - rearLeftFactor;
            rearRightSpeed *= 1.0 - rearRightFactor;
        }

        // determine the fastest wheel speed. Use absolute value to accomodate motors that
        // may be wired to turn in different directions
        double maxSpeed = Math.max(
                Math.max(Math.abs(frontLeftSpeed), Math.abs(frontRightSpeed)),
                Math.max(Math.abs(rearLeftSpeed), Math.abs(rearRightSpeed))
                );
        
        // one of the motors requires a speed greater than 1.
        // scale others down to compensate
        if (maxSpeed > 1.0)
        {
            double speedRatio = 1.0/maxSpeed;

            frontLeftSpeed *= speedRatio;
            frontRightSpeed *= speedRatio;
            rearLeftSpeed *= speedRatio;
            rearRightSpeed *= speedRatio;
        }

        m_frontLeftMotor.set(limit(frontLeftSpeed), syncGroup);
        m_rearLeftMotor.set(limit(rearLeftSpeed), syncGroup);
        m_frontRightMotor.set(limit(frontRightSpeed), syncGroup);
        m_rearRightMotor.set(limit(rearRightSpeed), syncGroup);

        if (m_isCANInitialized)
        {
            try
            {
                CANJaguar.updateSyncGroup(syncGroup);
            }
            catch (CANNotInitializedException e)
            {
                m_isCANInitialized = false;
            }
            catch (CANTimeoutException e)
            {
            }
        }
        if (m_safetyHelper != null) m_safetyHelper.feed();
    }

}
