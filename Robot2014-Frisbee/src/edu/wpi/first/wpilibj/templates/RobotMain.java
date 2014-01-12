/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.IterativeRobot;

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

    private static final boolean AUTONOMOUS_ENABLED = true;
    private static final double MINUMUM_DRIVE_SPEED = 0.3;
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
        Autonomous.init();

    }

    /**
     * This function is run when autonomous control mode is first enabled
     */
    public void autonomousInit()
    {
        //Read driverstation inputs
        driverstation.readInputs();
        Autonomous.init();

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
        if (AUTONOMOUS_ENABLED)
        {
            Autonomous.updateAutonomous();
        }
        //updates data to the driverstation, such as println
        driverstation.sendData();

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

        //============turn in place============
        if (driverstation.JoystickDriverButton3)
        {            
            //sets the speed to the joystick twist amount
            speed = driverstation.JoystickDriverTwist;

            //slow precision rotate
            if (driverstation.JoystickDriverTrigger)
            {
                speed = speed / 2;
            }
            //Rotate in place if button 3 is pressed
            drive.turnDrive(-speed);
        }
//        //============car drive==============
//        else if (driverstation.JoystickDriverButton4)
//        {
//            //sets speed to stick distance
//            speed = (driverstation.getStickDistance(driverstation.JoystickDriverX,
//                    driverstation.JoystickDriverY));
//            //limits speed
//            //sets the drive speed so it will not drive below a minimum speed
//            if (Math.abs(speed) < MINUMUM_DRIVE_SPEED)
//            {
//                speed = 0.0;
//            }
//            //drives with the limited speed
//            drive.carDrive(driverstation.getStickAngle(driverstation.JoystickDriverX,
//                    driverstation.JoystickDriverY), speed);
//            
//        }
        //============crab drive=============
        else
        {
            //sets speed to stick distance
            speed = (driverstation.getStickDistance(driverstation.JoystickDriverX,
                    driverstation.JoystickDriverY));
            //limits speed
            //sets the drive speed so it will not drive below a minimum speed
            if (Math.abs(speed) < MINUMUM_DRIVE_SPEED)
            {
                speed = 0.0;
            }
            //drives with the limited speed
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
        double stickAngle = driverstation.getStickAngle(driverstation.JoystickDriverX, driverstation.JoystickDriverY);

        //Branch into motor being calibrated
        if (driverstation.getStickDistance(driverstation.JoystickDriverX, driverstation.JoystickDriverY) > 0.5)
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
    }
    //Launch speed
    double launchSpeed = RobotMap.SHOOTER_RUN_SPEED;
    boolean smallAxisDebounce = true;

    /**
     * Update control of the shooter
     */
    private void updateNavigatorControl()
    {
    }    
}
