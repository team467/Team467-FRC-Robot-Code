/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.shs.first.team467;

import edu.wpi.first.wpilibj.SmartDashboard;

/**
 *
 * @author USFIRST
 */
public class Autonomous2011 {

    //Object instance
    private static Autonomous2011 instance;

    //Robot objects
    private Camera2011 cam;
    private Camera2011.CamData camData;
    private Drive2011 drive;
    private Lifter2011 lifter;
    private LineFollow2011 lineFollow;

    //Autonomous state constants
    private final int STATE_LINE_FOLLOW_FAST = 0;
    private final int STATE_TARGET_TRACK_SLOW = 1;
    private final int STATE_PEG_STRAFE_AWAY = 2;
    private final int STATE_PEG_STRAFE_TOWARDS = 3;
    private final int STATE_PEG_STRAFE_CENTER = 4;
    private final int STATE_LIFT_TO_INVISIBLE = 5;
    private final int STATE_LIFT_TO_VISIBLE = 6;
    private final int STATE_FORWARD_SLOW = 7;
    private final int STATE_LOWER_LIFTER_PARTIAL = 8;
    private final int STATE_BACKWARD_SLOW = 9;
    private final int STATE_LOWER_LIFTER_FULL = 10;
    private final int STATE_STOPPED = 11;

    //Peg constants
    private final int PEG_LEFT = 1;
    private final int PEG_MIDDLE = 2;
    private final int PEG_RIGHT = 0;

    private final int PEG_HEIGHT_TOP = 1;
    private final int PEG_HEIGHT_MIDDLE = 2;
    private final int PEG_HEIGHT_BOTTOM = 3;

    //Autonomous state variable
    private int autonomousState = STATE_LINE_FOLLOW_FAST;

    //The height (in pixels) for the circle to be when the robot drives to it
    private final int DRIVE_TO_HEIGHT = 15;

    //Driverstation cancel button number
    private final int CANCEL_BUTTON_NUMBER = 10;

    //Singleton so private constructor
    private Autonomous2011()
    {
        //Make objects
        cam = Camera2011.getInstance();
        drive = Drive2011.getInstance();
        lifter = Lifter2011.getInstance();
        lineFollow = LineFollow2011.getInstance();
    }

     /**
     * Get the instance of the Team467Camera object
     * @return The instance
     */
    public static Autonomous2011 getInstance()
    {
        if (instance == null)
        {
            instance = new Autonomous2011();
        }
        return instance;
    }

    public void resetState()
    {
        autonomousState = STATE_LINE_FOLLOW_FAST;
        timerTicks = 0;
        targetPassed = false;
    }

    private int timerTicks = 0;

    /** 
     * Called by the main robot loop and handles the current autonomous routine
     */
    public void updateAutonomousControl()
    {
        cam.startCameraReading();
        //Read camera data
        camData = cam.getCamData();

        //Update middle peg placing autonomous
        updateAutonomousMiddlePeg();
//        switch (autonomousState)
//        {
//            case STATE_LINE_FOLLOW_FAST:
//                lineFollow.setSpeed(0.4);
//                lineFollow.update();
//                lifter.liftStage1();
//                if (camData.targetHeight >= 6 && camData.targetVisible)
//                {
//                    autonomousState = STATE_TARGET_TRACK_SLOW;
//                }
//                break;
//            case STATE_TARGET_TRACK_SLOW:
//                lifter.stopStage1();
//                drive.faDrive(camData.angleToTarget, 0.25, 0);
//                //lifter.setSlider(1.0);
//                if (camData.targetHeight >= 30 && camData.targetVisible)//lineFollow.endOfLine())
//                {
//                    autonomousState = STATE_STOPPED;
//                }
//                break;
//            case STATE_STOPPED:
//                drive.faDrive(0, 0, 0);
//                lifter.stopStage1();
//                break;
//        }
        
    }

    public void logCameraData()
    {
        SmartDashboard.log(camData.targetHeight, "Target Height");
        SmartDashboard.log(camData.targetVisible, "Target Visible");
        SmartDashboard.log(camData.targetWidth, "Target Width");
        SmartDashboard.log(camData.targetYPos, "Target XPos");
        SmartDashboard.log(camData.targetXPos, "Target YPos");

    }

    boolean targetPassed = false;

