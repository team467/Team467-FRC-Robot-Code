/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.shs.first.team467;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

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

    JoystickDriverStation joystickDriverstation;
    Team467DriverStation xboxDriverstation;
    Team467Drive drive;

    //Teleop state constants
    private final int TELEOP_STATE_DRIVE = 0;
    private final int TELEOP_STATE_FOLLOW_LINE = 1;
    private final int TELEOP_STATE_MOVE_TO_CIRCLE = 2;
    private final int TELEOP_STATE_STRAFE_TO_CIRCLE = 3;

    //Teleop state
    private int teleopState = TELEOP_STATE_DRIVE;

    //Use joystick driverstation variable. I false, xbox will be used
    private final boolean JOYSTICK_DRIVERSTATION = true;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit()
    {
        drive = Team467Drive.GetInstance();
        if (JOYSTICK_DRIVERSTATION)
        {
            joystickDriverstation = JoystickDriverStation.GetInstance();
        }
        else
        {
            xboxDriverstation = Team467DriverStation.GetInstance();
        }
        SmartDashboard.init();
        SmartDashboard.log("Drive", "Teleop State");
        drive.startCameraRead();

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic()
    {
        
    }

    long lastTime = 0;
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic()
    {
        //Set led to always on
        //Make speed
        double speed = 0.0;
        SmartDashboard.log(drive.getGyro(), "Gyro");
        if (JOYSTICK_DRIVERSTATION)
        {
            // Read joystick driverstation
            joystickDriverstation.ReadInputs();
            // Get speed based on joystick distance from center
            speed = joystickDriverstation.getStickDistance(joystickDriverstation.JoystickX, joystickDriverstation.JoystickY);
            // Use trigger as trurbo
            if (!joystickDriverstation.JoystickTrigger)
            {
                speed /= 2;
            }
            if (joystickDriverstation.JoystickButton5)
            {
                teleopState = TELEOP_STATE_DRIVE;
                drive.stopCameraRead();
            }
            if (joystickDriverstation.JoystickButton3)
            {
                teleopState = TELEOP_STATE_FOLLOW_LINE;
                drive.stopCameraRead();
            }
            if (joystickDriverstation.JoystickButton4)
            {
                teleopState = TELEOP_STATE_STRAFE_TO_CIRCLE;
                drive.resetStrafeState();
                drive.startCameraRead();
            }
            if (joystickDriverstation.JoystickButton6)
            {
                drive.resetGyro();
                System.out.println("Gyro Resst");
            }
        }
        else
        {
            // Read xbox driverstation
            xboxDriverstation.ReadInputs();
            // Get speed based on joystick distance from center
            speed = xboxDriverstation.RightStickY;
            // Use trigger as trurbo
            if (!xboxDriverstation.XboxRightTrigger)
            {
                speed /= 2;
            }
        }

        switch (teleopState)
        {
            case TELEOP_STATE_DRIVE:
                if (JOYSTICK_DRIVERSTATION)
                {
                    updateJoystickDrive(speed);
                }
                else
                {
                    updateXboxDrive(speed);
                }
                break;
            case TELEOP_STATE_FOLLOW_LINE:
                drive.updateLineFollow(speed, false);
                break;
            case TELEOP_STATE_STRAFE_TO_CIRCLE:
                drive.updateCircleStrafe();
                break;
        }

    }

    private void updateXboxDrive(double speed)
    {
        if ((Math.abs(xboxDriverstation.LeftStickX) > SWERVE_JOYSTICK_DEADZONE) ||
                (Math.abs(xboxDriverstation.LeftStickY) > SWERVE_JOYSTICK_DEADZONE))
                {
                    // Left stick moved - use field aligned drive
                    drive.swerveDrive(xboxDriverstation.getStickAngle(xboxDriverstation.LeftStickX, xboxDriverstation.LeftStickY),
                              speed);
                }
                else
                {
                    // use Car drive - limit steering to 45 degrees in either direction
                    drive.carDrive(xboxDriverstation.RightStickX / 2, speed);
                }
    }

    /**
     * Update the teleop drive code. Call periodically to use normal drive code.
     * @param speed The speed to drive at
     */
    private void updateJoystickDrive(double speed)
    {
        // Use car drive if joystick twisted
        if (joystickDriverstation.JoystickTwist != 0)
        {
            drive.carDrive(joystickDriverstation.JoystickTwist / 2,
                      speed);

        }
        else
        {
            // Use field aligned drive if button 2 is pressed
            if (joystickDriverstation.JoystickButton2)
            {
                // Determine if going forward or backward
                if (joystickDriverstation.JoystickY <= 0)
                {
                    drive.faDrive(joystickDriverstation.getStickAngle(joystickDriverstation.JoystickX, joystickDriverstation.JoystickY),
                          speed);
                }
                else
                {
                    drive.faDrive(-joystickDriverstation.getStickAngle(joystickDriverstation.JoystickX, joystickDriverstation.JoystickY),
                          speed);
                }
            }
            else
            {
                // Use swerve drive
                // Determine if going forward or backward
                if (joystickDriverstation.JoystickY <= 0)
                {
                    drive.swerveDrive(joystickDriverstation.getStickAngle(joystickDriverstation.JoystickX, joystickDriverstation.JoystickY),
                          speed);
                }
                else
                {
                    drive.swerveDrive(-joystickDriverstation.getStickAngle(joystickDriverstation.JoystickX, joystickDriverstation.JoystickY),
                          speed);
                }
            }
        }
    }

}
