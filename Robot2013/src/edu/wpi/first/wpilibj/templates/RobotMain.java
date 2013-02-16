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
public class RobotMain extends IterativeRobot {
    
    //Robot objects
    private Driverstation driverstation;
    private Drive drive;
    
    //Debouce booleans
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
        Calibration.init();
        Autonomous.init();
        TableHandler.init();
        AxisCamera.getInstance();
    }
    
    public void disabledInit()
    {
        
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
        //Set speed
        if (driverstation.JoystickDriverButton2)
        {
            speed = driverstation.JoystickDriverTwist;
        }
        else
        {
            speed = driverstation.getStickDistance(driverstation.JoystickDriverX, 
                    driverstation.JoystickDriverY);
            
            //Turbo on trigger
            if (!driverstation.JoystickDriverTrigger)
            {
            
                speed /= 2.0;
            }
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
        }
        else
        {
            driverstation.println("Wheel Calibrate", 3);
            Calibration.updateWheelCalibrate(motorId);
        }      
        
    }
    
    /**
     * Update control of the shooter
     */
    private void updateNavigatorControl()
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
