/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

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
    private Memory data;
    
    //Driverstation object (for sake of printing debugs)
    private Driverstation driverstation;
    
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
    //the actual values to be used will be read from the crio
    private static double[] steeringCenters =
    {
        0.0, //Front left
        0.0, //Front right
        0.0, //Back left
        0.0 //Back right
    };
    
    //Angle to turn at when rotating in place
    private static double TURN_ANGLE = 0.183;

    //Private constuctor
    private Drive(Jaguar frontLeftMotor, Jaguar backLeftMotor, 
                  Jaguar frontRightMotor, Jaguar backRightMotor)
    {
        super(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor);
        
        //Make objects
        data = Memory.getInstance();
        driverstation = Driverstation.getInstance();
        
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
                Jaguar frontleft = new Jaguar(RobotMap.FRONT_LEFT_MOTOR_CHANNEL);
                Jaguar backleft = new Jaguar(RobotMap.BACK_LEFT_MOTOR_CHANNEL);
                Jaguar frontright = new Jaguar(RobotMap.FRONT_RIGHT_MOTOR_CHANNEL);
                Jaguar backright = new Jaguar(RobotMap.BACK_RIGHT_MOTOR_CHANNEL);
                instance = new Drive(frontleft, backleft, frontright, backright);
            
        }
        return instance;
    }

    /**
     * Get the Jaguar drive motor object for the specified motor (use RobotMap constants)
     * @param motor The motor to get
     * @return One of the four Jaguar drive motors
     */
    public Jaguar getDriveMotor(int motor)
    {
        Jaguar returnMotor = null;
        switch (motor)
        {
            case RobotMap.FRONT_LEFT:
                returnMotor = (Jaguar) m_frontLeftMotor;
                break;
            case RobotMap.FRONT_RIGHT:
                returnMotor = (Jaguar) m_frontRightMotor;
                break;
            case RobotMap.BACK_LEFT:
                returnMotor = (Jaguar) m_rearLeftMotor;
                break;
            case RobotMap.BACK_RIGHT:
                returnMotor = (Jaguar) m_rearRightMotor;
                break;
        }
        return returnMotor;
    }
    
    public Steering getSteering(int id)
    {
        return steering[id];
    }
    
    /**
     * Get the Jaguar steering motor object for the specified motor (use RobotMap constants)
     * @param motor The motor to get
     * @return One of the four Jaguar steering motors
     */
    public Talon getSteeringMotor(int motor)
    {
        return steering[motor].getMotor();
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
            steering[RobotMap.FRONT_LEFT].setAngle(TURN_ANGLE - 1.0);
            steering[RobotMap.FRONT_RIGHT].setAngle(-TURN_ANGLE + 1.0);
            steering[RobotMap.BACK_LEFT].setAngle(-TURN_ANGLE + 1.0);
            steering[RobotMap.BACK_RIGHT].setAngle(TURN_ANGLE - 1.0);
            
            //Reverse direction
            speed = -speed;
        }
        
        //Drive motors with left side motors inverted
        this.drive(speed, 0, new boolean[] {true, false, true, false});
    }

    /**
     * Prints sensor angles from steering sensors to lines 3-6 of the driverstation
     */
    public void logDrive()
    {
        driverstation.println("Angle FL: " + getSteeringAngle(RobotMap.FRONT_LEFT), 3);
        driverstation.println("Angle FR: " + getSteeringAngle(RobotMap.FRONT_RIGHT), 4);
        driverstation.println("Angle BL: " + getSteeringAngle(RobotMap.BACK_LEFT), 5);
        driverstation.println("Angle BR: " + getSteeringAngle(RobotMap.BACK_RIGHT), 6);
    }
    
    private double lastSpeed = 0.0; 
    /**
     * Limit the rate at which the robot can change speed once driving fast. This is to prevent
     * causing mechanical damage - or tipping the robot through stopping too quickly.
     * @param   speed desired speed for robot
     * @return  returns rate-limited speed
     */
    private double limitSpeed(double speed)
    {
        // Limit the rate at which robot can change speed once driving over 0.6
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
        lastSpeed = speed;
        return (speed);
    }
       
    /**
     * Field aligned drive. Assumes Gyro angle 0 is facing downfield
     * @param angle value corresponding to the field direction to move in
     * @param speed Speed to drive at
     * @param fieldAlign Whether or not to use field align drive
     */
    public void crabDrive(double angle, double speed, boolean fieldAlign)
    {
        double gyroAngle = gyro.getAngle();
        
        //Calculate the wheel angle necessary to drive in the required direction.
        double steeringAngle = (fieldAlign) ? angle - gyroAngle : angle;

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
              
        this.drive(limitSpeed(speed), 0 - gyroAngle, null);
    }
    
    /**
     * Individually controls a specific steering motor
     * @param angle Angle to drive to
     * @param speed Speed to drive at
     * @param steeringId Id of steering motor to drive
     */
    public void individualSteeringDrive(double angle, double speed, int steeringId)
    {
        //Set steering angle
        steering[steeringId].setAngle(angle);

        this.drive(limitSpeed(speed), 0, null);
    }
    
    /**
     * Individually controls a specific driving motor
     * @param speed Speed to drive at
     * @param steeringId Id of driving motor to drive
     */
    public void individualWheelDrive(double speed, int steeringId)
    {
        //Magic number copied from WPI code
        byte syncGroup = (byte)0x80;
        
        double frontLeftSpeed = 0.0;
        double frontRightSpeed = 0.0;
        double rearLeftSpeed = 0.0;
        double rearRightSpeed = 0.0;
        
        switch(steeringId)
        {
            case RobotMap.FRONT_LEFT:
                frontLeftSpeed = speed * -1.0;
                break;
            case RobotMap.FRONT_RIGHT:
                frontRightSpeed = speed * 1.0;
                break;
            case RobotMap.BACK_LEFT:
                rearLeftSpeed = speed * -1.0;
                break;
            case RobotMap.BACK_RIGHT:
                rearRightSpeed = speed * 1.0;
                break;
        }
        
        m_frontLeftMotor.set(frontLeftSpeed, syncGroup);
        m_frontRightMotor.set(frontRightSpeed, syncGroup);
        m_rearLeftMotor.set(rearLeftSpeed, syncGroup);
        m_rearRightMotor.set(rearRightSpeed, syncGroup);
//        
//        if (m_isCANInitialized)
//        {
//            try
//            {
//                Jaguar.updateSyncGroup(syncGroup);
//            }
//            catch (CANNotInitializedException e)
//            {
//                m_isCANInitialized = false;
//            }
//            catch (CANTimeoutException e)
//            {
//            }
//        }
        if (m_safetyHelper != null)
        {
            m_safetyHelper.feed();
        }
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
     * @param inverts Array of which motors to invert in form {FL, FR, BL, BR}
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
        
        
        //!inverts!
        //Correct speed to each motor to allow for motor wiring
        //and orientation
        double frontLeftSpeed = speed * -1.0;  
        double frontRightSpeed = speed * -1.0;
        double rearLeftSpeed = speed * -1.0;
        double rearRightSpeed = speed * 1.0;
        
        //If the inverts parameter is fed in, invert the specified motors
        if (inverts != null)
        {
            frontLeftSpeed *= inverts[0] ? -1.0 : 1.0;
            frontRightSpeed *= inverts[1] ? -1.0 : 1.0;
            rearLeftSpeed *= inverts[2] ? -1.0 : 1.0;
            rearRightSpeed *= inverts[3] ? -1.0 : 1.0;
        }

        m_frontLeftMotor.set(frontLeftSpeed, syncGroup);
        m_rearLeftMotor.set(rearLeftSpeed, syncGroup);
        m_frontRightMotor.set(frontRightSpeed, syncGroup);
        m_rearRightMotor.set(rearRightSpeed, syncGroup);
        
        m_frontLeftMotor.set(Calibration.adjustWheelPower(frontLeftSpeed, RobotMap.FRONT_LEFT), syncGroup);
        m_rearLeftMotor.set(Calibration.adjustWheelPower(rearLeftSpeed, RobotMap.BACK_LEFT), syncGroup);
        m_frontRightMotor.set(Calibration.adjustWheelPower(frontRightSpeed, RobotMap.FRONT_RIGHT), syncGroup);
        m_rearRightMotor.set(Calibration.adjustWheelPower(rearRightSpeed, RobotMap.BACK_RIGHT), syncGroup);

        if (m_safetyHelper != null) { m_safetyHelper.feed(); }
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
    
    /**
     * Get the normalized steering angle of the corresponding steering motor
     * @param steeringId The id of the steering motor
     * @return 
     */
    public double getNormalizedSteeringAngle(int steeringId)
    {
        return steering[steeringId].getSteeringAngle();
    }

}
