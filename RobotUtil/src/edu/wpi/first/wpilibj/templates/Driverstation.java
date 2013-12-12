package edu.wpi.first.wpilibj.templates;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationEnhancedIO;
import edu.wpi.first.wpilibj.DriverStationEnhancedIO.EnhancedIOException;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.DriverStationLCD.Line;
import edu.wpi.first.wpilibj.Joystick;


/**
 * Singleton class to handle driverstation I/O on Team467 2010 Robot
 * @author aidan
 */
public class Driverstation
{
    //Singleton instance variable
    private static Driverstation instance;

    //Driverstation objects
    private DriverStation driverstation;
//    private DriverStationEnhancedIO driverstationEnhanced;
    private DriverStationLCD lcd;
    
    public static final int SWITCH_UP = 0;
    public static final int SWITCH_DOWN = 1;
    public static final int SWITCH_MIDDLE = 2;
    
    //Joystick button constants
    private static final int J_TRIGGER = 1;
    private static final int J_BUTTON_2 = 2;
    private static final int J_BUTTON_3 = 3;
    private static final int J_BUTTON_4 = 4;
    private static final int J_BUTTON_5 = 5;
    private static final int J_BUTTON_6 = 6;
    private static final int J_BUTTON_7 = 7;
    private static final int J_BUTTON_8 = 8;
    private static final int J_BUTTON_9 = 9;
    private static final int J_BUTTON_10 = 10;
    private static final int J_BUTTON_11 = 11;
    private static final int J_BUTTON_12 = 12;

    //Joystick axis constants
    private static final int AXIS_X = 1;
    private static final int AXIS_Y = 2;
    private static final int TWIST = 3;
    private static final int CALIBRATE = 4;
    private static final int SMALL_AXIS_X = 5;
    private static final int SMALL_AXIS_Y = 6;

    //Joystick deadzone constant
    private static final double JOYSTICK_DEADZONE = 0.1;
    
    //Driverstation enhanced IO button constant
    private static final int BUTTON_LAUNCH = 1; //TBD

    //DriverStation enhanced IO switches constants
    private static final int SWITCH_ARM_UP = 4;
    private static final int SWITCH_ARM_DOWN = 5;
    private static final int SWITCH_SCOOP_UP = 6;
    private static final int SWITCH_SCOOP_DOWN = 7;
    private static final int SWITCH_NECK_ADVANCE_UP = 2;
    private static final int SWITCH_NECK_ADVANCE_DOWN = 3;
    private static final int SWITCH_AUTONOMOUS_UP = 9;
    private static final int SWITCH_AUTONOMOUS_DOWN = 10; 
    private static final int SWITCH_AUTONOMOUS_ON = 8;
    
    //Digital Output channel constants
    private static final int LAUNCH_LED = 16; 

    private static final int AUTONOMOUS_SWITCH_LED = 15;

    
    //Joystick objects
    private Joystick joystickDrive;
    private Joystick joystickNav;

    //Public joystick button objects
    public boolean joystickDriveTrigger = false;
    public boolean joystickDriveButton2 = false;
    public boolean joystickDriveButton3 = false;
    public boolean joystickDriveButton4 = false;
    public boolean joystickDriveButton5 = false;
    public boolean joystickDriveButton6 = false;
    public boolean joystickDriveButton7 = false;
    public boolean joystickDriveButton8 = false;
    public boolean joystickDriveButton9 = false;
    public boolean joystickDriveButton10 = false;
    public boolean joystickDriveButton11 = false;
    public boolean joystickDriveButton12 = false;
    public boolean joystickDriveCalibrate = false;

    //Public joystick axis objects
    public double joystickYDrive = 0.0;
    public double joystickXDrive = 0.0;
    public double joystickTwistDrive = 0.0;
    public double smallJoystickXDrive = 0.0;
    public double smallJoystickYDrive = 0.0;
    
    //Public joystick button objects
    public boolean joystickNavTrigger = false;
    public boolean joystickNavButton2 = false;
    public boolean joystickNavButton3 = false;
    public boolean joystickNavButton4 = false;
    public boolean joystickNavButton5 = false;
    public boolean joystickNavButton6 = false;
    public boolean joystickNavButton7 = false;
    public boolean joystickNavButton8 = false;
    public boolean joystickNavButton9 = false;
    public boolean joystickNavButton10 = false;
    public boolean joystickNavButton11 = false;
    public boolean joystickNavButton12 = false;
    public boolean joystickNavCalibrate = false;

    //Public joystick axis objects
    public double joystickYNav = 0.0;
    public double joystickXNav = 0.0;
    public double joystickTwistNav = 0.0;
    public double smallJoystickXNav = 0.0;
    public double smallJoystickYNav = 0.0;
    
