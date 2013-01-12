/* First Robotics Team 467
 *
 * Java code from 2010 Season for driving the 2009 Robot
 *
 */
package edu.shs.first.team467;

import edu.wpi.first.wpilibj.*;
//import edu.wpi.first.wpilibj.camera.AxisCamera;
//import edu.wpi.first.wpilibj.image.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Team467Robot2009 extends IterativeRobot
{
    // Constants

    private final int FRONT_LEFT_MOTOR = 3;
    private final int BACK_LEFT_MOTOR = 1;
    private final int FRONT_RIGHT_MOTOR = 5;
    private final int BACK_RIGHT_MOTOR = 2;
    private final int BALL_COLUMN_VICTOR1 = 4;
    private final int BALL_COLUMN_VICTOR2 = 7;
    private final int BALL_ROLLER_RELAY = 1;
    private final int TURRET_RELAY = 2;
    private final int FIRE_PIN_VICTOR = 6;
    private final int BALL_COLUMN_UP = -1;
    private final int BALL_COLUMN_DOWN = 1;
    private final int FIRE_PIN_OUT = 1;
    private final int FIRE_PIN_IN = -1;
    private final int OFF = 0;
    private final double SCORE_THRESHOLD = 0.01;

    // Private Class variables
    private RobotDrive drivetrain;
    private DriverStation ds = DriverStation.getInstance();
    private Victor ballColumn1;
    private Victor ballColumn2;
    private Relay ballRoller;
    private Relay turret;
    private Victor firePin;
    private Robot2010Inputs robotInput;
    private Servo servo;

//    // turnController is used in autonomous mode to turn the robot
//    // to face the target
//    private PIDController turnController;
//
//    private double angleToTarget = 0.0;
//    private boolean targetVisible = false;
//
//    public class TargetPIDSource implements PIDSource
//    {
//        public double pidGet()
//        {
//            return angleToTarget;
//        }
//    }
//
//    public class TargetPIDOutput implements PIDOutput
//    {
//
//        public void pidWrite(double output)
//        {
//            System.out.print("Drive: ");
//            System.out.println(output);
//            drivetrain.arcadeDrive(0, output);
//        }
//    }
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit()
    {
        //getWatchdog().setEnabled(false);

        robotInput = new Robot2010Inputs();

        drivetrain = new RobotDrive(FRONT_LEFT_MOTOR, BACK_LEFT_MOTOR, FRONT_RIGHT_MOTOR, BACK_RIGHT_MOTOR);
        drivetrain.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        drivetrain.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);


        ballColumn1 = new Victor(BALL_COLUMN_VICTOR1);
        ballColumn2 = new Victor(BALL_COLUMN_VICTOR2);
        ballRoller = new Relay(BALL_ROLLER_RELAY);
        turret = new Relay(TURRET_RELAY);
        firePin = new Victor(FIRE_PIN_VICTOR);
        servo = new Servo(8);
    }

//    /** Simple function to read targets and set a couple of global variables
//     * based on the status.
//     * Assumes there is only a single target (or target like object) on the field.
//     */
//    private void ReadTargets()
//    {
//        try
//        {
//            ColorImage image = AxisCamera.getInstance().getImage();
//            Target[] targets = Target.findCircularTargets(image);
//            image.free();
//            if (targets.length == 0 || targets[0].m_score < SCORE_THRESHOLD)
//            {
//                targetVisible = false;
//                System.out.println("Target Not Visible");
//            }
//            else
//            {
//                targetVisible = true;
//                angleToTarget = targets[0].getHorizontalAngle();
//                System.out.print("angleToTarget: ");
//                System.out.println(angleToTarget);
//            }
//        }
//        catch(Exception ex)
//        {
//            // Ignore any exceptions - but assume that the target wasn't found
//            targetVisible = false;
//            System.out.println("Target Exception");
//        }
//        return;
//    }
//
//    public void autonomousInit()
//    {
//        turnController = new PIDController( 0.08, 0.01, 0.0,
//                                            new TargetPIDSource(),
//                                            new TargetPIDOutput(),
//                                            0.1);
//
//        turnController.disable();
//        turnController.setInputRange(-15.0,15.0);
//        turnController.setOutputRange(-1.0, 1.0);
//        turnController.setSetpoint(0.0);
//        turnController.setTolerance(5.0);
//    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic()
    {
//        ReadTargets();
//
//        if (targetVisible)
//        {
//            turnController.enable();
//            if (turnController.onTarget())
//            {
//                System.out.println("On Target");
//            }
//        }
//        else
//        {
//            System.out.println("Searching for Target");
//            turnController.disable();
//            drivetrain.arcadeDrive(0.0, 0.5);
//        }
    }

    /**
     * Helper function for driving ball column.
     * The Ball Column is driven with 2 motors. Use of this
     * function ensures that both motors are always engaged
     * @param direction BALL_COLUMN_UP, BALL_COLUMN_DOWN or OFF
     */
    private void DriveBallColumn(int direction)
    {
        ballColumn1.set(direction);
        ballColumn2.set(direction);
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic()
    {
        if  (isOperatorControl() && isEnabled())
        {
            robotInput.ReadInputs();  // Read all robot inputs once per loop

            drivetrain.tankDrive(robotInput.dsLeftStick, robotInput.dsRightStick);

            if (!robotInput.dsFireButton)
            {
                // if Fire button is not depressed
                // Ball roller in/out automatically tracks with column up/down

                if (robotInput.dsBallUp)
                {
                    DriveBallColumn(BALL_COLUMN_UP);
                    ballRoller.set(Relay.Value.kReverse);
                }
                else if (robotInput.dsBallDown)
                {
                    DriveBallColumn(BALL_COLUMN_DOWN);
                    ballRoller.set(Relay.Value.kForward);
                }
                else
                {
                    DriveBallColumn(OFF);
                    // If ball column is not being driven then use the
                    // independent ball roller controls to drive the
                    // roller directly based on user input
                    if (robotInput.dsRollerOut)
                    {
                        servo.set(1);
                    }
                    else if (robotInput.dsRollerIn)
                    {
                        servo.set(0);
                    }
                    else
                    {
                        ballRoller.set(Relay.Value.kOff);
                    }

                }

                // If fire mechanism isn't resting in the
                // ball block position, then drive it until it is
                if (robotInput.diFirePinOutSensor)
                {
                    firePin.set(FIRE_PIN_OUT);
                }
                else
                {
                    firePin.set(OFF);
                }

            }
            else
            {
                // Fire button is depressed. Drive the firing mechanism
                // until it is in the retracted (i.e. firing) position
                // then drive the ball column to fire the ball
                if (robotInput.diFirePinInSensor)
                {
                    // fire pin must still retract
                    firePin.set(FIRE_PIN_IN);
                }
                else
                {
                    DriveBallColumn(BALL_COLUMN_UP);
                    firePin.set(OFF);
                }

            }

            // Drive turret based on user input
            if (robotInput.dsTurretRight)
            {
                turret.set(Relay.Value.kForward);
            }
            else if (robotInput.dsTurretLeft)
            {
                turret.set(Relay.Value.kReverse);
            }
            else
            {
                turret.set(Relay.Value.kOff);
            }
        }
    }
}
