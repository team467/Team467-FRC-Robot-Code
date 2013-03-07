/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.camera.AxisCamera;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotMain extends IterativeRobot
{

    //Robot objects
    private Driverstation driverstation;
    private Drive drive;
    private Shooter shooter;
    //Debouce booleans
    private boolean button4Debounce = true;
    private boolean autonomousEnabled = true;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit()
    {
        //Make robot objects
        driverstation = Driverstation.getInstance();
        drive = Drive.getInstance();
        shooter = Shooter.getInstance();
        Calibration.init();
        Autonomous.init();
        TableHandler.init();
        //AxisCamera.getInstance();
    }

    /**
     * This function is run when autonomous control mode is first enabled
     */
    public void autonomousInit()
    {
        //Read driverstation inputs
        driverstation.readInputs();

    }

    /**
     * This function is run when operator control mode is first enabled
     */
    public void teleopInit()
    {
    }

    /**
     * This function is run when test mode is first enabled
     */
    public void testInit()
    {
    }

    /**
     * This function is called periodically test mode
     */
    public void testPeriodic()
    {
        //Read driverstation inputs
        driverstation.readInputs();

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic()
    {
        if (autonomousEnabled)
        {
            Autonomous.updateAutonomous();
        }
        driverstation.sendData(); //?

    }
    //Speed to drive at (negative speeds drive backwards)
    double speed;

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic()
    {
        //Read driverstation inputs
        driverstation.readInputs();

        //Branch based on mode
        if (driverstation.JoystickDriverCalibrate)
        {
            driverstation.println("Mode: Calibrate", 1);
            updateCalibrateControl();
        }
        else
        {
            driverstation.println("Mode: Drive", 1);
            updateDriveControl();
            updateNavigatorControl();
        }

        //Send printed data to driverstation
        driverstation.sendData();
    }

    /**
     * Update normal driver control
     */
    private void updateDriveControl()
    {
        //Set speed to twist because the robot should turn in place w/ button 2
        if (driverstation.JoystickDriverButton2)
        {
            speed = driverstation.JoystickDriverTwist;
        }
        else
        {
            speed = (driverstation.getStickDistance(driverstation.JoystickDriverX,
                    driverstation.JoystickDriverY));
        }
        if (Math.abs(speed) < 0.4)
        {
            speed = 0.0;
        }
        //Decide drive mode
        if (driverstation.JoystickDriverButton2)
        {
            //Rotate in place if button 2 is pressed
            drive.turnDrive(-speed);
        }
        else
        {
            //Normally use crab drive
            drive.crabDrive(driverstation.getStickAngle(driverstation.JoystickDriverX,
                    driverstation.JoystickDriverY), speed);
        }
        System.out.println("Current Commanded Speed By Joystick: " + speed);
    }
    //Id of selected motor
    int motorId = 0;
    //Used for calibration. If calibrating steering, this is true. If calibrating wheels it is false.
    boolean steerMode = true;

    /**
     * Update steering calibration control
     */
    private void updateCalibrateControl()
    {
        double stickAngle = driverstation.getStickAngle(driverstation.JoystickDriverX,
                driverstation.JoystickDriverY);

        //Branch into motor being calibrated
        if (driverstation.getStickDistance(driverstation.JoystickDriverX,
                driverstation.JoystickDriverY) > 0.5)
        {
            if (stickAngle < 0)
            {
                if (stickAngle < -0.5)
                {
                    motorId = RobotMap.BACK_LEFT;
                }
                else
                {
                    motorId = RobotMap.FRONT_LEFT;
                }
            }
            else
            {
                if (stickAngle > 0)
                {
                    if (stickAngle > 0.5)
                    {
                        motorId = RobotMap.BACK_RIGHT;
                    }
                    else
                    {
                        motorId = RobotMap.FRONT_RIGHT;
                    }
                }
            }
        }

        //Prints selected motor to the driverstation
        printSelectedMotor();

        //Determine calibration mode
        if (driverstation.JoystickDriverButton3)
        {
            Calibration.stopWheelCalibrate();
            steerMode = true;
        }
        if (driverstation.JoystickDriverButton4 && button4Debounce)
        {
            Calibration.toggleWheelCalibrate();
            steerMode = false;
            button4Debounce = false;
        }
        if (!driverstation.JoystickDriverButton4)
        {
            button4Debounce = true;
        }

        //Branch into type of calibration
        if (steerMode)
        {
            driverstation.println("Steering Calibrate", 3);
            Calibration.updateSteeringCalibrate(motorId);
            System.out.println(drive.getSteeringAngle(motorId));
        }
        else
        {
            driverstation.println("Wheel Calibrate", 3);
            Calibration.updateWheelCalibrate(motorId);
        }

    }
    //Launch speed
    double launchSpeed = 0.8;
    boolean smallAxisDebounce = true;

    /**
     * Update control of the shooter
     */
    private void updateNavigatorControl()
    {
        //Sets turret to rotate with the rotation of the navigator stick
        shooter.driveTurretRotatorMotor(driverstation.JoystickNavigatorTwist);

        //Change launch speed
        if (driverstation.smallJoystickNavigatorY == -1.0 && smallAxisDebounce)
        {
            //Increase speed
            launchSpeed += 0.02;
            if (launchSpeed > 1.0)
            {
                launchSpeed = 1.0;
            }
            smallAxisDebounce = false;
        }
        if (driverstation.smallJoystickNavigatorY == 1.0 && smallAxisDebounce)
        {
            //Decrease speed
            launchSpeed -= 0.02;
            smallAxisDebounce = false;
        }
        if (driverstation.smallJoystickDriverY == 0.0)
        {
            //Reset debounce
            smallAxisDebounce = true;
        }

        //Print current launch speed to driverstation
        driverstation.println("Launch Speed: " + (int) (launchSpeed * 100.0) + "%", 2);

        //Run launch motor on button 3
        if (driverstation.JoystickNavigatorButton3)
        {
            shooter.driveLaunchMotor(launchSpeed);
        }
        else
        {
            shooter.driveLaunchMotor(0.0);
        }

        //Fires a frisbee on trigger press. Continual hold will continue to fire frisbees
        if (driverstation.JoystickNavigatorTrigger)
        {
            System.out.println("Frizbee Forward");
            shooter.driveFeederMotor(RobotMap.FRISBEE_DEPLOY_FORWARD);
        }
        else
        {
            //This only stops the motor after the limit switch has been pressed
            shooter.driveFeederMotor(RobotMap.FRISBEE_DEPLOY_STOP);
        }
    }

    /**
     * Prints the selected motor to the driverstation based on motor id
     */
    private void printSelectedMotor()
    {
        switch (motorId)
        {
            case RobotMap.FRONT_LEFT:
                driverstation.println("Selected Motor: FL", 2);
                break;
            case RobotMap.FRONT_RIGHT:
                driverstation.println("Selected Motor: FR", 2);
                break;
            case RobotMap.BACK_LEFT:
                driverstation.println("Selected Motor: BL", 2);
                break;
            case RobotMap.BACK_RIGHT:
                driverstation.println("Selected Motor: BR", 2);
                break;
        }
    }
}