    private void updateAutonomousMiddlePeg()
    {
        switch (autonomousState)
        {
            case STATE_LINE_FOLLOW_FAST:
                if (timerTicks > 50)
                {
                    lineFollow.setSpeed(0.7);
                    lineFollow.update();
                }
                lifter.setSlider(1.0);
                // TODO - measure typical time ticks and STOP if exceeded
                if (camData.targetHeight >= 8 && camData.targetVisible)
                {
                    timerTicks = 0;
                    autonomousState = STATE_TARGET_TRACK_SLOW;
                }
                timerTicks += 1;
                break;
            case STATE_TARGET_TRACK_SLOW:
                // TODO - experiment with angleToTarget *1.5
                drive.faDrive(camData.angleToTarget, 0.25, 0);
                lifter.setSlider(1.0);
                // TODO - cam should always be visible - stop if not.
                if (camData.targetHeight >= 30 && camData.targetVisible)
                {
                    autonomousState = STATE_LIFT_TO_INVISIBLE;
                }
                break;
            case STATE_LIFT_TO_INVISIBLE:
                lifter.setSlider(0.0);
                drive.faDrive(0, 0, 0);
                if (camData.targetVisible)
                {
                    lifter.liftStage1();
                }
                else
                {
                    autonomousState = STATE_LIFT_TO_VISIBLE;
                }
                break;
            case STATE_LIFT_TO_VISIBLE:
                // TODO - add timeout to stop lifter if don't see target after time
                if (!camData.targetVisible || camData.targetHeight < 20.0 || camData.targetYPos > 0)
                {
                    lifter.liftStage1();
                }
                else
                {
                    autonomousState = STATE_FORWARD_SLOW;
                }
                break;
            case STATE_FORWARD_SLOW:
                lifter.stopStage1();
                drive.faDrive(0, 0.25, 0);
                if (timerTicks == 20)
                {
                    timerTicks = 0;
                    autonomousState = STATE_LOWER_LIFTER_PARTIAL;
                }
                timerTicks += 1;
                break;
            case STATE_LOWER_LIFTER_PARTIAL:
                if (timerTicks < 24)
                {
                    lifter.lowerStage1();
                }
                else
                {
                    timerTicks = 0;
                    autonomousState = STATE_BACKWARD_SLOW;
                }
                timerTicks += 1;
                break;
            case STATE_BACKWARD_SLOW:
                lifter.stopStage1();
                drive.faDrive(0, -0.25, 0);
                if (timerTicks == 70)
                {
                    autonomousState = STATE_STOPPED;
                }
                timerTicks += 1;
                break;
            case STATE_STOPPED:
                drive.faDrive(0, 0, 0);
                lifter.stopStage1();
                break;
        }
    }

    private void updateAutonomousTopPeg()
    {
        switch (autonomousState)
        {
            case STATE_LINE_FOLLOW_FAST:
                lineFollow.setSpeed(0.7);
                lineFollow.update();
                //lifter.setSlider(1.0);
                if (camData.targetHeight >= 8 && camData.targetVisible)//lineFollow.endOfLine())
                {
                    autonomousState = STATE_TARGET_TRACK_SLOW;
                }
                break;
            case STATE_TARGET_TRACK_SLOW:
                drive.faDrive(camData.angleToTarget, 0.25, 0);
                //lifter.setSlider(1.0);
                if (camData.targetHeight >= 27 && camData.targetVisible)//lineFollow.endOfLine())
                {
                    autonomousState = STATE_LIFT_TO_INVISIBLE;
                }
                break;
            case STATE_LIFT_TO_INVISIBLE:
                drive.faDrive(0, 0, 0);
                if (camData.targetVisible)
                {
                    lifter.liftStage1();
                }
                else
                {
                    autonomousState = STATE_LIFT_TO_VISIBLE;
                }
                break;
            case STATE_LIFT_TO_VISIBLE:
                if (!camData.targetVisible || camData.targetHeight < 21.0 || camData.targetYPos > 2)
                {
                    lifter.liftStage1();
                }
                else
                {
                    if (targetPassed)
                    {
                        autonomousState = STATE_FORWARD_SLOW;
                    }
                    else
                    {
                        targetPassed = true;
                        autonomousState = STATE_LIFT_TO_INVISIBLE;
                    }
                }
                break;
            case STATE_FORWARD_SLOW:
                lifter.stopStage1();
                drive.faDrive(0, 0.25, 0);
                if (timerTicks == 29)
                {
                    timerTicks = 0;
                    autonomousState = STATE_LOWER_LIFTER_PARTIAL;
                }
                timerTicks += 1;
                break;
            case STATE_LOWER_LIFTER_PARTIAL:
                if (timerTicks < 15)
                {
                    lifter.lowerStage1();
                }
                else
                {
                    timerTicks = 0;
                    autonomousState = STATE_BACKWARD_SLOW;
                }
                timerTicks += 1;
                break;
            case STATE_BACKWARD_SLOW:
                lifter.stopStage1();
                drive.faDrive(0, -0.25, 0);
                if (timerTicks == 70)
                {
                    autonomousState = STATE_STOPPED;
                }
                timerTicks += 1;
                break;
            case STATE_STOPPED:
                drive.faDrive(0, 0, 0);
                lifter.stopStage1();
                break;
        }
    }

