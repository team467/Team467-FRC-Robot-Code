/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.shs.first.team467;

import edu.wpi.first.wpilibj.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotMain extends IterativeRobot
{
    private final double SWERVE_JOYSTICK_DEADZONE = 0.5;

    Team467DriverStation driverstation;
    Team467Drive drive;
    Kicker kicker;
    Vacuum vacuum;
    Hook hook;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit()
    {
        // all robot subsystems are singletons. Get a reference to them here
        drive = Team467Drive.GetInstance();
        driverstation = Team467DriverStation.GetInstance();
        kicker = Kicker.GetInstance();
        vacuum = Vacuum.GetInstance();
        hook = Hook.GetInstance();
    }
    
    private final boolean DEBUG = false;

    // constants and variables used in autonomous mode
    private static final int STATE_FORWARDS = 1;
    private static final int STATE_FORWARDS_BALLGRAB = 2;
    private static final int STATE_KICK = 3;
    private static final int STATE_FINISHED = 4;
    private static int autonomousState = STATE_FORWARDS;
    
    private final double AUTONOMOUS_SPEED = -0.5; //negative values
    
    private final int FORWARD_TIME = 100;
    private final int BALLGRAB_TIME = 10;
    private int forwardCount = 0;
    private int ballgrabCount = 0;

    /**
     * Print state from Autonomous mode state machine
     */
    private void PrintAutonomousState()
    {
        System.out.print("Autonomous State: ");
        switch (autonomousState)
        {
            case STATE_FORWARDS:
                System.out.println("STATE_FORWARDS");
                break;
            case STATE_FORWARDS_BALLGRAB:
                System.out.println("STATE_FORWARDS_BALLGRAB");
                break;
            case STATE_KICK:
                System.out.println("STATE_KICK");
                break;
            case STATE_FINISHED:
                System.out.println("STATE_FINISHED");
                break;
            default:
                System.out.println("Unknown State");
                break;
        }
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic()
    {
        driverstation.ReadInputs();
        // don't run autonomous if both overrides are on
        if (!(driverstation.KickerOverride && driverstation.VacuumOverride))
        {
            kicker.Update();
            vacuum.Update();

            if (DEBUG)
            {
                PrintAutonomousState();
            }
            switch (autonomousState)
            {
                case STATE_FORWARDS:
                    // go forward until either a ball is grabbed
                    // or we run out of time
                    if (vacuum.BallGrabbed())
                    {
                        autonomousState = STATE_FORWARDS_BALLGRAB;
                    }
                    else if (forwardCount <= FORWARD_TIME)
                    {
                        drive.swerveDrive(0.0, AUTONOMOUS_SPEED);
                        forwardCount++;
                    }
                    else
                    {
                        autonomousState = STATE_FINISHED;
                    }
                    break;

                case STATE_FORWARDS_BALLGRAB:
                    // we have a ball in range. Keep moving forward to improve
                    // chance of it being grabbed by the vacuum
                    if (ballgrabCount <= BALLGRAB_TIME)
                    {
                        drive.swerveDrive(0.0, AUTONOMOUS_SPEED);
                        ballgrabCount++;
                    }
                    else
                    {
                        drive.swerveDrive(0.0,0.0);
                        autonomousState = STATE_KICK;
                    }
                    break;

                case STATE_KICK:
                    // If kicker is primed and ball is in range, kick it
                    drive.swerveDrive(0.0, 0.0);
                    if (kicker.Primed() && vacuum.BallGrabbed())
                    {
                        kicker.Fire();
                        autonomousState = STATE_FINISHED;
                    }
                    break;
                    
                case STATE_FINISHED:
                    drive.swerveDrive(0.0,0.0);
                    break;

            }
        }
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic()
    {
        driverstation.ReadInputs();

        kicker.Update();
        vacuum.Update();
        hook.Update();

        double speed = driverstation.RightStickY;

        // Right Trigger gives "turbo" functionality
        if (!driverstation.XboxRightTrigger)
        {
            speed /= 2;
        }

        if ((Math.abs(driverstation.LeftStickX) > SWERVE_JOYSTICK_DEADZONE) ||
            (Math.abs(driverstation.LeftStickY) > SWERVE_JOYSTICK_DEADZONE))
        {
            // Left stick moved - use swerve drive
            drive.swerveDrive(driverstation.getStickAngle(driverstation.LeftStickX, driverstation.LeftStickY),
                              speed);
        }
        else
        {
            // use Car drive - limit steering to 45 degrees in either direction
            drive.carDrive(driverstation.RightStickX / 2, speed);
        }
    }
}
