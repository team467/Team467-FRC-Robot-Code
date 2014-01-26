/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
    //private Camera467 cam;
    private Camera467 cam;
    private boolean enabledOnce = false;
    private Gyro467 gyro;
    
    private boolean button10debounce = false;
    private boolean button11debounce = false;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit()
    {
        //Make robot objects
        driverstation = Driverstation.getInstance();
        drive = Drive.getInstance();
        gyro = Gyro467.getInstance();
        
        Calibration.init();
    }

    public void disabledInit()
    {
        if (enabledOnce) {
            cam.killThread();
        }
    }

    /**
     * This function is run when autonomous control mode is first enabled
     */
    public void autonomousInit()
    {
        
        Autonomous.resetState(0);
        Autonomous.init();
        //Read driverstation inputs
        driverstation.readInputs();
    }

    /**
     * This function is run when operator control mode is first enabled
     */
    public void teleopInit()
    {
        Autonomous.resetState(0);
        
        enabledOnce = true;
        cam = Camera467.getInstance();
        cam.startThread();
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

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic()
    {
        Autonomous.updateAutonomous(0);
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic()
    {   
        button10debounce = driverstation.JoystickRightButton10;
        button11debounce = driverstation.JoystickRightButton11;
        
        //Read driverstation inputs
        driverstation.readInputs();

        //Branch based on mode
        if (driverstation.JoystickRightCalibrate)
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
        //Speed to drive at (negative speeds drive backwards)
        double speed;
        
        //Set speed
        if (driverstation.JoystickRightButton2)
        {
            // Speed for turn in place
            speed = driverstation.JoystickRightTwist;
        }       
        else if (driverstation.JoystickRightButton3)
        {
            // Speed for car drive
            speed = driverstation.JoystickRightY;
        }
        else
        {
            // Speed for crab drive, field aligned or otherwise.
            speed = driverstation.getStickDistance(driverstation.JoystickRightX, driverstation.JoystickRightY);
        }
        
        // Speed modifiers
        if (driverstation.JoystickRightTrigger)
        {
            // Creep on trigger
            speed /= 3.0;
        }
        else if (driverstation.JoystickRightButton7)
        {
            // Turbo on button 7
            speed *= 2.0;
        }
        
        SmartDashboard.putNumber("Speed", speed );
        SmartDashboard.putNumber("Current Angle", gyro.getAngle());
        SmartDashboard.putNumber("Battery Usage", driverstation.getBatteryVoltage());
        
        //Decide drive mode
        if (driverstation.JoystickRightButton2)
        {
            //Rotate in place if button 2 is pressed
            drive.turnDrive(-speed);
        } 
        else if (driverstation.JoystickRightButton5) 
        {
            // Drive field aligned if button 5 is pressed
            drive.crabDrive(driverstation.getStickAngle(driverstation.JoystickRightX, driverstation.JoystickRightY),
                            speed, true);
        }
        else if (driverstation.JoystickRightButton3)
        {
            //Car drive if button 3 is pressed.
            // Stick twist controls turning, and stick Y controls speed.
            drive.carDrive(driverstation.JoystickRightTwist, speed);
        }
        else
        {
            //Normally use crab drive
            drive.crabDrive(driverstation.getStickAngle(driverstation.JoystickRightX, driverstation.JoystickRightY),
                            speed, false);
        }
        
        if (button11debounce && !driverstation.JoystickRightButton11) 
        {
            // Toggle camera if button 12 is pressed
            cam.toggleReading();
        }
        
        if (button10debounce && !driverstation.JoystickRightButton10) 
        {
            // Reset gyro if button 10 is pressed
            gyro.reset();
        }
        
        // Print camera status to driver station
        driverstation.println((cam.isReading()) 
                              ? "Target detected: " + cam.isTargetDetected()
                              : "Camera is not reading.", 4);
    }

    /**
     * Update steering calibration control
     */
    private void updateCalibrateControl()
    {
        int motorId = 0;
        double stickAngle = driverstation.getStickAngle(driverstation.JoystickRightX, driverstation.JoystickRightY);

        //Branch into motor being calibrated
        if (driverstation.getStickDistance(driverstation.JoystickRightX, driverstation.JoystickRightY) > 0.5)
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
        printSelectedMotor(motorId);

        driverstation.println("Steering Calibrate", 3);
        Calibration.updateSteeringCalibrate(motorId);
    }

    /**
     * Update control of the #removed#
     */
    private void updateNavigatorControl()
    {
        
    }

    /**
     * Prints the selected motor to the driverstation based on motor id
     */
    private void printSelectedMotor(int motorId)
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
