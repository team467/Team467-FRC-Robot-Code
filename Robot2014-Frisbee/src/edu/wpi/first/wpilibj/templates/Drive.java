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
    private static final boolean FRONT_LEFT_INVERT_WIRING_COMPENSATION = true;
    private static final boolean FRONT_RIGHT_INVERT_WIRING_COMPENSATION = false;
    private static final boolean BACK_LEFT_INVERT_WIRING_COMPENSATION = true;
    private static final boolean BACK_RIGHT_INVERT_WIRING_COMPENSATION = false;

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
        //backup val needed incase value cannot be read
        double blcenter = data.getDouble(RobotMap.BACK_LEFT_STEERING_KEY, STANDIN_STEERING_CENTER_VALUE);
        double brcenter = data.getDouble(RobotMap.BACK_RIGHT_STEERING_KEY, STANDIN_STEERING_CENTER_VALUE);
        double flcenter = data.getDouble(RobotMap.FRONT_LEFT_STEERING_KEY, STANDIN_STEERING_CENTER_VALUE);
        double frcenter = data.getDouble(RobotMap.FRONT_RIGHT_STEERING_KEY, STANDIN_STEERING_CENTER_VALUE);

        //make the new steering objects, using (p,i,d,steeringMotor,sensor,centerValue,sensorRange,flip)
        backLeftSteering = new Steering(
            RobotMap.BACK_LEFT_STEERING_PID_P,
            RobotMap.BACK_LEFT_STEERING_PID_I,
            RobotMap.BACK_LEFT_STEERING_PID_D,
            RobotMap.BACK_LEFT_STEERING_MOTOR_CHANNEL,
            RobotMap.BACK_LEFT_STEERING_SENSOR_CHANNEL,
            blcenter);
        backRightSteering = new Steering(
            RobotMap.BACK_RIGHT_STEERING_PID_P,
            RobotMap.BACK_RIGHT_STEERING_PID_I,
            RobotMap.BACK_RIGHT_STEERING_PID_D,
            RobotMap.BACK_RIGHT_STEERING_MOTOR_CHANNEL,
            RobotMap.BACK_RIGHT_STEERING_SENSOR_CHANNEL,
            brcenter);
        frontLeftSteering = new Steering(
            RobotMap.FRONT_LEFT_STEERING_PID_P,
            RobotMap.FRONT_LEFT_STEERING_PID_I,
            RobotMap.FRONT_LEFT_STEERING_PID_D,
            RobotMap.FRONT_LEFT_STEERING_MOTOR_CHANNEL,
            RobotMap.FRONT_LEFT_STEERING_SENSOR_CHANNEL,
            flcenter);
        frontRightSteering = new Steering(
            RobotMap.FRONT_RIGHT_STEERING_PID_P,
            RobotMap.FRONT_RIGHT_STEERING_PID_I,
            RobotMap.FRONT_RIGHT_STEERING_PID_D,
            RobotMap.FRONT_RIGHT_STEERING_MOTOR_CHANNEL,
            RobotMap.FRONT_RIGHT_STEERING_SENSOR_CHANNEL,
            frcenter);
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

    /**
     * Turn in place function. Positive speed turns counterclockwise, negative
     * speed turns clockwise.
     *
     * @param speed
     */
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
        this.drive(speed, false, true, false, true);
    }

    /**
     * Standard swerve drive. 0.0 angle is straight ahead.
     *
     * @param steeringAngle value between -1.0 and 1.0 corresponding to the
     * angle to drive at relative to center (0.0)
     * @param speed Speed to drive at
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

        //used to ensure the speed delta from last time is not greater than a certan amount
        double limitedDriveSpeedDelta = limitSpeedDelta(speed);

        if (Math.abs(limitedDriveSpeedDelta) != 0.0) //if greater than zero, is set to zero by RobotMain if below deadzone
        {
            //Set steering angles
            frontLeftSteering.setAngle(steeringAngle);
            frontRightSteering.setAngle(steeringAngle);
            backLeftSteering.setAngle(steeringAngle);
            backRightSteering.setAngle(steeringAngle);

            this.drive(limitedDriveSpeedDelta, false, false, false, false);
        }
        else // dont move the robot, center the wheels and stop
        {
            frontLeftSteering.setAngle(0.0);
            frontRightSteering.setAngle(0.0);
            backLeftSteering.setAngle(0.0);
            backRightSteering.setAngle(0.0);
            this.drive(0.0, false, false, false, false);
        }
    }

    /**
     * Drives the robot using the gyro to align it to the field
     * @param desiredAngle
     * @param speed 
     */
    public void fieldAlignedDrive(double desiredAngle, double speed)
    {
        
    }
    
    /**
     * Attempt at car drive operation. Ultimately deemed a failure after hitting 
     * myself with the robot, causing me to drop my bagel
     * @param steeringAngle angle to drive the front wheels
     * @param speed speed to drive the front wheels
     */
    public void carDrive(double steeringAngle, double speed)
    {
        if (speed != 0)
        {
            //used to ensure the speed delta from last time is not greater than a certan amount
            double limitedDriveSpeedDelta = limitSpeedDelta(speed);

            if (steeringAngle < -0.25)
            {
                steeringAngle = -0.25;
            }
            if (steeringAngle > 0.25)
            {
                steeringAngle = 0.25;
            }

            double backLeftSteeringPower = 0;
            double backRightSteeringPower = 0;

            //left turn
            if (steeringAngle < 0)
            {
                backLeftSteeringPower = Math.cos(Math.abs(steeringAngle) * 4 * Math.PI) * speed;
                backRightSteeringPower = Math.sin(Math.abs(steeringAngle) * 4 * Math.PI) + speed;
                if (backRightSteeringPower > 1)
                {
                    backRightSteeringPower = 1;
                }
            }
            //right turn
            else if (steeringAngle > 0)
            {
                backRightSteeringPower = Math.cos(Math.abs(steeringAngle) * 4 * Math.PI) * speed;
                backLeftSteeringPower = Math.sin(Math.abs(steeringAngle) * 4 * Math.PI) + speed;
                if (backLeftSteeringPower > 1.0)
                {
                    backRightSteeringPower = 1.0;
                }
            }
            //straight
            else
            {
                backRightSteeringPower = speed;
                backLeftSteeringPower = speed;
            }

            //turns wheels
            frontLeftSteering.setAngle(steeringAngle);
            frontRightSteering.setAngle(steeringAngle);
            //locks back wheels straight
            backLeftSteering.setAngle(0.0);
            backRightSteering.setAngle(0.0);
//            drive(limitedDriveSpeedDelta, false, false, false, false);
            System.out.println("would drive " + limitedDriveSpeedDelta);
            limitedDriveSpeedDelta = 0;
            drive(limitedDriveSpeedDelta, limitedDriveSpeedDelta, backRightSteeringPower, backLeftSteeringPower,
                  false, false, false, false);
        }
        else // dont move the robot, center the wheels and stop
        {
            frontLeftSteering.setAngle(0.0);
            frontRightSteering.setAngle(0.0);
            backLeftSteering.setAngle(0.0);
            backRightSteering.setAngle(0.0);
            this.drive(0.0, false, false, false, false);
        }
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
     * Ensures the delta in speed is not greater than 0.2 over 0.6 speed
     *
     * @param speed desired speed for robot
     * @return returns rate-limited speed
     */
    private double limitSpeedDelta(double speed)
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
     * Standard drive function. Allows for wheel direction correction by
     * flipping the direction to drive.
     *
     * <b>true</b> flips the direction, <b>false</b> does not
     *
     * @param speed - speed to travel
     * @param frontRightInvert - Front right invert
     * @param frontLeftInvert - Front left invert
     * @param backRightInvert - Back right invert
     * @param backLeftInvert - Back left invert
     */
    public void drive(double speed, boolean frontRightInvert, boolean frontLeftInvert, boolean backRightInvert, boolean backLeftInvert)
    {
        drive(speed, speed, speed, speed, frontRightInvert, frontLeftInvert, backRightInvert, backLeftInvert);
    }

    /**
     *
     * Standard drive function. Allows for wheel direction correction by
     * flipping the direction to drive. Also allows for different powers for
     * each wheel.
     *
     * <b>true</b> flips the direction, <b>false</b> does not
     *
     *
     * @param frspeed front right wheel speed
     * @param flspeed front left wheel speed
     * @param brspeed back right wheel speed
     * @param blspeed back left wheel speed
     * @param frontRightInvert - Front right invert
     * @param frontLeftInvert - Front left invert
     * @param backRightInvert - Back right invert
     * @param backLeftInvert - Back left invert
     */
    public void drive(double frspeed, double flspeed, double brspeed, double blspeed, 
                      boolean frontRightInvert, boolean frontLeftInvert, boolean backRightInvert, boolean backLeftInvert)
    {
        //If any of the motors doesn't exist then exit
        if (m_rearLeftMotor == null || m_rearRightMotor == null ||
            m_frontLeftMotor == null || m_rearLeftMotor == null  )
        {
            throw new NullPointerException("Null motor provided");
        }
        
        //Magic number copied from WPI code
        byte syncGroup = (byte) 0x80;

        double frontLeftSpeed = (FRONT_LEFT_INVERT_WIRING_COMPENSATION) ? flspeed * -1.0 : flspeed;
        double frontRightSpeed = (FRONT_RIGHT_INVERT_WIRING_COMPENSATION) ? frspeed * -1.0 : frspeed;
        double backLeftSpeed = (BACK_LEFT_INVERT_WIRING_COMPENSATION) ? blspeed * -1.0 : blspeed;
        double backRightSpeed = (BACK_RIGHT_INVERT_WIRING_COMPENSATION) ? brspeed * -1.0 : brspeed;

        //If the inverts parameter is fed in, invert the specified motors
        frontLeftSpeed *= frontLeftInvert ? -1.0 : 1.0;
        frontRightSpeed *= frontRightInvert ? -1.0 : 1.0;
        backLeftSpeed *= backLeftInvert ? -1.0 : 1.0;
        backRightSpeed *= backRightInvert ? -1.0 : 1.0;

        //drives all motors at the proper speed
        frontLeftMotor.set(frontLeftSpeed);
        frontRightMotor.set(frontRightSpeed);
        backLeftMotor.set(backLeftSpeed);
        backRightMotor.set(backRightSpeed);
        
        m_frontLeftMotor.set(frontLeftSpeed, syncGroup);
        m_rearLeftMotor.set(backLeftSpeed, syncGroup);
        m_frontRightMotor.set(frontRightSpeed, syncGroup);
        m_rearRightMotor.set(backRightSpeed, syncGroup);
        
        if (m_safetyHelper != null) { m_safetyHelper.feed(); }

    }
    
    public void allDrive(double speed) {
        drive(speed, speed, speed, speed, false, false, false, false);
    }
    
    public Steering getSteering(int id) {
        switch(id) {
            case RobotMap.FRONT_RIGHT:
                return frontRightSteering;
            case RobotMap.FRONT_LEFT:
                return frontLeftSteering;
            case RobotMap.BACK_RIGHT:
                return backRightSteering;
            case RobotMap.BACK_LEFT:
                return backLeftSteering;
            default:
                return null;
        }
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
        
        getSteering(steeringId).setAngle(angle);

        this.allDrive(limitSpeedDelta(speed));
    }
}