    //Digital inputs
    public boolean launchButton = false;    
    public boolean autonomousOnSwitch = false;
    
    //Blank line to append to driverstation printouts so no previous text can be seen
    private static final String BLANK_LINE = "                              ";

    //Singleton so constructor is private
    private Driverstation()
    {
        driverstation = DriverStation.getInstance();
        lcd = DriverStationLCD.getInstance();
        joystickDrive = new Joystick(1);
        joystickNav = new Joystick(2);
//        driverstationEnhanced = driverstation.getEnhancedIO();
    }

    /**
     * Returns the single instance of this class
     * @return
     */
    public static Driverstation getInstance()
    {
	if (instance == null)
        {
            instance = new Driverstation();
	}
	return instance;
    }

    /**
     * Calculate the distance of the stick from the center position, preserving the sign of the Y
     * component
     * @param stickX
     * @param stickY
     * @return
     */
    public double getStickDistance(double stickX, double stickY)
    {
        double val = Math.sqrt(stickX * stickX + stickY * stickY);
        return val;
    }

    /**
     * Calculate the angle of a joystick, given a specific x and y input value.
     * @param stickX - X parameter - in the range -1.0 to 1.0
     * @param stickY - Y parameter - in the range -1.0 to 1.0 
     * @return Joystick Angle in range -1.0 to 1.0
     */
    public double getStickAngle(double stickX, double stickY)
    {
        // This shouldn't be necessary, deadzone filtering should already
        // be performed - however it doesn't hurt to make sure.
        if ((Math.abs(stickX) < JOYSTICK_DEADZONE) && (Math.abs(stickY) < JOYSTICK_DEADZONE))
        {
            return 0.0;
        }

        if (stickY == 0.0)
        {
            // In Y deadzone avoid divide by zero error
            // Note - may not be needed. Java handles this automatically
            // Need to test if the cRIO Java also handles it correctly
            if (stickX > 0.0)
            {
                return 0.5;
            }
            else
            {
                return -0.5;
            }
        }

        // Return value in range -1.0 to 1.0

        double stickAngle = MathUtils.atan(stickX / -stickY);

        if (stickY > 0)
        {
            if (stickX > 0)
            {
                stickAngle += Math.PI;
            }
            else
            {
                stickAngle -= Math.PI;
            }
        }

        return (stickAngle / (Math.PI));
    }

    /**
     * Implement a dead zone for joystick centering - and a non-linear
     * acceleration as the user moves away from the zero position.
     * @param input
     * @return
     */
    private double filterJoystickInput(double input)
    {
        // Ensure that there is a dead zone around zero
        if (Math.abs(input) < JOYSTICK_DEADZONE)
        {
            return 0.0;
        }
        // Simply square the input to provide acceleration
        // ensuring that the sign of the input is preserved
        return (input * Math.abs(input));
    }

    /**
     * returns status of specified Joystick buttons
     * @param button
     * @return
     */
    private boolean buttonStatus(Joystick j, int button)
    {
        return j.getRawButton(button);
    }
    
    /**
     * Sets the launcher led to on or off
     * @param b Value to set the digital output to: true = on, false = off
     */
    public void setLaunchLed(boolean b)
    {
//        try
//        {
//            driverstationEnhanced.setDigitalOutput(LAUNCH_LED, b);
//        }
//        catch (EnhancedIOException ex)
//        {
//            ex.printStackTrace();
//        }
    }

