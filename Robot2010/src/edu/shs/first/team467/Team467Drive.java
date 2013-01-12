package edu.shs.first.team467;

import edu.wpi.first.wpilibj.*;

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
    private static final int FRONT_STEERING_SENSOR = 1;

    private static final double FRONT_STEERING_SENSOR_LEFTLIMIT = 714;
    private static final double FRONT_STEERING_SENSOR_MIDPOINT = 486;
    private static final double FRONT_STEERING_SENSOR_RIGHTLIMIT = 260;

    private static final double FRONT_STEERING_PID_P = -0.015;
    private static final double FRONT_STEERING_PID_I = 0.0;
    private static final double FRONT_STEERING_PID_D = 0.0;

    private static final int BACK_STEERING_MOTOR = 6;
    private static final int BACK_STEERING_SENSOR = 2;

    private static final double BACK_STEERING_SENSOR_LEFTLIMIT = 89;
    private static final double BACK_STEERING_SENSOR_MIDPOINT = 808;
    private static final double BACK_STEERING_SENSOR_RIGHTLIMIT = 574;

    private static final double BACK_STEERING_PID_P = -0.01;
    private static final double BACK_STEERING_PID_I = 0.0;
    private static final double BACK_STEERING_PID_D = 0.0;

    private Steering frontSteering;
    private Steering backSteering;

    // Singleton instance variable
    private static Team467Drive driveInstance;

    // Singleton so constructor is private
    private Team467Drive()
    {
        super(FRONT_LEFT_MOTOR, BACK_LEFT_MOTOR, FRONT_RIGHT_MOTOR, BACK_RIGHT_MOTOR);
        frontSteering = new Steering(
                FRONT_STEERING_PID_P, FRONT_STEERING_PID_I, FRONT_STEERING_PID_D,
                FRONT_STEERING_MOTOR, FRONT_STEERING_SENSOR,
                FRONT_STEERING_SENSOR_LEFTLIMIT, FRONT_STEERING_SENSOR_MIDPOINT, FRONT_STEERING_SENSOR_RIGHTLIMIT
                );

        backSteering = new Steering(
                BACK_STEERING_PID_P, BACK_STEERING_PID_I, BACK_STEERING_PID_D,
                BACK_STEERING_MOTOR, BACK_STEERING_SENSOR,
                BACK_STEERING_SENSOR_LEFTLIMIT, BACK_STEERING_SENSOR_MIDPOINT, BACK_STEERING_SENSOR_RIGHTLIMIT
                );

        setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
    }

    // return single instance of this class
	public static synchronized Team467Drive GetInstance()
    {
		if (driveInstance == null)
        {
			driveInstance = new Team467Drive();
		}
		return driveInstance;
	}

    /**
     * All 4 wheels steer in the same direction.
     * @param steering
     * @param angle
     * @param speed
     */
    public void swerveDrive(double angle, double speed)
    {

        frontSteering.SetAngle(angle);
        backSteering.SetAngle(angle);
        this.drive(speed, 0);
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
        this.drive(speed, 0);
        backSteering.Print();
    }
}


