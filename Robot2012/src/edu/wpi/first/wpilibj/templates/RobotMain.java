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
public class RobotMain extends IterativeRobot {
    
    //Robot objects
    private Driverstation driverstation;
    private Drive drive;
    private Gyro467 gyro;
    private PIDAlignment alignDrive;
    private Llamahead llamahead;
    private PneumaticArm arm;
    private Compressor467 compressor;
    
    //Debounce of joystick button so staring the wheel calibration is only called
    //once
    private boolean button4Debounce = true;
    
    private boolean triggerDebounce = true;
    
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
        arm = PneumaticArm.getInstance();
        compressor = Compressor467.getInstance();
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
        //Read driverstation inputs
        driverstation.readInputs();
        
        if (!driverstation.autonomousOnSwitch)
        {
            driverstation.println("Autonomous Enabled", 1);
            Autonomous.updateAutonomous(driverstation.autonomousModeSwitch);
        }
        else
        {
            driverstation.println("Autonomous Disabled", 1);
        }
        
        //Print autonomous mode to driverstation
        switch (driverstation.autonomousModeSwitch)
        {
            case Autonomous.MODE_FRONT_KEY:
                //Front key mode
                driverstation.println("Mode: Front Key" , 2);
                break;
            case Autonomous.MODE_BACK_KEY:
                //Back key mode
                driverstation.println("Mode: Back Key" , 2);
                break;
            case Autonomous.MODE_FULL:
                //Front key mode
                driverstation.println("Mode: Full" , 2);
                break;
        }
        
        //Reload compressor
        compressor.update();
        
        //Send data to the driverstation
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
        if (driverstation.joystickCalibrate)
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
                
        //Gyro reset at button 7
        if (driverstation.joystickButton7)
        {
            gyro.reset();
        }
        
        //Compressor reloading
        compressor.update();
        
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
            if (driverstation.joystickTrigger)
            {
                speed /= 3.0;
            }
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
                    speed, true);
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
            Calibration.toggleWheelCalibrate();
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
    
    private double launchSpeed = 0.0;
    
    /**
     * Update control of the llamahead (launcher)
     */
    private void updateNavigatorControl()
    {   
        //NOTE: The driverstation variables scoopSwitch, advanceSwitch, and armSwitch
        //correspond to constants that are the same between the llamahead, driverstation,
        //and pneumaticArm. The constants go in the order FORWARD/UP = 1, REVERSE/DOWN = 2,
        //and STOP/MIDDLE = 3. This means that they can be directly set to the driverstation
        //variables for the 3 way switches
        
        //Determine launch speed
        switch (driverstation.autonomousModeSwitch)
        {
            case Driverstation.SWITCH_UP:
                launchSpeed = 55.0;
                break;
            case Driverstation.SWITCH_DOWN:
                launchSpeed = Llamahead.SPEED_FRONT_KEY; 
                break;
            case Driverstation.SWITCH_MIDDLE:
                launchSpeed = Llamahead.SPEED_FRONT_KEY;
                break;
        }
        
        //Ball pickup
        llamahead.setBallIntake(driverstation.scoopSwitch);
        
        //Ball advance
        if (llamahead.atSpeed() && driverstation.neckSwitch == Driverstation.SWITCH_UP
                && driverstation.launchButton)
        {
            llamahead.setNeckAdvance(Llamahead.LAUNCH);
        }
        else
        {
            llamahead.setNeckAdvance(driverstation.neckSwitch);
        }
        
        //Arm movement
        if (driverstation.armSwitch == Driverstation.SWITCH_UP)
        {
            arm.moveArm(PneumaticArm.ARM_DOWN);
        }
        if (driverstation.armSwitch == Driverstation.SWITCH_DOWN)
        {
            arm.moveArm(PneumaticArm.ARM_UP);
        }
        
        //Launching
        if (driverstation.launchButton)
        {
            //Drive launcher wheel
            llamahead.setLauncherWheel(launchSpeed);
            
            //Turn on led if llamahead is at speed
            driverstation.setLaunchLed(llamahead.atSpeed());
            
            triggerDebounce = true;
        }
        else if (triggerDebounce)
        {
            llamahead.stopLauncherWheel();
            triggerDebounce = false;
        }
        else
        {
            //Keep launcher wheel at 0 speed
            llamahead.setLauncherWheel(0.0);
            
            //Turn off led when launch button isn't pressed
            driverstation.setLaunchLed(false);
        }
        
        //Print launch speed
        driverstation.println("Launch Speed: " + llamahead.getLauncherSpeed(), 3);
    }
    
    /**
     * Update control of the llamahead (launcher) using buttons on the joystick
     * for testing purposes
     */
    private void updateJoystickNavigatorControl()
    {   
        //NOTE: The driverstation variables scoopSwitch, advanceSwitch, and armSwitch
        //correspond to constants that are the same between the llamahead, driverstation,
        //and pneumaticArm. The constants go in the order FORWARD/UP = 1, REVERSE/DOWN = 2,
        //and STOP/MIDDLE = 3. This means that they can be directly set to the driverstation
        //variables for the 3 way switches
        
        //Ball pickup
        if (driverstation.joystickButton5)
        {
            llamahead.setBallIntake(Llamahead.FORWARD);
        }
        else if (driverstation.joystickButton3)
        {
            llamahead.setBallIntake(Llamahead.BACKWARD);
        }
        else 
        {
            llamahead.setBallIntake(Llamahead.STOP);
        }
        
        //Ball advance
        if (driverstation.joystickButton6)
        {
            llamahead.setNeckAdvance(Llamahead.FORWARD);
        }
        else if (driverstation.joystickButton4)
        {
            llamahead.setNeckAdvance(Llamahead.BACKWARD);
        }
        else if (driverstation.joystickButton11)
        {
            llamahead.setNeckAdvance(Llamahead.LAUNCH);
        }
        else 
        {
            llamahead.setNeckAdvance(Llamahead.STOP);
        }
        
        //Arm movement
        if (driverstation.joystickButton12)
        {
            arm.moveArm(PneumaticArm.ARM_UP);
        }
        else
        {
            arm.moveArm(PneumaticArm.ARM_DOWN);
        }
        
        //Compressor reloading
        compressor.update();
        
        //Launching
        if (driverstation.joystickButton9)
        {
            //llamahead.launch(TEMP_LAUNCH_SPEED);
            llamahead.driveLaunchMotor(1.0);
            triggerDebounce = true;
        }
        else if (triggerDebounce)
        {
            llamahead.stopLauncherWheel();
            triggerDebounce = false;
        }
        else
        {
            llamahead.driveLaunchMotor(0.0);
        }
        
        //Turn on led if llamahead is at speed
        driverstation.setLaunchLed(llamahead.atSpeed());
        
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