    public void setLed(boolean b)
    {
        cam.setLED(b);
    }

    private int pegDirection = 0;
    private int pegHeight = 0;

    /**
     * 
     */
    public void updateTeleopNavigatorControl(int pegNumber)
    {
        //Set camera reading flag to true
        cam.startCameraReading();
        
        //If cancel button is pressed reset state and don't do autonomous code
        if (pegNumber != CANCEL_BUTTON_NUMBER)
        {
            //Read camera data
            camData = cam.getCamData();

            //Set peg direction and height
            pegDirection = pegNumber % 3;
            pegHeight = (int) Math.ceil(pegNumber / 3.0);

            //Branch by autonomous state
            switch (autonomousState)
            {
                case STATE_LINE_FOLLOW_FAST:
                    lineFollow.setSpeed(0.4);
                    lineFollow.update();
                    if (camData.targetHeight >= DRIVE_TO_HEIGHT)
                    {
                        autonomousState = STATE_PEG_STRAFE_AWAY;
                    }
                    break;
                case STATE_PEG_STRAFE_AWAY:
                    if (driveUntilPegNotVisible(pegDirection))
                    {
                        autonomousState = STATE_PEG_STRAFE_TOWARDS;
                    }
                    break;
                case STATE_PEG_STRAFE_TOWARDS:
                    if (driveUntilPegVisible(pegDirection))
                    {
                        autonomousState = STATE_PEG_STRAFE_CENTER;
                    }
                    break;
                case STATE_PEG_STRAFE_CENTER:
                    if (driveUntilPegCentered())
                    {
                        autonomousState = STATE_STOPPED;
                        //Continue autonmous code here
                    }
                    break;
                case STATE_STOPPED:
                    drive.faDrive(0, 0, 0);
                    break;
            }
        }
        else
        {
            //Reset state if cancel button is pressed
            autonomousState = STATE_LINE_FOLLOW_FAST;
        }
    }

    /**
     * Drive until a particle is not visible
     * @param direction the direction to drive in 
     * @return Whether or not the circle is visible
     */
    private boolean driveUntilPegNotVisible(int direction)
    {
        //Look for particles greater than 15 pixels in height when closer
        cam.setMimimumTargetHeight(12.0);

        switch (direction)
        {
            case PEG_LEFT:
                if (camData.targetVisible)
                {
                    drive.faDrive(-0.5, -0.3, 0);
                }
                else
                {
                    return true;
                }
                break;
             case PEG_RIGHT:
                 if (camData.targetVisible)
                 {
                     drive.faDrive(0.5, -0.3, 0);
                 }
                 else
                 {
                     return true;
                 }
                 break;
            case PEG_MIDDLE:
                return true;
        }
        return false;
    }

    /**
     * Drives until a peg is visible in the specified direction
     * @param direction The direction to look for a peg from
     * @return Whether or not the peg is visible on the specified side
     */
    private boolean driveUntilPegVisible(int direction)
    {
        switch (direction)
        {
            case PEG_LEFT:
                if (camData.targetVisible && camData.targetXPos < 10.0)
                {
                    return true;
                }
                else
                {
                    drive.faDrive(-1, -0.3, 0);
                }
                break;
            case PEG_RIGHT:
                if (camData.targetVisible && camData.targetXPos > -10.0)
                {
                    return true;
                }
                else
                {
                    drive.faDrive(-1, -0.3, 0);
                }
                break;
            case PEG_MIDDLE:
                return true;
        }
        return false;
    }

    /**
     * Drives until the (presumably visible) peg is centered within 5 pixels in either
     * direction.
     */
    private boolean driveUntilPegCentered()
    {    
        if (camData.targetVisible)
        {
            if (camData.targetXPos > 5)
            {
                drive.faDrive(0.5, 0.2, 0);
            }
            else if (camData.targetXPos < -5)
            {
                drive.faDrive(-0.5, 0.2, 0);
            }
            else
            {
                drive.faDrive(0, 0, 0);
                return true;
            }
        }
        else
        {
            drive.faDrive(0, 0, 0);
        }
        return false;

    }

}
