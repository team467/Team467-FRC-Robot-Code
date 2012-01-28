/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.can.CANNotInitializedException;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author USFIRST
 */
public class Drive extends RobotDrive
{
    //Single instance of this class
    private static Drive instance = null;

    //Gyro object
    private static Gyro467 gyro;

    //Steering objects
    private Steering[] steering;
    
    //Data storage object
    private Preferences data;
    
    //Steering constant array
    private static final int[] STEERING_MOTOR_CHANNELS = 
    {
        RobotMap.FRONT_LEFT_STEERING_MOTOR_CHANNEL,
        RobotMap.FRONT_RIGHT_STEERING_MOTOR_CHANNEL,
        RobotMap.BACK_LEFT_STEERING_MOTOR_CHANNEL,
        RobotMap.BACK_RIGHT_STEERING_MOTOR_CHANNEL
    };
    
    //Steering sensor constant array
    private static final int[] STEERING_SENSOR_CHANNELS = 
    {
        RobotMap.FRONT_LEFT_STEERING_SENSOR_CHANNEL,
        RobotMap.FRONT_RIGHT_STEERING_SENSOR_CHANNEL,
        RobotMap.BACK_LEFT_STEERING_SENSOR_CHANNEL,
        RobotMap.BACK_RIGHT_STEERING_SENSOR_CHANNEL
    };
    
    //Steering center array (not constant)
    //Note - These values will be changed by in code calibration so the inital
    //values will only apply until the robot is calibrated for the first time
    private static double[] steeringCenters =
    {
        0, //Front left
        0, //Front right
        0, //Back left
        0 //Back right
    };
    
    //Angle to turn at when rotating in place
    private static double TURN_ANGLE = 0.317;

    //Private constuctor
    private Drive(CANJaguar frontLeftMotor, CANJaguar backLeftMotor, CANJaguar frontRightMotor,
            CANJaguar backRightMotor)
    {
        super(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor);
        
        //Make data object
        data = Preferences.getInstance();
        
        //Make steering array
        steering = new Steering[4];
        
        //Make all steering objects
        for (int i = 0; i < steering.length; i++)
        {
            //Get all steering values from saved robot data(Format = (<data key>, <backup value>))
            steeringCenters[i] = data.getDouble(RobotMap.STEERING_KEYS[i], steeringCenters[i]);
            
            //Make steering
            steering[i] = new Steering(PIDValues.values[i][0],PIDValues.values[i][1], PIDValues.values[i][2], 
                     STEERING_MOTOR_CHANNELS[i], STEERING_SENSOR_CHANNELS[i], steeringCenters[i]);
        }
        
        //TODO: Set inverted motors 
        //setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        
        gyro = Gyro467.getInstance();
        gyro.reset();
    }

    /**
     * Gets the single instance of this class.
     * @return The single instance.
     */
    public static Drive getInstance()
    {
        if (instance == null)
        {
            try
            {
                instance = new Drive(new CANJaguar(RobotMap.FRONT_LEFT_MOTOR_CHANNEL),
                                         new CANJaguar(RobotMap.BACK_LEFT_MOTOR_CHANNEL),
                                         new CANJaguar(RobotMap.FRONT_RIGHT_MOTOR_CHANNEL),
                                         new CANJaguar(RobotMap.BACK_RIGHT_MOTOR_CHANNEL));
            }
            catch (CANTimeoutException ex)
            {
                // TODO - print an error to the console here and retry
                ex.printStackTrace();
            }
        }
        return instance;
    }

    
    public void turnDrive(double speed)
    {
        //Set angles in "turn in place" position
        //Wrap around will check whether the closest angle is facing forward or backward
        //  
        //  Front Left- / \ - Front Right
        //
        //  Back Left - \ / - Back Right
        //  
        if (wrapAroundDifference(TURN_ANGLE, steering[RobotMap.FRONT_LEFT].getSteeringAngle()) <= 0.5)
        {
            //Front facing angles
            steering[RobotMap.FRONT_LEFT].setAngle(TURN_ANGLE);
            steering[RobotMap.FRONT_RIGHT].setAngle(-TURN_ANGLE);
            steering[RobotMap.BACK_LEFT].setAngle(-TURN_ANGLE);
            steering[RobotMap.BACK_RIGHT].setAngle(TURN_ANGLE);          
        }
        else
        {
            //Rear facing angles
            steering[RobotMap.FRONT_LEFT].setAngle(TURN_ANGLE - 1);
            steering[RobotMap.FRONT_RIGHT].setAngle(-TURN_ANGLE + 1);
            steering[RobotMap.BACK_LEFT].setAngle(-TURN_ANGLE + 1);
            steering[RobotMap.BACK_RIGHT].setAngle(TURN_ANGLE - 1);
            
            //Reverse direction
            speed = -speed;
        }
        
        //Drive motors with left side motors inverted
        this.drive(speed, 0, new boolean[] {true, false, true, false});
    }

    double lastSpeed;

