package edu.wpi.first.wpilibj.templates;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationEnhancedIO;
import edu.wpi.first.wpilibj.DriverStationEnhancedIO.EnhancedIOException;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.DriverStationLCD.Line;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.networktables2.util.List;


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
    
  
 
    
    //Joystick objects
    private Joystick JoystickLeft;
    private Joystick JoystickRight;

    //Public JoystickLeft button objects
    public boolean JoystickLeftTrigger = false;
    public boolean JoystickLeftButton2 = false;
    public boolean JoystickLeftButton3 = false;
    public boolean JoystickLeftButton4 = false;
    public boolean JoystickLeftButton5 = false;
    public boolean JoystickLeftButton6 = false;
    public boolean JoystickLeftButton7 = false;
    public boolean JoystickLeftButton8 = false;
    public boolean JoystickLeftButton9 = false;
    public boolean JoystickLeftButton10 = false;
    public boolean JoystickLeftButton11 = false;
    public boolean JoystickLeftButton12 = false;
    public boolean JoystickLeftCalibrate = false;
    
    public boolean JoystickRightTrigger = false;
    public boolean JoystickRightButton2 = false;
    public boolean JoystickRightButton3 = false;
    public boolean JoystickRightButton4 = false;
    public boolean JoystickRightButton5 = false;
    public boolean JoystickRightButton6 = false;
    public boolean JoystickRightButton7 = false;
    public boolean JoystickRightButton8 = false;
    public boolean JoystickRightButton9 = false;
    public boolean JoystickRightButton10 = false;
    public boolean JoystickRightButton11 = false;
    public boolean JoystickRightButton12 = false;
    public boolean JoystickRightCalibrate = false;


    //Public JoystickLeft axis objects
    public double JoystickLeftY = 0.0;
    public double JoystickLeftX = 0.0;
    public double JoystickLeftTwist = 0.0;
    public double smallJoystickLeftX = 0.0;
    public double smallJoystickLeftY = 0.0;
    
    public double JoystickRightY = 0.0;
    public double JoystickRightX = 0.0;
    public double JoystickRightTwist = 0.0;
    public double smallJoystickRightX = 0.0;
    public double smallJoystickRightY = 0.0;
    
    //Blank line to append to driverstation printouts so no previous text can be seen
    private static final String BLANK_LINE = "                              ";

    //Singleton so constructor is private
    private Driverstation()
    {
        driverstation = DriverStation.getInstance();
        lcd = DriverStationLCD.getInstance();
        JoystickLeft = new Joystick(1);
        JoystickRight = new Joystick(2);
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
     * Calculate the angle of a JoystickLeft, given a specific x and y input value.
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
     * Implement a dead zone for JoystickLeft centering - and a non-linear
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
     * Read all Robot Inputs. Typically, this is called once per iteration of the main
     * event loop. Any soft filtering of inputs to remove noise or implement
     * non-linear acceleration is also done here.
     */
    public void readInputs()
    {
        //Read JoystickLeft and JoystickRight buttons
        JoystickLeftTrigger = buttonStatus(JoystickLeft, J_TRIGGER);
        JoystickLeftButton2 = buttonStatus(JoystickLeft, J_BUTTON_2);
        JoystickLeftButton3 = buttonStatus(JoystickLeft, J_BUTTON_3);
        JoystickLeftButton4 = buttonStatus(JoystickLeft, J_BUTTON_4);
        JoystickLeftButton5 = buttonStatus(JoystickLeft, J_BUTTON_5);
        JoystickLeftButton6 = buttonStatus(JoystickLeft, J_BUTTON_6);
        JoystickLeftButton7 = buttonStatus(JoystickLeft, J_BUTTON_7);
        JoystickLeftButton8 = buttonStatus(JoystickLeft, J_BUTTON_8);
        JoystickLeftButton9 = buttonStatus(JoystickLeft, J_BUTTON_9);
        JoystickLeftButton10 = buttonStatus(JoystickLeft, J_BUTTON_10);
        JoystickLeftButton11 = buttonStatus(JoystickLeft, J_BUTTON_11);
        JoystickLeftButton12 = buttonStatus(JoystickLeft, J_BUTTON_12);
        
        JoystickRightTrigger = buttonStatus(JoystickLeft, J_TRIGGER);
        JoystickRightButton2 = buttonStatus(JoystickLeft, J_BUTTON_2);
        JoystickRightButton3 = buttonStatus(JoystickLeft, J_BUTTON_3);
        JoystickRightButton4 = buttonStatus(JoystickRight, J_BUTTON_4);
        JoystickRightButton5 = buttonStatus(JoystickRight, J_BUTTON_5);
        JoystickRightButton6 = buttonStatus(JoystickRight, J_BUTTON_6);
        JoystickRightButton7 = buttonStatus(JoystickRight, J_BUTTON_7);
        JoystickRightButton8 = buttonStatus(JoystickRight, J_BUTTON_8);
        JoystickRightButton9 = buttonStatus(JoystickRight, J_BUTTON_9);
        JoystickRightButton10 = buttonStatus(JoystickRight, J_BUTTON_10);
        JoystickRightButton11 = buttonStatus(JoystickRight, J_BUTTON_11);
        JoystickRightButton12 = buttonStatus(JoystickLeft, J_BUTTON_12);

        //Read JoystickLeft and JoystickRight axes
        JoystickLeftCalibrate = JoystickLeft.getRawAxis(CALIBRATE) < 0.0;
        JoystickLeftY = filterJoystickInput(JoystickLeft.getRawAxis(AXIS_Y));
        JoystickLeftX = filterJoystickInput(JoystickLeft.getRawAxis(AXIS_X));
        JoystickLeftTwist = filterJoystickInput(JoystickLeft.getRawAxis(TWIST));
        smallJoystickLeftX = JoystickLeft.getRawAxis(SMALL_AXIS_X);
        smallJoystickLeftY = JoystickLeft.getRawAxis(SMALL_AXIS_Y);
        
        JoystickRightCalibrate = JoystickRight.getRawAxis(CALIBRATE) < 0.0;
        JoystickRightY = filterJoystickInput(JoystickRight.getRawAxis(AXIS_Y));
        JoystickRightX = filterJoystickInput(JoystickRight.getRawAxis(AXIS_X));
        JoystickRightTwist = filterJoystickInput(JoystickRight.getRawAxis(TWIST));
        smallJoystickRightX = JoystickRight.getRawAxis(SMALL_AXIS_X);
        smallJoystickRightY = JoystickRight.getRawAxis(SMALL_AXIS_Y);
        
       
        
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
    
    private List debounceKeys;
    private List debounceVals;
    
    /**
     * Make a new debounce with the given key
     * @param key The key name of the debounce
     */
    public void makeDebounce(String key)
    {
        //Make the two lists if they don't exist yet
        if (debounceKeys == null)
        {
            debounceKeys = new List();
            debounceVals = new List();
        }
        
        //Check that the current key doesn't already exist
        for (int i = 0; i < debounceKeys.size(); i++)
        {
            if (debounceKeys.get(i).equals(key))
            {
                System.err.println("Error: Debounce with key \"" + key + "\" already exists");
                return;
            }
        }
    }
    
    /**
     * Checks the debounced value. Will only return true if the given value is true
     * and this function has been called at least once prior when the value is false
     * @param key The debounce key
     * @param value The current value of the check input
     * @return 
     */
    public boolean checkDebounce(String key, boolean value)
    {
        //Get debounce value
        boolean dval = getDebounceValue(key);
        
        //If button is debounced, return true
        if (dval == false && value == true)
        {
            setDebounceValue(key, true);
            return true;
        }
        else if (value == false)
        {
            setDebounceValue(key, false);
        }
        
        //Defaultly return false
        return false;
        
    }
    
    /**
     * Private function to get debounce value that corresponds to the given key
     * @param key The key to check
     * @return 
     */
    private boolean getDebounceValue(String key)
    {
        //Check the key list to see if any keys match the given one
        for (int i = 0; i < debounceKeys.size(); i++)
        {
            if (debounceKeys.get(i).equals(key))
            {
                //Return the corresponding value for the correct key
                return ((Boolean)debounceVals.get(i)).booleanValue();
            }
        }
        
        //Default return false
        return false;
    }
    
    /**
     * Private function to set debounce value that corresponds to the given key
     * @param key The key to set
     * @param value 
     */
    private void setDebounceValue(String key, boolean value)
    {
        //Check the key list to see if any keys match the given one
        for (int i = 0; i < debounceKeys.size(); i++)
        {
            if (debounceKeys.get(i).equals(key))
            {
                //Set the value at the corresponding index
               debounceVals.set(i, Boolean.valueOf(value));
            }
        }
    }

}
