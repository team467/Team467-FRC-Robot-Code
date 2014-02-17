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
    //sets if cam is to ever be intialized and used
    private final boolean CAMERA_ENABLED = false;
    
    //Robot objects
    private Driverstation driverstation;
    private Drive drive;
    //private Camera467 cam;
    private Camera467 cam;
    private GyroI2C467 gyroi2c;
    private boolean enabledOnce = false;

    private double steeringRange = 0;

    //private Gyro467 gyro;
    private long startTime;

    //private LEDring LED;
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
        gyroi2c = GyroI2C467.getInstance();
        //gyro = Gyro467.getInstance();
        //LED = LEDring.getInstance();

        // static static static static static
        Calibration.init();
    }

    public void disabledInit()
    {
        if (enabledOnce && CAMERA_ENABLED)
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
        if (CAMERA_ENABLED)
        {
            cam = Camera467.getInstance();
            cam.startThread();
        }
    }

    /**
     * This function is run when test mode is first enabled
     */
    public void testInit()
    {
        /*
         gts.reset();
         gts.start();
        
         startTime = System.currentTimeMillis();
         */
    }

    /**
     * This function is called periodically test mode
     */
    public void testPeriodic()
    {
        
        //<editor-fold defaultstate="collapsed" desc="Commented Out Test Periodic Code">
        /*
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
        */
        
        /*
        Steering steering = drive.getSteering(RobotMap.FRONT_LEFT);
        
        steering.getMotor().set(.3);
        
        double val = drive.getSteering(RobotMap.FRONT_LEFT).getSensorValue();
        
        if (val > steeringRange) {
        steeringRange = val;
        }
        
        System.out.println(steeringRange);
        */
//</editor-fold>
        System.out.println("FL: " + drive.getSteering(RobotMap.FRONT_LEFT).getSensorValue());
        System.out.println("BL: " + drive.getSteering(RobotMap.BACK_LEFT).getSensorValue());
        System.out.println("BR: " + drive.getSteering(RobotMap.BACK_RIGHT).getSensorValue());
        System.out.println("FR: " + drive.getSteering(RobotMap.FRONT_RIGHT).getSensorValue());
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
            System.out.println("FL: " + drive.getSteering(RobotMap.FRONT_LEFT).getSensorValue());
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
        Joystick467 joyRight = driverstation.getRightJoystick();

        //Set speed
        if (joyRight.buttonDown(2))//TURN IN PLACE
        {
            // Speed for turn in place
            speed = joyRight.getTwist();
        }
        else if (joyRight.buttonDown(3))//CAR DRIVE
        {
            // Speed for car drive
            speed = joyRight.getStickY();
        }        
        else//CRAB OR FIELD ALIGNED
        {
            // Speed for crab drive, field aligned or otherwise.
            speed = joyRight.getStickDistance();
        }

        // Speed modifiers
        if (joyRight.buttonDown(Joystick467.TRIGGER))//CREEP
        {
            // Creep on trigger
            speed /= 3.0;
        }
        else if (joyRight.buttonDown(7))//TURBO
        {
            // Turbo on button 7
            speed *= 2.0;
        }

        SmartDashboard.putNumber("Speed", speed);
        //SmartDashboard.putNumber("Current Angle", gyro.getAngle());
        SmartDashboard.putNumber("Battery Usage", driverstation.getBatteryVoltage());

        //Decide drive mode
        if (joyRight.buttonDown(2))//TURN DRIVE
        {
            //Rotate in place if button 2 is pressed
            drive.turnDrive(-speed);
        }
        else if (joyRight.buttonDown(5))//FIELD ALIGNED
        {
            // Drive field aligned if button 5 is pressed
            drive.crabDrive(joyRight.getStickAngle(), speed, true /*field aligned*/);
        }        
        else if (joyRight.buttonDown(3))//CAR DRIVE
        {
            //Car drive if button 3 is pressed.
            // Stick twist controls turning, and stick Y controls speed.
            drive.carDrive(joyRight.getTwist(), speed);
        }
        else//CRAB DRIVE
        {
            //Normally use crab drive
            drive.crabDrive(joyRight.getStickAngle(), speed, false/*not field aligned*/);
        }
        
        if (joyRight.buttonPressed(10))//SET ANGLE AS GYRO ZERO
        {
            gyroi2c.setCurrentAngleAsZero();
        }

        if ( CAMERA_ENABLED && joyRight.buttonPressed(11))//TOGGLE CAMERA
        {
            // Toggle camera if button 11 is pressed
            cam.toggleReading();
        }

        if (joyRight.buttonDown(10))
        {
            // Reset gyro if button 10 is pressed
            //gyro.reset();
        }

        if (joyRight.buttonPressed(8))
        {
            // Toggle LED is button 8 is pressed.
            //LED.toggle();
        }

        if (CAMERA_ENABLED)
        {
            // Print camera status to driver station
            driverstation.println((cam.isReading())
                    ? "Target detected: " + ((cam.isTargetDetected()) ? "Yes" : "No")
                    : "Camera is not reading.", 4);
        }
    }

    //this is a persistant variable to select the wheel to calibrate.
    int motorId = 0;
    /**
     * Update steering calibration control
     */
    private void updateCalibrateControl()
    {        
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