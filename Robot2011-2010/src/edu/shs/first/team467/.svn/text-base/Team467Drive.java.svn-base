package edu.shs.first.team467;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 *
 * @author 
 */
public class Team467Drive extends RobotDrive
{

    private static final int FRONT_RIGHT_MOTOR = 1;
    private static final int FRONT_LEFT_MOTOR = 2;
    private static final int BACK_LEFT_MOTOR = 3;
    private static final int BACK_RIGHT_MOTOR = 4;

    private static final int FRONT_STEERING_MOTOR = 5;
    private static final int FRONT_STEERING_SENSOR = 7;

    private static final double FRONT_STEERING_SENSOR_LEFTLIMIT = 729;
    private static final double FRONT_STEERING_SENSOR_MIDPOINT = 487;
    private static final double FRONT_STEERING_SENSOR_RIGHTLIMIT = 260;

    private static final double FRONT_STEERING_PID_P = -0.028; // -0.015
    private static final double FRONT_STEERING_PID_I = 0.0;
    private static final double FRONT_STEERING_PID_D = 0.0;

    private static final int BACK_STEERING_MOTOR = 6;
    private static final int BACK_STEERING_SENSOR = 2;

    private static final double BACK_STEERING_SENSOR_LEFTLIMIT = 93;
    private static final double BACK_STEERING_SENSOR_MIDPOINT = 799;
    private static final double BACK_STEERING_SENSOR_RIGHTLIMIT = 559;

    private static final double BACK_STEERING_PID_P = -0.015;  // -0.01
    private static final double BACK_STEERING_PID_I = 0.0;
    private static final double BACK_STEERING_PID_D = 0.0;

    private static final int GYRO_CHANNEL = 1;

    private Steering frontSteering;
    private Steering backSteering;

    private Gyro gyro;

    Team467Camera.CamData camData;

     //Circle strafing state constants
    private final int STRAFE_FORWARD_TO_CIRCLE = 0;
    private final int STRAFE_AWAY_FROM_CIRCLE = 1;
    private final int STRAFE_UNTIL_LEFT_CIRCLE = 2;
    private final int STRAFE_TO_CIRCLE = 3;
    private final int STRAFE_STOPPED = 4;

    //The height (in pixels) for the circle to be when the robot drives to it
    private final int DRIVE_TO_HEIGHT = 15;

    //Circle strafing state
    private int strafeState = STRAFE_FORWARD_TO_CIRCLE;

    //Line follow object
    private static LineFollow lineFollow;

    //Camera
    private Team467Camera cam;

    // Singleton instance variable
    private static Team467Drive driveInstance;

    // Singleton so constructor is private
    private Team467Drive()
    {
        super(FRONT_LEFT_MOTOR, BACK_LEFT_MOTOR, FRONT_RIGHT_MOTOR, BACK_RIGHT_MOTOR);
        frontSteering = new Steering(FRONT_STEERING_PID_P, FRONT_STEERING_PID_I,
                FRONT_STEERING_PID_D, FRONT_STEERING_MOTOR, FRONT_STEERING_SENSOR,
                FRONT_STEERING_SENSOR_LEFTLIMIT, FRONT_STEERING_SENSOR_MIDPOINT,
                FRONT_STEERING_SENSOR_RIGHTLIMIT);
        backSteering = new Steering(BACK_STEERING_PID_P, BACK_STEERING_PID_I,
                BACK_STEERING_PID_D, BACK_STEERING_MOTOR, BACK_STEERING_SENSOR,
                BACK_STEERING_SENSOR_LEFTLIMIT, BACK_STEERING_SENSOR_MIDPOINT,
                BACK_STEERING_SENSOR_RIGHTLIMIT);
        setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        gyro = new Gyro(GYRO_CHANNEL);
        gyro.reset();
        cam = Team467Camera.getInstance();
    }

    // return single instance of this class
    public static synchronized Team467Drive GetInstance()
    {
	if (driveInstance == null)
    {
        driveInstance = new Team467Drive();
        lineFollow = LineFollow.getInstance();
	}
	return driveInstance;
    }

    /**
     * Drives with back wheels always straight. Front wheels drive at the
     * specified angle
     * @param angle Angle of front wheels
     * @param speed Drive speed
     */
    public void backStraightDrive(double angle, double speed)
    {
        frontSteering.SetAngle(angle);
        backSteering.SetAngle(0);
        //this.drive(speed, 0);
    }

    public double getGyro()
    {
        return gyro.getAngle();
    }

    public void resetGyro()
    {
        gyro.reset();
    }
    /**
     * Back wheels only field aligned drive. Front wheels drive at the given angle.
     * @param angle The angle of the front wheels.
     * @param speed Drive speed
     */
    public void faLineFollowDrive(double angle, double speed)
    {
        double steeringAngle = angle - (gyro.getAngle()/180.0);

        if (steeringAngle < -1.0)
        {
            steeringAngle = -1.0;
        }
        if (steeringAngle > 1.0)
        {
            steeringAngle = 1.0;
        }
        frontSteering.SetAngle(angle);
        backSteering.SetAngle(-steeringAngle / 2);
        this.drive(speed, 0);
    }