    /**
     * Called to log interesting data from the drive system to the Smart Dashboard
     * More specifically used to calibrate wheels
     */
    public void logDrive()
    {
        SmartDashboard.putDouble("GyroAngle", gyro.getAngle());
        SmartDashboard.putDouble("Front Left Sensor", steering[RobotMap.FRONT_LEFT].getSensorValue());
        SmartDashboard.putDouble("Front Right Sensor", steering[RobotMap.FRONT_RIGHT].getSensorValue());
        SmartDashboard.putDouble("Back Left Sensor", steering[RobotMap.BACK_LEFT].getSensorValue());
        SmartDashboard.putDouble("Back Right Sensor", steering[RobotMap.BACK_RIGHT].getSensorValue());
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

        if (wrapAroundDifference(steering[RobotMap.FRONT_LEFT].getSteeringAngle(), steeringAngle) > 0.5)
        {
            speed = -speed;
            steeringAngle = steeringAngle - 1.0;
            if (steeringAngle < -1.0)
            {
                steeringAngle += 2.0;
            }
        }

        //Set steering angles
        for (int i = 0; i < steering.length; i++)
        {
            steering[i].setAngle(steeringAngle);
        }
        
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
        this.drive(speed, orientation - gyroAngle, null);
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
     * @param inverts Array of which motors to invert in form {fl, fr, bl, br}
     */
    public void drive(double speed, double angleCorrection, boolean[] inverts)
    {
        //If any of the motors doesn't exist then exit
        if (m_rearLeftMotor == null || m_rearRightMotor == null ||
              m_frontLeftMotor == null || m_rearLeftMotor == null  )
        {
            throw new NullPointerException("Null motor provided");
        }

        //Magic number copied from WPI code
        byte syncGroup = (byte)0x80;

        //Correct speed to each motor to allow for motor wiring
        //and orientation
        double frontLeftSpeed = speed * -1.0;  
        double frontRightSpeed = speed * -1.0;
        double rearLeftSpeed = speed * 1.0;
        double rearRightSpeed = speed * -1.0;
        
        //If the inverts parameter is fed in, invert the specified motors
        if (inverts != null)
        {
            frontLeftSpeed *= inverts[0] ? -1 : 1;
            frontRightSpeed *= inverts[1] ? -1 : 1;
            rearLeftSpeed *= inverts[2] ? -1 : 1;
            rearRightSpeed *= inverts[3] ? -1 : 1;
        }

        //Read current wheel orientation. This is used to determine
        //how much compensation to apply to each wheel
        double wheelAngle = steering[RobotMap.FRONT_LEFT].getSteeringAngle();


        //Based on the orientation of the wheels relative to the robot,
        //determine how much weighting to apply to the left vs the front drive system.
        //Note:  right is negative left and rear is negative front so don't need
        //to be calculated separately
        
        double leftFactor = Math.cos(wheelAngle * Math.PI);
        double frontFactor = Math.sin(wheelAngle * Math.PI);

        double frontLeftFactor = limit(leftFactor + frontFactor) * angleCorrection * SPEED_CORRECTION;
        double frontRightFactor = limit(-leftFactor + frontFactor) * angleCorrection * SPEED_CORRECTION;
        double rearLeftFactor = limit(leftFactor - frontFactor) * angleCorrection * SPEED_CORRECTION;
        double rearRightFactor = limit(-leftFactor - frontFactor) * angleCorrection * SPEED_CORRECTION;

        //Limit individual wheel speed variation to 20% maximim
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
            //Forward direction, add compensation to speed
            frontLeftSpeed *= 1.0 + frontLeftFactor;
            frontRightSpeed *= 1.0 + frontRightFactor;
            rearLeftSpeed *= 1.0 + rearLeftFactor;
            rearRightSpeed *= 1.0 + rearRightFactor;
        }
        else
        {
            //Reverse direction, subtract compensation from speed
            frontLeftSpeed *= 1.0 - frontLeftFactor;
            frontRightSpeed *= 1.0 - frontRightFactor;
            rearLeftSpeed *= 1.0 - rearLeftFactor;
            rearRightSpeed *= 1.0 - rearRightFactor;
        }

        //Determine the fastest wheel speed. Use absolute value to accomodate motors that
        //may be wired to turn in different directions
        double maxSpeed = Math.max(
                Math.max(Math.abs(frontLeftSpeed), Math.abs(frontRightSpeed)),
                Math.max(Math.abs(rearLeftSpeed), Math.abs(rearRightSpeed))
                );
        
        //One of the motors requires a speed greater than 1.
        //Scale others down to compensate
        if (maxSpeed > 1.0)
        {
            double speedRatio = 1.0 / maxSpeed;

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
    
    /**
     * Set the steering center to a new value
     * @param steeringMotor The id of the steering motor (0 = FL, 1 = FR, 2 = BL, 3 = BR
     * @param value The new center value
     */
    public void setSteeringCenter(int steeringMotor, double value)
    {
        steeringCenters[steeringMotor] = value;
        steering[steeringMotor].setCenter(value);
    }
    
    /**
     * Get the steering angle of the corresponding steering motor
     * @param steeringId The id of the steering motor
     * @return 
     */
    public double getSteeringAngle(int steeringId)
    {
        return steering[steeringId].getSensorValue();
    }

}
