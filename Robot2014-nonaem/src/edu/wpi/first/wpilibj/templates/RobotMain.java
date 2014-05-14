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
    
    private static Feeder feeder;
    //private Camera467 cam;
    private Launcher launcher;
    private boolean enabledOnce = false;
    
    private Compressor467 comp;
    private GearToothSensor gts;
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
        feeder = Feeder.getInstance();
        drive = Drive.getInstance();
        comp = Compressor467.getInstance();
        launcher = Launcher.getInstance();
        //cam = Camera467.getInstance();
        gts = new GearToothSensor(4, RobotMap.WHEEL_CIRCUMFRENCE, RobotMap.TICKS_PER_WHEEL);
        
        //SpeedCalibration.init();
        //LED = LEDring.getInstance();

        // static static static static static
        Calibration.init();
    }

    public void disabledInit()
    {
        if (enabledOnce && CAMERA_ENABLED)
        {
            //cam.killThread();
        }
    }

    /**
     * This function is run when autonomous control mode is first enabled
     */
    public void autonomousInit()
    {
        Autonomous.init();
        //Read driverstation inputs
        driverstation.readInputs();
    }

    /**
     * This function is run when operator control mode is first enabled
     */
    public void teleopInit()
    {
        enabledOnce = true;
        if (CAMERA_ENABLED)
        {
            //cam = Camera467.getInstance();
            //cam.startThread();
        }
        comp = Compressor467.getInstance();
    }

    /**
     * This function is run when test mode is first enabled
     */
    public void testInit()
    {
        
         gts.reset();
         gts.start();
        
         //startTime = System.currentTimeMillis();
    }
    /**
     * This function is called periodically test mode
     */
    public void testPeriodic()
    {
        /*
        driverstation.readInputs();
        Joystick467 joy = driverstation.getNavJoystick();
//        if(joy.buttonDown(Joystick467.TRIGGER))
//        {            
//            drive.driveFeeder(joy.getStickY());
//        }
        
        comp.update();
        */
        
        gts.update();
        System.out.println("Ticks: " + gts.getTicks());
        
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
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic()
    {
        Autonomous.updateAutonomous();
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
        //Use driver's stick
        if (driverstation.getDriveJoystick().getFlap())
        {
            driverstation.println("Mode: Calibrate", 1);
            updateCalibrateControl();
        }
        else
        {
            driverstation.println("Mode: Drive", 1);
            updateDriveControl();
            updateNavigatorControl();
//            System.out.println("FL: " + drive.getSteering(RobotMap.FRONT_LEFT).getSensorValue());
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
        Joystick467 joyLeft = driverstation.getDriveJoystick();

        //Set speed
        if (joyLeft.buttonDown(2))//TURN IN PLACE
        {
            // Speed for turn in place
            speed = joyLeft.getTwist();
        }
        else if (joyLeft.buttonDown(3) || joyLeft.buttonDown(6))//CAR DRIVE
        {
            // Speed for car drive
            speed = joyLeft.getStickY();
        }        
        else//CRAB OR FIELD ALIGNED
        {
            // Speed for crab drive, field aligned or otherwise.
            speed = joyLeft.getStickDistance();
        }

        // Speed modifiers
        if (joyLeft.buttonDown(Joystick467.TRIGGER))//CREEP
        {
            // Creep on trigger
            speed /= 3.0;
        }
        else if (joyLeft.buttonDown(7))//TURBO
        {
            // Turbo on button 7
            speed *= 2.0;
        }

        SmartDashboard.putNumber("Speed", speed);
        //SmartDashboard.putNumber("Current Angle", gyro.getAngle());
        SmartDashboard.putNumber("Battery Usage", driverstation.getBatteryVoltage());

        //Decide drive mode
        if (joyLeft.buttonDown(2))//TURN DRIVE
        {
            //Rotate in place if button 2 is pressed
            drive.turnDrive(-speed);
        }
        else if (joyLeft.buttonDown(5))//FIELD ALIGNED
        {
            // Drive field aligned if button 5 is pressed
            drive.crabDrive(joyLeft.getStickAngle(), speed, true /*field aligned*/);
        }        
        else if (joyLeft.buttonDown(3))//CAR DRIVE
        {
            //Car drive if button 3 is pressed.
            // Stick twist controls turning, and stick Y controls speed.
            drive.carDrive(joyLeft.getTwist(), speed);
        }        
        else//CRAB DRIVE
        {
            //Normally use crab drive
            drive.crabDrive(joyLeft.getStickAngle(), speed, false/*not field aligned*/);
        }

        if ( CAMERA_ENABLED && joyLeft.buttonPressed(11))//TOGGLE CAMERA
        {
            // Toggle camera if button 11 is pressed
            //cam.toggleReading();
        }

        if (joyLeft.buttonDown(10))
        {
            // Reset gyro if button 10 is pressed
//            gyro.reset();
        }

        if (joyLeft.buttonPressed(8))
        {
            // Toggle LED is button 8 is pressed.
            //LED.toggle();
        }

        if (CAMERA_ENABLED)
        {
            // Print camera status to driver station
            /*
            driverstation.println((cam.isReading())
                    ? "Target detected: " + ((cam.isTargetDetected()) ? "Yes" : "No")
                    : "Camera is not reading.", 4);
            */
        }
    }

    //this is a persistant variable to select the wheel to calibrate.
    int motorId = 0;
    /**
     * Update steering calibration control
     */
    private void updateCalibrateControl()
    {        
        double stickAngle = driverstation.getDriveJoystick().getStickAngle();

        //Branch into motor being calibrated
        if (driverstation.getDriveJoystick().getStickDistance() > 0.5)
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
     * Update control of the Navigator
     */
    private void updateNavigatorControl()
    {
        Joystick467 joyNav = driverstation.getNavJoystick();
                
        //FIRE
        if (joyNav.getFlap())
        {
            launcher.fireLauncher();
            System.out.println("Fire launcher");
        }        
        else
        {
            launcher.pullBackLauncher();
            System.out.println("Pull back launcher");
        }
        
        //hat stick is backward
        if (joyNav.getHatY() < -0.5)
        {
            //intake ball
            feeder.driveFeederMotor(-1.0);
        }
        //hat stick is forward
        else if (joyNav.getHatY() > 0.5)
        {
            //spit out ball
            feeder.driveFeederMotor(1.0);
        }
        else
        {
            //dont feed
            feeder.driveFeederMotor(0);            
        }
        
        //sets arms down or up
        if (joyNav.getStickY() < -0.5)
        {
            feeder.lowerFeeder();
        }
        else
        {
            feeder.raiseFeeder();
        }
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
