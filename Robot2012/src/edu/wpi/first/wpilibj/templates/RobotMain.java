/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.GearTooth;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.AnalogChannel;

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
    private Llamahead llamahead;

    private boolean button4Debounce = true;
    
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
        alignDrive = new PIDAlignment(1.6, 0.0, 0.0);
        llamahead = Llamahead.getInstance();
        Calibration.init();
        Autonomous.init();
    }
    
    /**
     * This function is run when autonomous control mode is first enabled
     */
    public void autonomousInit()
    {
        Autonomous.resetState();
    }
    
    /**
     * This function is run when operator control mode is first enabled
     */
    public void teleopInit()
    {
        
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() 
    {
        Autonomous.updateAutonomous();
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
        if (driverstation.joystickCalibrate)
        {
            driverstation.println("Mode: Calibrate", 1);
            updateCalibrateControl();
        }
        else
        {
            driverstation.println("Mode: Drive", 1);
            updateDriveControl();
            updateLlamaheadControl();
        }
                
        //Gyro reset at button 7
        if (driverstation.joystickButton7)
        {
            gyro.reset();
        }
        
        //Send printed data to driverstation
        driverstation.sendData();
    }
    
    /**
     * Update normal driver control
     */
    private void updateDriveControl()
    {
        //Set speed
        if (driverstation.joystickButton2)
        {
            speed = driverstation.joystickTwist;
        }
        else
        {
            speed = driverstation.getStickDistance(driverstation.joystickX, driverstation.joystickY);
        }

        //Decide drive mode
        if (driverstation.joystickButton2)
        {
            //Rotate in place if button 2 is pressed
            drive.turnDrive(-speed);
        }
        else if (driverstation.smallJoystickX != 0.0 ||
                driverstation.smallJoystickY != 0.0)
        {
            //Align drive if small joystick is pressed
            if (driverstation.smallJoystickX == -1.0)
            {
                alignDrive.setOrientation(-0.5);
            }
            else if (driverstation.smallJoystickX == 1.0)
            {
                alignDrive.setOrientation(0.5);
            }
            else if (driverstation.smallJoystickY == -1.0)
            {
                alignDrive.setOrientation(0);
            }
            else if (driverstation.smallJoystickY == 1.0)
            {
                alignDrive.setOrientation(1.0);
            }
        }
        else
        {
            //Normally use crab drive
            drive.crabDrive(driverstation.getStickAngle(driverstation.joystickX, driverstation.joystickY),
                    speed, false);
        }
    }
    
    //Id of selected motor
    int motorId = 0;
    
    boolean steerMode = true;
    /**
     * Update steering calibration control
     */
    private void updateCalibrateControl()
    {
        double stickAngle = driverstation.getStickAngle(driverstation.joystickX, driverstation.joystickY);
        
        //Branch into motor being calibrated
        if (driverstation.getStickDistance(driverstation.joystickX, driverstation.joystickY) > 0.5)
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
        if (driverstation.joystickButton3)
        {
            Calibration.stopWheelCalibrate();
            steerMode = true;
        }
        if (driverstation.joystickButton4 && button4Debounce)
        {
            Calibration.switchWheelCalibrate();
            steerMode = false;
            button4Debounce = false;
        }
        if (!driverstation.joystickButton4)
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
    
    //Launching speed
    double launchSpeed = 0.0;
    
    /**
     * Update control of the llamahead (launcher)
     */
    private void updateLlamaheadControl()
    {   
        //Increment launch speed based on twist
        launchSpeed += driverstation.tempTwist / 100.0;
        
        if (launchSpeed > 1.0) launchSpeed = 1.0;
        if (launchSpeed < 0.0) launchSpeed = 0.0;
        
        //Print speed to driverstation
        driverstation.println("Launch Speed: " + launchSpeed, 2);
        
        //Drive ball pickup on button 3
        if (driverstation.tempButton3)
        {
            llamahead.setBallIntake(Llamahead.FORWARD);
        }
        else
        {
            llamahead.setBallIntake(Llamahead.STOP);
        }
        
        //Drive ball advance on button 4
        if (driverstation.tempButton4)
        {
            llamahead.setBallAdvance(Llamahead.FORWARD);
        }
        else
        {
            llamahead.setBallAdvance(Llamahead.STOP);
        }
        
        //Only drive wheel if trigger is pressed
        if (driverstation.tempTrigger)
        {
            llamahead.setLauncherWheel(launchSpeed);
        }
        else
        {
            llamahead.setLauncherWheel(0.0);
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
