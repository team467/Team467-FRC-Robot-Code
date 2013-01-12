/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.shs.first.team467;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.camera.AxisCamera;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotMain2011 extends IterativeRobot
{
    //Robot objects
    private AxisCamera cam;
    private Driverstation2011 driverstation;
    private Drive2011 drive;
    private Lifter2011 lifter;

    //Autonomous control objects. All autonomous code is handled whithin this class
    //and should be called by .updateTeleopNavigatorControl or .updateAutonomousControl
    private Autonomous2011 autonomous;

    //Teleoperated state constants
    private final int TELEOP_STATE_DRIVE = 0;
    private final int TELEOP_STATE_LINE_FOLLOW = 1;

    //Teleoperated state variable
    private int teleopState = TELEOP_STATE_DRIVE;

    //Navigator control constants
    private final int RAISE_LIFTER_STAGE1_BUTTON = 1;
    private final int LOWER_LIFTER_STAGE1_BUTTON = 7;
    private final int RAISE_LIFTER_STAGE2_BUTTON = 3;
    private final int LOWER_LIFTER_STAGE2_BUTTON = 9;
    private final int SLIDE_LIFTER_FORWARD_BUTTON = 2;
    private final int SLIDE_LIFTER_BACKWARD_BUTTON = 8;

    //Angle used for centering wheels
    double calibrationAngle = 0.0;

    //Orientation of robot
    double orientation;

    //Drive speed
    double speed = 0.0;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit()
    {
        //Make robot objects
        drive = Drive2011.getInstance();
        driverstation = Driverstation2011.GetInstance();
        cam = AxisCamera.getInstance();
        lifter = Lifter2011.getInstance();

        //Make autonomous control
        autonomous = Autonomous2011.getInstance();

        //Initialize smart dashboard for debugging
        SmartDashboard.init();
    }

    /**
     * Called when the robot is disabled
     */
    public void disabledInit()
    {
        System.out.println("Default IterativeRobot.disabledInit() method... Overload me!");
        lifter.stopStage1();
        lifter.stopStage2();
        drive.faDrive(0, 0, 0);
    }

    public void autonomousInit()
    {
        Gyro2011.getInstance().reset();
        autonomous.resetState();
    }


    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic()
    {
        autonomous.updateAutonomousControl();
    }

    // TODO - refactor code, this should be a local variable to the functions
    // where it is used
    double stickAngle = 0.0;

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic()
    {
        //Read driverstation joystick inputs
        driverstation.ReadInputs();

        //Read joystick angle
        stickAngle = driverstation.getStickAngle(driverstation.JoystickX, driverstation.JoystickY);

        //Rotate angle 180 if button 11 is pressed
        if (!driverstation.JoystickButton11)
        {
            stickAngle += 1.0;
            if (stickAngle > 1.0)
            {
                stickAngle -= 2.0;
            }
        }

        //Use button 9 to turn on the LED
        autonomous.setLed(driverstation.JoystickButton9);

        //Get the speed to drive at (determined by stick distance from center)
        speed = driverstation.getStickDistance(driverstation.JoystickX, driverstation.JoystickY);

        //Use turbo if trigger is pressed
        if (!driverstation.JoystickTrigger)
        {
            speed /= 2;
        }

        //Update driving only if navigator automatic control is off
        if (driverstation.ManualControlSwitch)
        {
            updateDrive();
        }
    }

    /**
     * Update the driving state of the robot (use joystick to move)
     */
    private void updateDrive()
    {
        //Updates any control the navigator has (lifter movement)
        updateNavigatorControl();

        //Lock orientation of robot when joystick is at 0 poistion
        if ((driverstation.JoystickX == 0.0)
            && (driverstation.JoystickY == 0.0))
        {
            orientation = Gyro2011.getInstance().getAngle();
        }

        //Determine if calibrating wheel center position
        if (driverstation.JoystickCalibrate)
        {
            //Increase wheel angle by a small amount based on joystick twist
            calibrationAngle += driverstation.JoystickTwist / 100.0;

            //Drive with no speed to allow only steering
            drive.faDrive(calibrationAngle, 0, 0);
            // print useful Drive information to the Smart Dashboard
            drive.logDrive();
        }
        else
        {
            //Determine if joytick is twisted
            if (((driverstation.JoystickTwist > 0.1) || (driverstation.JoystickTwist < -0.1)))
            {
                //Use car drive if button 2 is pressed
                if (driverstation.JoystickButton2)
                {
                    //Drive forward or backward based on joystick y position
                    if (driverstation.JoystickY < 0.0)
                    {
                        drive.carDrive(driverstation.JoystickTwist, speed);
                    }
                    else
                    {
                        drive.carDrive(driverstation.JoystickTwist, -speed);
                    }
                }
                else
                {
                    // TODO - remove this else clause by refactoring conditionals above
                    //Normal field aligned drive
                    drive.faDrive(stickAngle, speed, orientation);
                }
            }
            else
            {
                //Use field aligned drive
                drive.faDrive(stickAngle, speed, orientation);
            }
        }

        //Use a button to reset the gyro
        if (driverstation.JoystickButton7)
        {
            Gyro2011.getInstance().reset();
        }

    }

    /**
     * Called periodically to update any navigator control
     */
    private void updateNavigatorControl()
    {
        //Branch into manual or automatic control 
        if (driverstation.ManualControlSwitch)
        {
            //Slide lifter
            if (driverstation.getButtonStatus(SLIDE_LIFTER_FORWARD_BUTTON))
            {
                lifter.setSlider(1.0);
            }
            else if (driverstation.getButtonStatus(SLIDE_LIFTER_BACKWARD_BUTTON))
            {
                lifter.setSlider(-1.0);
            }
            else
            {
                lifter.setSlider(0.0);
            }

            //Move lifter first stage
            if (driverstation.getButtonStatus(RAISE_LIFTER_STAGE1_BUTTON))
            {
                lifter.liftStage1();
            }
            else if (driverstation.getButtonStatus(LOWER_LIFTER_STAGE1_BUTTON))
            {
                lifter.lowerStage1();
            }
            else
            {
                lifter.stopStage1();
            }

            //Move lifter second stage
            if (driverstation.getButtonStatus(RAISE_LIFTER_STAGE2_BUTTON))
            {
                lifter.liftStage2();
            }
            else if (driverstation.getButtonStatus(LOWER_LIFTER_STAGE2_BUTTON))
            {
                lifter.lowerStage2();
            }
            else
            {
                lifter.stopStage2();
            }
        }
        else
        {
            int lastPressedButton = 10;
            for(int i = 1; i < 11; i ++)
            {
                if (driverstation.getButtonStatus(i))
                {
                    lastPressedButton = i;
                }
            }
            //autonomous.updateTeleopNavigatorControl(lastPressedButton);
        }
        
    }
}
