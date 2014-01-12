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

    //Correct speed to each motor to allow for motor wiring
    //and orientation ... true multiplies by -1, false multiplies by 1
    private static boolean frontLeftInvertWiringCompensation = true;
    private static boolean frontRightInvertWiringCompensation = false;
    private static boolean backLeftInvertWiringCompensation = true;
    private static boolean backRightInvertWiringCompensation = false;

    //Jaguar Objects
    private Jaguar frontRightMotor;
    private Jaguar frontLeftMotor;
    private Jaguar backRightMotor;
    private Jaguar backLeftMotor;
    //Steering Objects
    private Steering frontRightSteering;
    private Steering frontLeftSteering;
    private Steering backRightSteering;
    private Steering backLeftSteering;

    //Data storage object, used to get the steering centers from the robot
    private DataStorage data;

    //Driverstation object (for sake of printing debugs)
    private static Driverstation driverstation;

    //needed so that if values are not found for steering center
    private static final double STANDIN_STEERING_CENTER_VALUE = 480.0;

    //New turn angle constants for 2013 robot
    private static double FRONT_TURN_ANGLE = 0.0754;
    private static double BACK_TURN_ANGLE = -0.356;

    private static double BACK_LEFT_STEERING_CENTER;
    private static double BACK_RIGHT_STEERING_CENTER;
    private static double FRONT_LEFT_STEERING_CENTER;
    private static double FRONT_RIGHT_STEERING_CENTER;

    //Private constuctor
    private Drive(Jaguar frontLeftMotor, Jaguar backLeftMotor, Jaguar frontRightMotor, Jaguar backRightMotor)
    {        
        //required to pass into WPI's RobotDrive object
        super(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor);
        
        this.frontLeftMotor = frontLeftMotor;
        this.frontRightMotor = frontRightMotor;
        this.backLeftMotor = backLeftMotor;
        this.backRightMotor = backRightMotor;
        
        //Make objects
        data = DataStorage.getInstance();
        driverstation = Driverstation.getInstance();

        //setup steering centers
        //Get all steering values from saved robotata(Format = (<data key>, <backup value>))
        BACK_LEFT_STEERING_CENTER = data.getDouble(RobotMap.BACK_LEFT_STEERING_KEY, STANDIN_STEERING_CENTER_VALUE);
        BACK_RIGHT_STEERING_CENTER = data.getDouble(RobotMap.BACK_RIGHT_STEERING_KEY, STANDIN_STEERING_CENTER_VALUE);
        FRONT_LEFT_STEERING_CENTER = data.getDouble(RobotMap.FRONT_LEFT_STEERING_KEY, STANDIN_STEERING_CENTER_VALUE);
        FRONT_RIGHT_STEERING_CENTER = data.getDouble(RobotMap.FRONT_RIGHT_STEERING_KEY, STANDIN_STEERING_CENTER_VALUE);

        //make the new steering object
        backLeftSteering = new Steering(
                RobotMap.BACK_LEFT_STEERING_PID_P,
                RobotMap.BACK_LEFT_STEERING_PID_I,
                RobotMap.BACK_LEFT_STEERING_PID_D,
                RobotMap.BACK_LEFT_STEERING_MOTOR_CHANNEL,
                RobotMap.BACK_LEFT_STEERING_SENSOR_CHANNEL,
                BACK_LEFT_STEERING_CENTER,
                RobotMap.BACK_LEFT_SENSOR_RANGE);
        backRightSteering = new Steering(
                RobotMap.BACK_RIGHT_STEERING_PID_P,
                RobotMap.BACK_RIGHT_STEERING_PID_I,
                RobotMap.BACK_RIGHT_STEERING_PID_D,
                RobotMap.BACK_RIGHT_STEERING_MOTOR_CHANNEL,
                RobotMap.BACK_RIGHT_STEERING_SENSOR_CHANNEL,
                BACK_RIGHT_STEERING_CENTER,
                RobotMap.BACK_RIGHT_SENSOR_RANGE);
        frontLeftSteering = new Steering(
                RobotMap.FRONT_LEFT_STEERING_PID_P,
                RobotMap.FRONT_LEFT_STEERING_PID_I,
                RobotMap.FRONT_LEFT_STEERING_PID_D,
                RobotMap.FRONT_LEFT_STEERING_MOTOR_CHANNEL,
                RobotMap.FRONT_LEFT_STEERING_SENSOR_CHANNEL,
                FRONT_LEFT_STEERING_CENTER,
                RobotMap.FRONT_LEFT_SENSOR_RANGE);
        frontRightSteering = new Steering(
                RobotMap.FRONT_RIGHT_STEERING_PID_P,
                RobotMap.FRONT_RIGHT_STEERING_PID_I,
                RobotMap.FRONT_RIGHT_STEERING_PID_D,
                RobotMap.FRONT_RIGHT_STEERING_MOTOR_CHANNEL,
                RobotMap.FRONT_RIGHT_STEERING_SENSOR_CHANNEL,
                FRONT_RIGHT_STEERING_CENTER,
                RobotMap.FRONT_RIGHT_SENSOR_RANGE);

    }

    /**
     * Gets the single instance of this class.
     *
     * @return The single instance.
     */
    public static Drive getInstance()
    {
        if (instance == null)
        {
            //creates the four jaguars
            Jaguar frontleft = new Jaguar(RobotMap.FRONT_LEFT_MOTOR_CHANNEL);
            Jaguar backleft = new Jaguar(RobotMap.BACK_LEFT_MOTOR_CHANNEL);
            Jaguar frontright = new Jaguar(RobotMap.FRONT_RIGHT_MOTOR_CHANNEL);
            Jaguar backright = new Jaguar(RobotMap.BACK_RIGHT_MOTOR_CHANNEL);
            //passes the jags into the drive object
            
            instance = new Drive(frontleft, backleft, frontright, backright);
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

        //bases the angle based on front left
        if (wrapAroundDifference(FRONT_TURN_ANGLE, frontLeftSteering.getSteeringAngle()) <= 0.5)
        {
            //Front facing angles
            frontLeftSteering.setAngle(FRONT_TURN_ANGLE);
            frontRightSteering.setAngle(-FRONT_TURN_ANGLE);
            backLeftSteering.setAngle(BACK_TURN_ANGLE);
            backRightSteering.setAngle(-BACK_TURN_ANGLE);
        }
        else
        {
            //Rear facing angles
            frontLeftSteering.setAngle(FRONT_TURN_ANGLE - 1.0);
            frontRightSteering.setAngle(-FRONT_TURN_ANGLE + 1.0);
            backLeftSteering.setAngle(BACK_TURN_ANGLE - 1.0);
            backRightSteering.setAngle(-BACK_TURN_ANGLE + 1.0);

            //Reverse direction
            speed = -speed;
        }

        //Drive motors with left side motors inverted
        this.drive(speed, new boolean[]
        {
            true, false, true, false
        });
    }

    /**
     * Prints sensor angles from steering sensors to lines 3-6 of the
     * driverstation
     */
    public void sendSteerAnglesToDriverstation()
    {
        driverstation.println("Angle FL: " + frontLeftSteering.getSteeringAngle(), 3);
        driverstation.println("Angle FR: " + frontRightSteering.getSteeringAngle(), 4);
        driverstation.println("Angle BL: " + backLeftSteering.getSteeringAngle(), 5);
        driverstation.println("Angle BR: " + backRightSteering.getSteeringAngle(), 6);
    }
    private double lastSpeed = 0.0;

    /**
     * Limit the rate at which the robot can change speed once driving fast.
     * This is to prevent causing mechanical damage - or tipping the robot
     * through stopping too quickly.
     *
     * @param speed desired speed for robot
     * @return returns rate-limited speed
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
     *
     * @param angle value corresponding to the field direction to move in
     * @param speed Speed to drive at
     * @param fieldAlign Whether or not to use field align drive
     */
    public void crabDrive(double steeringAngle, double speed)
    {
        if (wrapAroundDifference(frontLeftSteering.getSteeringAngle(), steeringAngle) > 0.5)
        {
            speed = -speed;
            steeringAngle = steeringAngle - 1.0;
            if (steeringAngle < -1.0)
            {
                steeringAngle += 2.0;
            }
        }

        //Set steering angles
        frontLeftSteering.setAngle(steeringAngle);
        frontRightSteering.setAngle(steeringAngle);
        backLeftSteering.setAngle(steeringAngle);
        backRightSteering.setAngle(steeringAngle);

        this.drive(limitSpeed(speed),new boolean[]
        {
            false, false, false, false
        });
    }

    /**
     * Individually controls a specific driving motor
     *
     * @param speed Speed to drive at
     * @param steeringId Id of driving motor to drive
     */
    public void individualWheelDrive(double speed, int steeringId)
    {
        //Magic number copied from WPI code
        byte syncGroup = (byte) 0x80;

        double frontLeftSpeed = 0.0;
        double frontRightSpeed = 0.0;
        double rearLeftSpeed = 0.0;
        double rearRightSpeed = 0.0;

        switch (steeringId)
        {
            case RobotMap.FRONT_LEFT:
                frontLeftSpeed = speed * 1.0;
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

        if (m_safetyHelper != null)
        {
            m_safetyHelper.feed();
        }
    }

    /**
     * Function to determine the wrapped around difference from the joystick
     * angle to the steering angle.
     *
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

    private long previousSystemTime = 0;

    /**
     * New drive function. Allows for wheel correction using speed based on a
     * specified correction angle
     *
     * @param speed The speed to drive at
     * @param inverts Array of which motors to invert in form {FL, FR, BL, BR}
     */
    public void drive(double speed, boolean[] inverts)
    {
        //If any of the motors doesn't exist then exit
        if (m_rearLeftMotor == null || m_rearRightMotor == null
                || m_frontLeftMotor == null || m_rearLeftMotor == null)
        {
            throw new NullPointerException("Null motor provided");
        }

        //Magic number copied from WPI code
        byte syncGroup = (byte) 0x80;

        double frontLeftSpeed = (frontLeftInvertWiringCompensation) ? speed * -1.0 : speed;
        double frontRightSpeed = (frontRightInvertWiringCompensation) ? speed * -1.0 : speed;
        double backLeftSpeed = (backLeftInvertWiringCompensation) ? speed * -1.0 : speed;
        double backRightSpeed = (backRightInvertWiringCompensation) ? speed * -1.0 : speed;

        //If the inverts parameter is fed in, invert the specified motors
        if (inverts != null)
        {
            frontLeftSpeed *= inverts[0] ? -1.0 : 1.0;
            frontRightSpeed *= inverts[1] ? -1.0 : 1.0;
            backLeftSpeed *= inverts[2] ? -1.0 : 1.0;
            backRightSpeed *= inverts[3] ? -1.0 : 1.0;
        }
        if (m_safetyHelper != null)
        {
            m_safetyHelper.feed();
        }
        frontLeftMotor.set(frontLeftSpeed);
        frontRightMotor.set(frontRightSpeed);
        backLeftMotor.set(backLeftSpeed);
        backRightMotor.set(backRightSpeed);
        
        
    }
}