    /**
     * Read all Robot Inputs. Typically, this is called once per iteration of the main
     * event loop. Any soft filtering of inputs to remove noise or implement
     * non-linear acceleration is also done here.
     */
    public void readInputs()
    {
        //Read joystick buttons
        joystickDriveTrigger = buttonStatus(joystickDrive, J_TRIGGER);
        joystickDriveButton2 = buttonStatus(joystickDrive, J_BUTTON_2);
        joystickDriveButton3 = buttonStatus(joystickDrive, J_BUTTON_3);
        joystickDriveButton4 = buttonStatus(joystickDrive, J_BUTTON_4);
        joystickDriveButton5 = buttonStatus(joystickDrive, J_BUTTON_5);
        joystickDriveButton6 = buttonStatus(joystickDrive, J_BUTTON_6);
        joystickDriveButton7 = buttonStatus(joystickDrive, J_BUTTON_7);
        joystickDriveButton8 = buttonStatus(joystickDrive, J_BUTTON_8);
        joystickDriveButton9 = buttonStatus(joystickDrive, J_BUTTON_9);
        joystickDriveButton10 = buttonStatus(joystickDrive, J_BUTTON_10);
        joystickDriveButton11 = buttonStatus(joystickDrive, J_BUTTON_11);
        joystickDriveButton12 = buttonStatus(joystickDrive, J_BUTTON_12);
        
        joystickNavTrigger = buttonStatus(joystickNav, J_TRIGGER);
        joystickNavButton2 = buttonStatus(joystickNav, J_BUTTON_2);
        joystickNavButton3 = buttonStatus(joystickNav, J_BUTTON_3);
        joystickNavButton4 = buttonStatus(joystickNav, J_BUTTON_4);
        joystickNavButton5 = buttonStatus(joystickNav, J_BUTTON_5);
        joystickNavButton6 = buttonStatus(joystickNav, J_BUTTON_6);
        joystickNavButton7 = buttonStatus(joystickNav, J_BUTTON_7);
        joystickNavButton8 = buttonStatus(joystickNav, J_BUTTON_8);
        joystickNavButton9 = buttonStatus(joystickNav, J_BUTTON_9);
        joystickNavButton10 = buttonStatus(joystickNav, J_BUTTON_10);
        joystickNavButton11 = buttonStatus(joystickNav, J_BUTTON_11);
        joystickNavButton12 = buttonStatus(joystickNav, J_BUTTON_12);

        //Read joystick axes
        joystickDriveCalibrate = joystickDrive.getRawAxis(CALIBRATE) < 0.0;
        joystickYDrive = filterJoystickInput(joystickDrive.getRawAxis(AXIS_Y));
        joystickXDrive = filterJoystickInput(joystickDrive.getRawAxis(AXIS_X));
        joystickTwistDrive = filterJoystickInput(joystickDrive.getRawAxis(TWIST));
        smallJoystickXDrive = joystickDrive.getRawAxis(SMALL_AXIS_X);
        smallJoystickYDrive = joystickDrive.getRawAxis(SMALL_AXIS_Y);
        
        //Read all digital inputs
//        try
//        {
            //Determine arm switch state

//            
//            //Determine scoop switch state
//            if (!driverstationEnhanced.getDigital(SWITCH_SCOOP_UP))
//            {
//                scoopSwitch = Llamahead.BACKWARD;
//            }
//            else if (!driverstationEnhanced.getDigital(SWITCH_SCOOP_DOWN))
//            {
//                scoopSwitch = Llamahead.FORWARD;
//            }
//            else
//            {
//                scoopSwitch = Llamahead.STOP;
//            }
                        
            
//            //Determine autonomous mode switch state
//            if (!driverstationEnhanced.getDigital(SWITCH_AUTONOMOUS_UP))
//            {
//                autonomousModeSwitch = SWITCH_UP;
//            }
//            else if (!driverstationEnhanced.getDigital(SWITCH_AUTONOMOUS_DOWN))
//            {
//                autonomousModeSwitch = SWITCH_DOWN;
//            }
//            else
//            {
//                autonomousModeSwitch = SWITCH_MIDDLE;
//            }
            
            
//            //Read autonomous on/off switch
//            autonomousOnSwitch = !driverstationEnhanced.getDigital(SWITCH_AUTONOMOUS_ON);
            
            //Read launch button
            launchButton = joystickNav.getRawButton(J_TRIGGER);
            
//        }
//        catch (EnhancedIOException ex)
//        {
//            ex.printStackTrace();
//        }
    }
    
    /**
     * Prints the specified text to the string buffer
     * @param text The text to print
     * @param line The line number to print to (1-6)
     */
    public void println(String text, int line)
    {
        printFinal(text, line);
    }
    
    /**
     * Prints the specified text to the string buffer
     * @param text The text to print
     * @param line The line number to print to (1-6)
     */
    public void println(int text, int line)
    {
        printFinal(Integer.toString(text), line);
    }
    
    /**
     * Prints the specified text to the string buffer
     * @param text The text to print
     * @param line The line number to print to (1-6)
     */
    public void println(double text, int line)
    {
        printFinal(Double.toString(text), line);
    }
    
    /**
     * Private print function to determine which line to print to
     * (gets called by all println functions for the driverstation)
     * @param text
     * @param lineNum 
     */
    private void printFinal(String text, int lineNum)
    {
        //Determine Line based on given integer
        Line line = null;
        switch(lineNum)
        {
            case 1:
                line = Line.kUser1;
                break;
            case 2:
                line = Line.kUser2;
                break;
            case 3:
                line = Line.kUser3;
                break;
            case 4:
                line = Line.kUser4;
                break;
            case 5:
                line = Line.kUser5;
                break;
            case 6:
                line = Line.kUser6;
                break;
        }
        
        //Return if line number is invalid
        if (line == null)
        {
            return;
        }
        
        //Print to string buffer
        lcd.println(line, 1, text + BLANK_LINE);
    }
    
    /**
     * Send the string buffer to the driverstation
     */
    public void sendData()
    {
        lcd.updateLCD();
    }

}
