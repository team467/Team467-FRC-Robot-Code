/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Preferences;

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
    private Preferences data;
    private Autonomous autonomous;
    private Gyro467 gyro;
    private PIDAlignment alignDrive;
    
    //Debounce for trigger on calibrating
    private boolean trigDebounce = false;
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() 
    {
        //Make robot objects
        driverstation = Driverstation.getInstance();
        
        drive = Drive.getInstance();
        data = Preferences.getInstance();
        gyro = Gyro467.getInstance();
        alignDrive = new PIDAlignment(1.6, 0.0, 0.0);
        //autonomous = Autonomous467.getInstance();

    }
    
    /**
     * This function is run when autonomous control mode is first enabled
     */
    public void autonomousInit()
    {
        
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
        autonomous.updateAutonomous();
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
        }
        
        //Gyro reset at button 7
        if (driverstation.joystickButton7)
        {
            gyro.reset();
        }
        
        //Print angles to driverstation
        drive.logDrive();
        
        //Send printed data to driverstation
        driverstation.sendData();
        
        //Read out kinect values
        kinect.updateKinect();
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

        //Implement turbo
        if (!driverstation.joystickTrigger)
        {
            speed /= 2.0;
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
            //Normally use field aligned drive
            drive.faDrive(driverstation.getStickAngle(driverstation.joystickX, driverstation.joystickY),
                    speed, 0);
        }
    }
    
    //Incremented angle used for calibrating wheels
    double calibrationAngle = 0.0;
    
    //Id of selected motor
    int motorId = 0;
    
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
        
        //Drive motor based on twist angle
        //Increase wheel angle by a small amount based on joystick twist
        calibrationAngle += driverstation.joystickTwist / 100.0;
        
        if (calibrationAngle > 1.0) calibrationAngle -= 2.0;
        if (calibrationAngle < -1.0) calibrationAngle += 2.0;

        //Drive with no speed to allow only steering
        drive.individualSteeringDrive(calibrationAngle, 0, motorId);
        
        //Write and set new center if trigger is pressed
        if (driverstation.joystickTrigger && !trigDebounce)
        {   
            double currentAngle = drive.getSteeringAngle(motorId);
            
            //Write data to robot
            data.putDouble(RobotMap.STEERING_KEYS[motorId], currentAngle);
            data.save();
            
            //Set new steering center
            drive.setSteeringCenter(motorId, currentAngle);
            
            //Reset calibration angle
            calibrationAngle = 0.0;
            
            trigDebounce = true;
        }
        if (!driverstation.joystickTrigger)
        {
            trigDebounce = false;
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
