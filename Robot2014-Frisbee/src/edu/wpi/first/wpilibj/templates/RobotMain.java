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
    private boolean enabledOnce = false;
    private Gyro467 gyro;
    private Compressor467 comp;
    
    private Shooter shoot;
    
    private long startTime;

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
        shoot = Shooter.getInstance();
        shoot.init();
        comp = Compressor467.getInstance();
        
        // static static static static static
        
        Calibration.init();
    }

    public void disabledInit()
    {
        if (enabledOnce) 
        {
        }
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
        
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic()
    {   
        //Read driverstation inputs
        driverstation.readInputs();
        driverstation.clearPrint();
        
        comp.update();

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
        else
        {
            //Normally use crab drive
            drive.crabDrive(joy.getStickAngle(), speed);
        }
        
        if (joy.buttonDown(10)) 
        {
            // Reset gyro if button 10 is pressed
            gyro.reset();
        }
        //Run launch motor on button 4
        if (joy.buttonDown(4))
        {
            System.out.println(RobotMap.SHOOTER_RUN_SPEED);
            shoot.driveLaunchMotor(RobotMap.SHOOTER_RUN_SPEED);
        }
        else
        {
            shoot.driveLaunchMotor(0.0);
        }

        //polls the trigger to see if fire, but only fires if wheel spin button held and at commanded speed
        if (joy.buttonDown(6) && joy.buttonDown(4) && shoot.atCommandedSpeed())
        {
            shoot.deployFrisbeePneu(Shooter.PNEU_DEPLOY_OUT);
        }
        else
        {
            shoot.deployFrisbeePneu(Shooter.PNEU_IDLE);
        }
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