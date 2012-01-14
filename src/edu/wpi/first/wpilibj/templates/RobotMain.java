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

    }
    
    /**
     * This function is run when autonomous control mode is first enabled
     */
    public void autonomousInit()
    {
        
    }
    
    double speed;
    
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

    }

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
            updateCalibrateControl();
        }
        else
        {
            updateDriveControl();
        }
        
        
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
            speed /= 2;
        }

        //Decide drive mode
        if (driverstation.joystickButton2)
        {
            //Rotate in place if button 2 is pressed
            drive.turnDrive(-speed);
        }
        else
        {
            //Normally use field aligned drive
            drive.faDrive(driverstation.getStickAngle(driverstation.joystickX, driverstation.joystickY),
                    speed, 0);
        }
    }
    
    
    double calibrationAngle;
    
    /**
     * Update steering calibration control
     */
    private void updateCalibrateControl()
    {
        int motorId = 0;
        
        //Branch into motor being calibrated
        if (driverstation.getStickAngle(driverstation.joystickX, driverstation.joystickY) < 0)
        {
            if (driverstation.getStickAngle(driverstation.joystickX, driverstation.joystickY) < -0.5)
            {
                motorId = Drive.BACK_LEFT;
            }
            else
            {
                motorId = Drive.FRONT_LEFT;
            }
        }
        else
        {
            if (driverstation.getStickAngle(driverstation.joystickX, driverstation.joystickY) > 0.5)
            {
                motorId = Drive.BACK_RIGHT;
            }
            else
            {
                motorId = Drive.FRONT_LEFT;
            }
        }
        
        //Drive motor based on twist angle
        //Increase wheel angle by a small amount based on joystick twist
        calibrationAngle += driverstation.joystickTwist / 100.0;

        //Drive with no speed to allow only steering
        drive.faDrive(calibrationAngle, 0, 0);
        
        //Print useful Drive information to the Smart Dashboard
        drive.logDrive();
        
        //Write and set new center if trigger is pressed
        if (driverstation.joystickTrigger && !trigDebounce)
        {   
            //Write data to robot
            double currentAngle = drive.getSteeringAngle(motorId);
            data.putDouble(Drive.STEERING_KEYS[motorId], currentAngle);
            
            //Set new steering center
            drive.setSteeringCenter(motorId, currentAngle);
            
            //Reset calibration angle for next wheel
            calibrationAngle = 0;
            
            trigDebounce = true;
        }
        if (!driverstation.joystickTrigger)
        {
            trigDebounce = false;
        }  
        
    }
    
}