    /**
     * Field aligned drive. Assumes Gyro angle 0 is facing downfield
     * @param angle
     * @param speed
     */
    public void faDrive(double angle, double speed)
    {
        double steeringAngle = angle - (gyro.getAngle()/180.0);

        if (steeringAngle < -1.0)
        {
            steeringAngle = -1.0;
        }
        if (steeringAngle > 1.0)
        {
            steeringAngle = 1.0;
        }
        frontSteering.SetAngle(steeringAngle);
        backSteering.SetAngle(steeringAngle);
        this.drive(speed, 0);
    }

    /**
     * All 4 wheels steer in the same direction.
     * @param steering
     * @param angle
     * @param speed
     */
    public void swerveDrive(double angle, double speed, double orientation)
    {
        double angleCorrection = (gyro.getAngle() - orientation)/(45.0);
        frontSteering.SetAngle(angle);
        backSteering.SetAngle(angle + angleCorrection);
        //System.out.println("Angle: " + angleCorrection);
        //frontSteering.DashboardLog("Front steering");
        //backSteering.DashboardLog("Back Steering");
        SmartDashboard.log(angle, "Set Angle");
        SmartDashboard.log(frontSteering.GetAngle(), "Wheel Angle");
        SmartDashboard.log(angleCorrection, "Correction");
        if (Math.abs(frontSteering.GetAngle() - angle) <= 0.1)
        {
            this.drive(speed, 0);
        }
    }


    public void swerveDrive(double angle, double speed)
    {
        swerveDrive(angle, speed, gyro.getAngle());
    }
    /**
     * Front and Rear wheels steer in opposite directions, allowing for rapid rotations
     * @param steering
     * @param angle
     * @param speed
     */
    public void carDrive(double angle, double speed)
    {
        frontSteering.SetAngle(angle);
        backSteering.SetAngle(-angle);
        SmartDashboard.log(frontSteering.GetSensorValue(),"FrontSteeringSensor");
        SmartDashboard.log(backSteering.GetSensorValue(), "BackSteeringSensor");
        this.drive(speed, 0);
    }

    /**
     * Updates the line follow sequence. Call on a loop to have the robot
     * follow a line.
     * @param speed The speed to follow at
     * @param fa Whether or not to use field aligned following
     */
    public void updateLineFollow(double speed, boolean fa)
    {
        lineFollow.update(speed, fa);
    }

    /**
     * Starts the camera reading and processing of images
     */
    public void startCameraRead()
    {
        cam.startCameraReading();
    }

    /**
     * Stops the camera reading and processing of images
     */
    public void stopCameraRead()
    {
        cam.stopCameraReading();
    }

    /**
     * Resets the strafe to circle state
     */
    public void resetStrafeState()
    {
        strafeState = STRAFE_FORWARD_TO_CIRCLE;
    }

    /**
     * Update the teleop strafe to circle code. Call periodically to use strafe
     * to circle code.
     * @param speed The speed to drive at
     */

    public void updateCircleStrafe()
    {
        camData = cam.getCamData();
        //System.out.println("Particle Visible: " + cam.particleVisible());
        SmartDashboard.log(strafeState, "Strafe State");
        switch (strafeState)
        {
            case STRAFE_FORWARD_TO_CIRCLE:
                //Look for particles greater than 5 pixels in height
                cam.setMimimumTargetHeight(5.0);
                //System.out.println("Strafe State: Strafe Forward");
                if (camData.targetVisible)
                {
                    //System.out.println("Visible: " + camData.closestDistance);
                    swerveDrive(camData.angleToTarget * 4, -0.3, 0);
                    if (camData.targetHeight >= DRIVE_TO_HEIGHT)
                    {
                        strafeState = STRAFE_AWAY_FROM_CIRCLE;
                    }
                }
                else
                {
                    //System.out.println("Not Visible");
                    swerveDrive(0, 0, 0);
                }
                break;
            case STRAFE_AWAY_FROM_CIRCLE:
               //Look for particles greater than 15 pixels in height when closer
               cam.setMimimumTargetHeight(12.0);
               System.out.println("Strafe State: Strafe Away");
                if (camData.targetVisible)
                {
                    swerveDrive(-1, -0.3, 0);
                }
                else
                {
                    strafeState = STRAFE_UNTIL_LEFT_CIRCLE;
                }
                break;
            case STRAFE_UNTIL_LEFT_CIRCLE:
                if (camData.targetVisible && camData.targetXPos < 10.0)
                {
                    strafeState = STRAFE_TO_CIRCLE;
                }
                else
                {
                    swerveDrive(-1, -0.3, 0);
                }
                break;
            case STRAFE_TO_CIRCLE:
                System.out.println("Strafe State: Strafe To");
                if (camData.targetVisible)
                {
                    SmartDashboard.log(camData.targetXPos, "Distance X");
                    if (camData.targetXPos > -5)
                    {
                        swerveDrive(-1, 0.2, 0);
                        strafeState = STRAFE_STOPPED;
                    }
                    else
                    {
                        swerveDrive(-1, -0.2, 0);
                    }
                }
                else
                {
                    swerveDrive(-1, -0.3, 0);
                }
                break;
            case STRAFE_STOPPED:
                System.out.println("Strafe State: Strafe Stopped");
                swerveDrive(0, 0, 0);
                break;
        }
    }

}


