/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.IterativeRobot;
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
    
    private long startTime;
    
    private GearToothSensor gts;
    
    private LEDring LED;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit()
    {
        //Make robot objects
        driverstation = Driverstation.getInstance();
        driverstation.clearPrint();
        drive = Drive.getInstance();
        gyro = Gyro467.getInstance();
        LED = LEDring.getInstance();
        
        // static static static static static
        
        gts = new GearToothSensor(RobotMap.GEAR_TOOTH_SENSOR_CHANNEL, RobotMap.PARASITE_CIRCUMFRENCE, 60);
        
        Calibration.init();
    }

    public void disabledInit()
    {
        if (enabledOnce) 
        {
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
        gts.reset();
        gts.start();
        
        startTime = System.currentTimeMillis();
    }


    /**
     * This function is called periodically test mode
     */
    public void testPeriodic()
    {
        driverstation.readInputs();
        driverstation.clearPrint();
        Joystick467 joy = driverstation.getRightJoystick();

        double speed = joy.getStickY();
        
        if (joy.buttonDown(4))
        {
            drive.driveParasite(speed);
        }
        
        if (joy.buttonDown(9)) 
        {
            gts.reset();
        }
      
        gts.update();
        
        double RPM = gts.getAccurateRPM();
        
        driverstation.println("Parasite Wheel", 1);
        driverstation.println("RPM: " + (int) RPM, 2);
        driverstation.println("Ticks: " + gts.getTicks(), 3);
        driverstation.println("Power: " + drive.getParasite().get(), 4);
        driverstation.println("Speed: ~" + (int) gts.convertRPMtoVelocity(RPM) + " ft/s", 5);
                
        if (joy.buttonDown(5)) 
        {
            drive.driveParasite(Math.sin((System.currentTimeMillis() - startTime)*.01));
        } 
        
        driverstation.sendData();
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
        //Read driverstation inputs
        driverstation.readInputs();
        driverstation.clearPrint();

        //Branch based on mode
        if (driverstation.getRightJoystick().getFlap())
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
        Joystick467 joy = driverstation.getRightJoystick();
        
        //Set speed
        if (joy.buttonDown(2))
        {
            // Speed for turn in place
            speed = joy.getTwist();
        }       
        else if (joy.buttonDown(3))
        {
            // Speed for car drive
            speed = joy.getStickY();
        }
        else
        {
            // Speed for crab drive, field aligned or otherwise.
            speed = joy.getStickDistance();
        }
        
        // Speed modifiers
        if (joy.buttonDown(Joystick467.TRIGGER))
        {
            // Creep on trigger
            speed /= 3.0;
        }
        else if (joy.buttonDown(7))
        {
            // Turbo on button 7
            speed *= 2.0;
        }
        
        SmartDashboard.putNumber("Speed", speed );
        SmartDashboard.putNumber("Current Angle", gyro.getAngle());
        SmartDashboard.putNumber("Battery Usage", driverstation.getBatteryVoltage());
        
        //Decide drive mode
        if (joy.buttonDown(2))
        {
            //Rotate in place if button 2 is pressed
            drive.turnDrive(-speed);
        } 
        else if (joy.buttonDown(4))
        {
            drive.driveParasite(speed);
        }
        else if (joy.buttonDown(5)) 
        {
            // Drive field aligned if button 5 is pressed
            drive.crabDrive(joy.getStickAngle(), speed, true);
        }
        else if (joy.buttonDown(3))
        {
            //Car drive if button 3 is pressed.
            // Stick twist controls turning, and stick Y controls speed.
            drive.carDrive(joy.getTwist(), speed);
        }
        else
        {
            //Normally use crab drive
            drive.crabDrive(joy.getStickAngle(), speed, false);
        }
        
        if (joy.buttonPressed(11)) 
        {
            // Toggle camera if button 11 is pressed
            cam.toggleReading();
        }
        
        if (joy.buttonDown(10)) 
        {
            // Reset gyro if button 10 is pressed
            gyro.reset();
        }
        
        if (joy.buttonPressed(8)) 
        {
            // Toggle LED is button 8 is pressed.
            LED.toggle();
        }
        
        // Print camera status to driver station
        driverstation.println((cam.isReading()) 
                              ? "Target detected: " + ((cam.isTargetDetected()) ? "Yes" : "No")
                              : "Camera is not reading.", 4);
    }

    /**
     * Update steering calibration control
     */
    private void updateCalibrateControl()
    {
        int motorId = 0;
        double stickAngle = driverstation.getRightJoystick().getStickAngle();

        //Branch into motor being calibrated
        if (driverstation.getRightJoystick().getStickDistance() > 0.5)
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
