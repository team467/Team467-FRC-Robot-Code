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
    private DriverStationEnhancedIO driverstationEnhanced;
    private DriverStationLCD lcd;
    
    //Get single instance of camera to enable camera display on driverstation
    //private AxisCamera cam = AxisCamera.getInstance();
    
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
    private static final int BUTTON_LAUNCH = 8; //TBD

    //DriverStation enhanced IO switches constants
    private static final int SWITCH_ARM_UP = 14;
    private static final int SWITCH_ARM_DOWN = 10;
    private static final int SWITCH_SCOOP_UP = 5;
    private static final int SWITCH_SCOOP_DOWN = 1;
    private static final int SWITCH_NECK_ADVANCE_UP = 6;
    private static final int SWITCH_NECK_ADVANCE_DOWN = 2;
    private static final int SWITCH_AUTONOMOUS_UP = 11;
    private static final int SWITCH_AUTONOMOUS_DOWN = 9; 
    private static final int SWITCH_AUTONOMOUS_ON = 7;
    
    //Digital Output channel constants
    private static final int LAUNCH_LED = 13; 
    private static final int AUTONOMOUS_SWITCH_LED = 15;
    
    //Joystick objects
    private Joystick joystick;
    private Joystick joystickTemp;

    //Public joystick button objects
    public boolean joystickTrigger = false;
    public boolean joystickButton2 = false;
    public boolean joystickButton3 = false;
    public boolean joystickButton4 = false;
    public boolean joystickButton5 = false;
    public boolean joystickButton6 = false;
    public boolean joystickButton7 = false;
    public boolean joystickButton8 = false;
    public boolean joystickButton9 = false;
    public boolean joystickButton10 = false;
    public boolean joystickButton11 = false;
    public boolean joystickButton12 = false;
    public boolean joystickCalibrate = false;

    //Public joystick axis objects
    public double joystickY = 0.0;
    public double joystickX = 0.0;
    public double joystickTwist = 0.0;
    public double smallJoystickX = 0.0;
    public double smallJoystickY = 0.0;
    
    //Digital inputs
    public boolean launchButton = false;
    public int armSwitch = Llamahead.STOP;
    public int scoopSwitch = Llamahead.STOP;
    public int neckSwitch = Llamahead.STOP;
    public int autonomousModeSwitch = Llamahead.STOP;    
    public boolean autonomousOnSwitch = false;
    
    //Blank line to append to driverstation printouts so no previous text can be seen
    private static final String BLANK_LINE = "                              ";

    //Singleton so constructor is private
    private Driverstation()
    {
        driverstation = DriverStation.getInstance();
        lcd = DriverStationLCD.getInstance();
        joystick = new Joystick(1);
        joystickTemp = new Joystick(2);
        driverstationEnhanced = driverstation.getEnhancedIO();
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
        joystickTrigger = buttonStatus(joystick, J_TRIGGER);
        joystickButton2 = buttonStatus(joystick, J_BUTTON_2);
        joystickButton3 = buttonStatus(joystick, J_BUTTON_3);
        joystickButton4 = buttonStatus(joystick, J_BUTTON_4);
        joystickButton5 = buttonStatus(joystick, J_BUTTON_5);
        joystickButton6 = buttonStatus(joystick, J_BUTTON_6);
        joystickButton7 = buttonStatus(joystick, J_BUTTON_7);
        joystickButton8 = buttonStatus(joystick, J_BUTTON_8);
        joystickButton9 = buttonStatus(joystick, J_BUTTON_9);
        joystickButton10 = buttonStatus(joystick, J_BUTTON_10);
        joystickButton11 = buttonStatus(joystick, J_BUTTON_11);
        joystickButton12 = buttonStatus(joystick, J_BUTTON_12);

        //Read joystick axes
        joystickCalibrate = joystick.getRawAxis(CALIBRATE) < 0.0;
        joystickY = filterJoystickInput(joystick.getRawAxis(AXIS_Y));
        joystickX = filterJoystickInput(joystick.getRawAxis(AXIS_X));
        joystickTwist = filterJoystickInput(joystick.getRawAxis(TWIST));
        smallJoystickX = joystick.getRawAxis(SMALL_AXIS_X);
        smallJoystickY = joystick.getRawAxis(SMALL_AXIS_Y);
        
//        //Read all digital inputs
//        try
//        {
//            //Determine arm switch state
//            if (driverstationEnhanced.getDigital(SWITCH_ARM_UP))
//            {
//                armSwitch = Llamahead.FORWARD;
//            }
//            else if (driverstationEnhanced.getDigital(SWITCH_ARM_DOWN)) 
//            {
//                armSwitch = Llamahead.BACKWARD;
//            }
//            else
//            {
//                armSwitch = Llamahead.STOP;
//            }
//            
//            //Determine scoop switch state
//            if (driverstationEnhanced.getDigital(SWITCH_SCOOP_UP))
//            {
//                scoopSwitch = Llamahead.FORWARD;
//            }
//            else if (driverstationEnhanced.getDigital(SWITCH_SCOOP_DOWN))
//            {
//                scoopSwitch = Llamahead.BACKWARD;
//            }
//            else
//            {
//                scoopSwitch = Llamahead.STOP;
//            }
//            
//            //Determine neck switch state
//            if (driverstationEnhanced.getDigital(SWITCH_NECK_ADVANCE_UP)) 
//            {
//                neckSwitch = Llamahead.FORWARD;
//            }
//            else if (driverstationEnhanced.getDigital(SWITCH_NECK_ADVANCE_DOWN))
//            {
//                neckSwitch = Llamahead.BACKWARD;
//            }
//            else
//            {
//                neckSwitch = Llamahead.STOP;
//            }
//            
//            //Determine autonomous mode switch state
//            if (driverstationEnhanced.getDigital(SWITCH_AUTONOMOUS_UP))
//            {
//                autonomousModeSwitch = Llamahead.FORWARD;
//            }
//            else if (driverstationEnhanced.getDigital(SWITCH_AUTONOMOUS_DOWN))
//            {
//                autonomousModeSwitch = Llamahead.BACKWARD;
//            }
//            else
//            {
//                autonomousModeSwitch = Llamahead.STOP;
//            }
//            
//            //Read autonomous on/off switch
//            autonomousOnSwitch = driverstationEnhanced.getDigital(SWITCH_AUTONOMOUS_ON);
//            
//            //Turn on the autonomous switch led if the switch is on
//            driverstationEnhanced.setDigitalOutput(AUTONOMOUS_SWITCH_LED, autonomousOnSwitch);
//            
//            //Read launch button
//            launchButton = driverstationEnhanced.getDigital(BUTTON_LAUNCH);
//            
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
                line = Line.kMain6;
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
