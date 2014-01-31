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
    
    private static final int TANGENT_RESOLUTION = 200;

    //Gyro object
    private static Gyro467 gyro;

    //Steering objects
    private Steering[] steering;
    
    //Data storage object
    private DataStorage data;
    
    //Driverstation object (for sake of printing debugs)
    private Driverstation driverstation;
    
    //Angle to turn at when rotating in place
    private static double TURN_ANGLE = 0.183;
    
    // 2 times the longer dimension of the robot divided by the shorter.
    // Based on 2012 robot's geometry. Change for newer robots!
    // (Used in car drive).
    private static final double ROBOT_RATIO = 2.91358;
    
    // Magic number copied from WPI code
    private static final byte SYNC_GROUP = (byte)0x80;
    
    private long currentTime = 0;
    private long previousTime = 0;
    private double gyroStart = 0.0;
    private double turnWhileDriveDist = 0;
    
    private static final double SPIN_RATE = 1;
    
    // Invert the drive motors to allow for wiring.
    private static final boolean FRONT_LEFT_DRIVE_INVERT = true;
    private static final boolean FRONT_RIGHT_DRIVE_INVERT = true;
    private static final boolean BACK_LEFT_DRIVE_INVERT = true;
    private static final boolean BACK_RIGHT_DRIVE_INVERT = true;

    //Private constuctor
    private Drive(Jaguar frontLeftMotor, Jaguar backLeftMotor, 
                  Jaguar frontRightMotor, Jaguar backRightMotor)
    {
        super(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor);
        
        //Make objects
        data = DataStorage.getInstance();
        driverstation = Driverstation.getInstance();
        
        //Make steering array
        steering = new Steering[4];
        
        //Make all steering objects
        for (int i = 0; i < steering.length; i++)
        {
            //Get all steering values from saved robot data(Format = (<data key>, <backup value>))
            double steeringCenter = data.getDouble(RobotMap.STEERING_KEYS[i], 0.0);
            
            //Make steering
            steering[i] = new Steering(RobotMap.PIDvalues[i][0],
                                       RobotMap.PIDvalues[i][1], 
                                       RobotMap.PIDvalues[i][2], 
                                       RobotMap.STEERING_MOTOR_CHANNELS[i], 
                                       RobotMap.STEERING_SENSOR_CHANNELS[i], 
                                       steeringCenter);
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
     * Drives each of the four wheels at different speeds using invert constants
     * to account for wiring.
     * 
     * @param frontLeftSpeed
     * @param frontRightSpeed
     * @param backLeftSpeed
     * @param backRightSpeed 
     */
    public void fourMotorDrive(double frontLeftSpeed, double frontRightSpeed, 
                               double backLeftSpeed, double backRightSpeed) {
        
        //If any of the motors doesn't exist then exit
        if (m_rearLeftMotor == null || m_rearRightMotor == null ||
            m_frontLeftMotor == null || m_rearLeftMotor == null  )
        {
            throw new NullPointerException("Null motor provided");
        }
        
        m_frontLeftMotor.set(((FRONT_LEFT_DRIVE_INVERT) ? -1 : 1)*frontLeftSpeed, SYNC_GROUP);
        m_frontRightMotor.set(((FRONT_RIGHT_DRIVE_INVERT) ? -1 : 1)*frontRightSpeed, SYNC_GROUP);
        m_rearLeftMotor.set(((BACK_LEFT_DRIVE_INVERT) ? -1 : 1)*backLeftSpeed, SYNC_GROUP);
        m_rearRightMotor.set(((BACK_RIGHT_DRIVE_INVERT) ? -1 : 1)*backRightSpeed, SYNC_GROUP);
        
        if (m_safetyHelper != null)
        {
            m_safetyHelper.feed();
        }
    }

    /**
     * Get the Jaguar drive motor object for the specified motor (use RobotMap constants)
     * @param motor The motor to get
     * @return One of the four Jaguar drive motors
     */
    public Jaguar getDriveMotor(int motor)
    {
        Jaguar returnMotor;
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
            default:
                returnMotor = null;
        }
        return returnMotor;
    }
    
    public Steering getSteering(int id)
    {
        return steering[id];
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
        this.drive(limitSpeed(speed), new boolean[] {true, false, true, false});
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
              
        this.drive(limitSpeed(speed), null);
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

        this.drive(limitSpeed(speed), null);
    }
    
    /**
     * An integral approximation of the inverse tangent function, implemented 
     * using Riemann sums. Used in car drive.
     * 
     * @param x the x in arctan(x)
     * @param resolution the accuracy of the approximation.
     *          the higher this is, the better the approximation.
     * @return arctan(x)
     */
    public double arctanIntegral(double x, int resolution) 
    {
        double sum = 0;
        double slope = x / resolution;
        
        for (int n = 0; n <= resolution; n++) {
            double slice = slope * n;
            
            sum += 1 / (1 + slice*slice);
        }
        
        return sum * slope;
    }
    
    /**
     * Yo dog, I heard you like to drive, so I put a car in yo car so you
     * can drive while you drive.
     * 
     * Drives the robot similarly to a car. Essentially works by angling the
     * wheels so they are tangent to a circular path, and driving the wheels
     * at the appropriate speed so they do not drag.
     * 
     * TODO: Make a method that isn't slow as balls.
     * 
     * See RobotMain for controls.
     * 
     * @param turnAngle Angle to turn at, from -1.0 to 1.0. Negative values
     *                   drive left, positive values drive right.
     * @param speed Speed to drive at. Negative values drive backwards.
     */
    public void carDrive(double turnAngle, double speed) 
    {
        // Dampen speed.
        speed = limitSpeed(speed);
        
        // 2pi, for convenience.
        double PI2 = Math.PI*2;
        
        // Dampen the turning angle, ensuring the inner wheels will not turn
        //   more than 45 degrees.
        double dampenedTurningAngle = turnAngle / 2.5;
        
        // Convert turning angle for use with outerTurnAngle algorithm.
        // Note: To convert from robot angles to radians, multiply
        //   by 2pi.
        double absTurnAngle = Math.abs(dampenedTurningAngle);
        double turnAngleRadians = absTurnAngle*PI2;
        
        // The direction of the turn.
        //   If left, direction = -1
        //   If right, direction = 1
        //   If straight forward or backward, direction = 0
        int direction = (int)(dampenedTurningAngle / absTurnAngle);
        
        // Calculate wheel angles such that perpendicular lines drawn from the wheels
        //   will all intersect at the same point. This ensures that all the wheels will
        //   follow a circular path.
        double innerTurnAngle = absTurnAngle / 2;
        double outerTurnAngle = (Math.PI/2 - arctanIntegral(Math.tan((Math.PI - turnAngleRadians)/2) + ROBOT_RATIO, TANGENT_RESOLUTION)) / PI2;
        
        // Conditional operators for left and right angles, addressing the direction
        //   of the turn.
        // If turning right, that is, (direction = 1.0), the right wheels will be the 
        //   inner wheels, and the left wheels will be the outer wheels. However, 
        //   for left turns this is reversed.
        double rightAngleConditional = ((direction > 0) ? innerTurnAngle : outerTurnAngle);
        double leftAngleConditional = ((direction > 0) ? outerTurnAngle : innerTurnAngle);
        
        // Set angles. Note that the back wheels turn in the opposite direction, hence
        //   the inverts. Multiplying by the direction corrects for left and right turning,
        //   and ensures that if driving straight, all turning angles will absolutely be zero.
        steering[RobotMap.FRONT_RIGHT].setAngle(rightAngleConditional*direction);
        steering[RobotMap.FRONT_LEFT].setAngle(leftAngleConditional*direction);
        steering[RobotMap.BACK_RIGHT].setAngle(-rightAngleConditional*direction);
        steering[RobotMap.BACK_LEFT].setAngle(-leftAngleConditional*direction);
        
        // Calculate the speed for the inner wheels to drive at. This is guaranteed to be smaller
        //   than the speed of the outer wheels.
        double sinRt2 = ROBOT_RATIO * Math.sin(turnAngleRadians);
        double innerSpeed = speed / Math.sqrt(sinRt2*sinRt2 + ROBOT_RATIO*Math.sin(2*turnAngleRadians) + 1);
        
        // Conditional operators for left and right turns.
        double leftSpeedConditional = -((direction > 0) ? speed : innerSpeed);
        double rightSpeedConditional = -((direction > 0) ? innerSpeed : speed);
        
        // Drive the wheels.
        fourMotorDrive(leftSpeedConditional, rightSpeedConditional,
                       leftSpeedConditional, rightSpeedConditional);
    }
    
    public void turnWhileDriveEnable() {
        gyroStart = gyro.getAngle();
        turnWhileDriveDist = 0;
        currentTime = System.currentTimeMillis();
    }
    
    public void turnWhileDrive(double speed) {
        final double frontLeftAngConstant = .99074;
        final double backLeftAngConstant = 2.15085;
        final double frontRightAngConstant = -.99074;
        final double backRightAngConstant = -2.15085;
        
        previousTime = currentTime;
        currentTime = System.currentTimeMillis();
        
        double dt = (double) (currentTime - previousTime);
        
        turnWhileDriveDist += speed * (dt / 1000);
        
        double dist = turnWhileDriveDist;
        
        System.out.println("DIST: " + turnWhileDriveDist);
        
        double frontLeftSpinFactor = calcSpinFactor(frontLeftAngConstant, dist);
        double backLeftSpinFactor = calcSpinFactor(backLeftAngConstant, dist);
        double frontRightSpinFactor = calcSpinFactor(frontRightAngConstant, dist);
        double backRightSpinFactor = calcSpinFactor(backRightAngConstant, dist);
        
        double frontLeftAngle = calcAngle(frontLeftSpinFactor);
        double backLeftAngle = calcAngle(backLeftSpinFactor);
        double frontRightAngle = calcAngle(frontRightSpinFactor);
        double backRightAngle = calcAngle(backRightSpinFactor);
        
        double frontLeftSpeed = calcSpeed(frontLeftSpinFactor);
        double backLeftSpeed = calcSpeed(backLeftSpinFactor);
        double frontRightSpeed = calcSpeed(frontRightSpinFactor);
        double backRightSpeed = calcSpeed(backRightSpinFactor);
        
        steering[RobotMap.FRONT_LEFT].setAngle(frontLeftAngle);
        steering[RobotMap.BACK_LEFT].setAngle(backLeftAngle);
        steering[RobotMap.FRONT_RIGHT].setAngle(frontRightAngle);
        steering[RobotMap.BACK_RIGHT].setAngle(backRightAngle);
        
        fourMotorDrive(frontLeftSpeed, frontRightSpeed, backLeftSpeed, backRightSpeed);
    }
    
    public double calcSpinFactor(double constant, double dist) {
        return SPIN_RATE * dist + constant - 2*Math.PI*gyroStart;
    }
    
    public double calcAngle(double spinFactor) {
        return arctanIntegral(Math.cos(spinFactor) / (1 - Math.sin(spinFactor)), TANGENT_RESOLUTION) / (Math.PI*2);
    }
    
    public double calcSpeed(double spinFactor) {
        return Math.sqrt(2*(1 - Math.sin(spinFactor)));
    }
    
    /**
     * Individually controls a specific driving motor
     * @param speed Speed to drive at
     * @param steeringId Id of driving motor to drive
     */
    public void individualWheelDrive(double speed, int steeringId)
    {
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
        
        fourMotorDrive(frontLeftSpeed, frontRightSpeed, rearLeftSpeed, rearRightSpeed);
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

    /**
     * New drive function. Allows for wheel correction using speed based on
     * a specified correction angle
     * @param speed The speed to drive at
     * @param inverts Array of which motors to invert in form {FL, FR, BL, BR}
     */
    public void drive(double speed, boolean[] inverts)
    {
        double frontLeftSpeed = speed;  
        double frontRightSpeed = speed;
        double rearLeftSpeed = speed;
        double rearRightSpeed = speed;
        
        //If the inverts parameter is fed in, invert the specified motors
        if (inverts != null)
        {
            frontLeftSpeed *= inverts[0] ? -1.0 : 1.0;
            frontRightSpeed *= inverts[1] ? -1.0 : 1.0;
            rearLeftSpeed *= inverts[2] ? -1.0 : 1.0;
            rearRightSpeed *= inverts[3] ? -1.0 : 1.0;
        }

        fourMotorDrive(frontLeftSpeed, frontRightSpeed, rearLeftSpeed, rearRightSpeed);
    }
    
    public void stop() 
    {
        drive(0, null);
    }
    
    /**
     * Set the steering center to a new value
     * @param steeringMotor The id of the steering motor (0 = FL, 1 = FR, 2 = BL, 3 = BR)
     * @param value The new center value
     */
    public void setSteeringCenter(int steeringMotor, double value)
    {
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
