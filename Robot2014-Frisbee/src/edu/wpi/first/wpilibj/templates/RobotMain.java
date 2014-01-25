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

    private int DRIVE_SLOW_TRIGGER = 0;
    private int DRIVE_TURBO_TRIGGER = 1;
    //var used throughout
    private int DRIVE_TRIGGER_COMMAND = DRIVE_SLOW_TRIGGER;

    //Robot objects
    private Driverstation driverstation;
    private Drive drive;
    private Gyro467 gyro;

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
        gyro = Gyro467.getInstance();
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
        gyro.reset();
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
    double gyroAngle;

    long prevTime = System.currentTimeMillis();

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic()
    {
//        System.out.println(System.currentTimeMillis() - prevTime);
//        prevTime = System.currentTimeMillis();
        System.out.println("An:" + gyro.getAngle() + 
                           " dr:" + (-gyroAngle + driverstation.getStickAngle(driverstation.JoystickDriverX,
                                                                              driverstation.JoystickDriverY)));

        //Read driverstation inputs
        driverstation.readInputs();

        //resets the gryo when button 10 pressed
        if (driverstation.JoystickDriverButton10)
        {
            gyro.reset();
        }

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
        if (driverstation.JoystickDriverButton12)
        {
            //==========Turn in place===================
            if (driverstation.JoystickDriverButton3)
            {
                turnInPlace();
                return;
            }
            //sets speed to stick distance
            speed = (driverstation.getStickDistance(driverstation.JoystickDriverX,
                                                    driverstation.JoystickDriverY));

            //limits speed
            //sets the drive speed so it will not drive below a minimum speed
            if (Math.abs(speed) < MINUMUM_DRIVE_SPEED)
            {
                speed = 0.0;
            }

            //takes the gyro angle, converts it from (-180 to 180) to (-1 to 1)
            gyroAngle = gyro.getAngle();

            //drives with the angle of the wheels being straight, plus the angle of the stick
            drive.crabDrive(-gyroAngle + driverstation.getStickAngle(driverstation.JoystickDriverX,
                                                                     driverstation.JoystickDriverY), speed);

        }
        //============crab drive=============
        else
        {
            //============turn in place============
            if (driverstation.JoystickDriverButton3)
            {
                turnInPlace();
                return;
            }
            //sets speed to stick distance
            speed = (driverstation.getStickDistance(driverstation.JoystickDriverX,
                                                    driverstation.JoystickDriverY));
            //limits speed
            //sets the drive speed so it will not drive below a minimum speed
            if (Math.abs(speed) < MINUMUM_DRIVE_SPEED)
            {
                speed = 0.0;
            }
            //sets command of trigger
            if (driverstation.JoystickDriverTrigger && DRIVE_TRIGGER_COMMAND == DRIVE_SLOW_TRIGGER)
            {
                speed = speed / 2;
            }
            else if (driverstation.JoystickDriverTrigger && DRIVE_TRIGGER_COMMAND == DRIVE_TURBO_TRIGGER)
            {
                speed = speed * 2;
            }
            //drives with the limited speed
            drive.crabDrive(driverstation.getStickAngle(driverstation.JoystickDriverX,
                                                        driverstation.JoystickDriverY), speed);
        }
    }

    private void turnInPlace()
    {
        //============turn in place============

        //sets the speed to the joystick twist amount
        speed = driverstation.JoystickDriverTwist;

        //slow precision rotate
        if (driverstation.JoystickDriverTrigger)
        {
            speed /= 2;
        }

        //Rotate in place if button 3 is pressed
        drive.turnDrive(-speed);
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
