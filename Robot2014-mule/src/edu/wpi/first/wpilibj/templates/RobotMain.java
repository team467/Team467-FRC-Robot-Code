/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotMain extends IterativeRobot {

    //Robot objects
    private Driverstation driverstation;
    private Drive drive;
    private Gyro467 gyro;
    private PIDAlignment alignDrive;
    //private Llamahead llamahead;
    //private PneumaticArm arm;
    //private Compressor467 compressor;

    //Debounce of JoystickLeft button so staring the wheel calibration is only called
    //once
    private boolean button4Debounce = true;

    private boolean button7Debounce = true;

    private boolean triggerDebounce = true;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialisation code.
     */
    public void robotInit()
    {
        //Make robot objects
        driverstation = Driverstation.getInstance();
        drive = Drive.getInstance();
        gyro = Gyro467.getInstance();
        alignDrive = new PIDAlignment(1.6, 0.0, 0.0);
        Calibration.init();
        Autonomous.init();
        TableHandler.init();
        PIDTuning.init();
//        AxisCamera.getInstance();
        //SmartDashboardHandler.init();
    }

    public void disabledInit()
    {
        //llamahead.setJaguarMode(CANJaguar.NeutralMode.kCoast);
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

    double angle = -1.0;
    int ticks = 0;

    /**
     * This function is run when test mode is first enabled
     */
    public void testInit()
    {
        LiveWindow.setEnabled(false);
        angle = -1.0;
        ticks = 0;
    }


    /**
     * This function is called periodically test mode
     */
    public void testPeriodic()
    {
        //Read driverstation inputs
        driverstation.readInputs();

        PIDTuning.updateWheelAngleTune();

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic()
    {

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
        if (driverstation.JoystickLeftCalibrate)
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

        //Print gyro angle to driverstation
//        if (compressor.compressionFinished())
//        {
//            driverstation.println("Compression Done", 5);
//        }
//        else
//        {
//            driverstation.println("Compressing...", 5);
//        }

        //Print ultrasonic value to driverstation
        driverstation.println("Distance: " + Autonomous.getUltrasonic(), 6);

        //Send printed data to driverstation
        driverstation.sendData();
    }

    /**
     * Update normal driver control
     */
    private void updateDriveControl()
    {
        //Set speed
        if (driverstation.JoystickLeftButton2)
        {
            speed = driverstation.JoystickLeftTwist;
            if (driverstation.JoystickLeftTrigger)
            {
                speed /= 3.0;
            }
        }
        else
        {
            speed = driverstation.getStickDistance(driverstation.JoystickLeftX, driverstation.JoystickLeftY);

            //Turbo on button 7
            if (driverstation.JoystickLeftButton7)
            {

                speed *= 2.0;
            }
        }

        //Decide drive mode
        if (driverstation.JoystickLeftButton2)
        {
            //Rotate in place if button 2 is pressed
            drive.turnDrive(-speed);
        }
        else if (driverstation.smallJoystickLeftX != 0.0 ||
                driverstation.smallJoystickLeftY != 0.0)
        {
            //Align drive if small JoystickLeft is pressed
            if (driverstation.smallJoystickLeftX == -1.0)
            {
                alignDrive.setOrientation(-0.5);
            }
            else if (driverstation.smallJoystickLeftX == 1.0)
            {
                alignDrive.setOrientation(0.5);
            }
            else if (driverstation.smallJoystickLeftY == -1.0)
            {
                alignDrive.setOrientation(0);
            }
            else if (driverstation.smallJoystickLeftY == 1.0)
            {
                alignDrive.setOrientation(1.0);
            }
        }
        else
        {
            //Normally use crab drive
            drive.crabDrive(driverstation.getStickAngle(driverstation.JoystickLeftX, driverstation.JoystickLeftY),
                    speed, false);
        }
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
        double stickAngle = driverstation.getStickAngle(driverstation.JoystickLeftX, driverstation.JoystickLeftY);

        //Branch into motor being calibrated
        if (driverstation.getStickDistance(driverstation.JoystickLeftX, driverstation.JoystickLeftY) > 0.5)
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
        if (driverstation.JoystickLeftButton3)
        {
            Calibration.stopWheelCalibrate();
            steerMode = true;
        }
        if (driverstation.JoystickLeftButton4 && button4Debounce)
        {
            Calibration.toggleWheelCalibrate();
            steerMode = false;
            button4Debounce = false;
        }
        if (!driverstation.JoystickLeftButton4)
        {
            button4Debounce = true;
        }

        //Branch into type of calibration
        if (steerMode)
        {
            driverstation.println("Steering Calibrate", 3);
            Calibration.updateSteeringCalibrate(motorId);
        }
        else
        {
            driverstation.println("Wheel Calibrate", 3);
            Calibration.updateWheelCalibrate(motorId);
        }

    }

    private double launchSpeed = 0.0;

    /**
     * Update control of the Llamahead (launcher)
     */
    private void updateNavigatorControl()
    {
        
    }

    /**
     * Update control of the llamahead (launcher) using buttons on the JoystickLeft
     * for testing purposes
     */
    private void updateJoystickNavigatorControl()
    {

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
