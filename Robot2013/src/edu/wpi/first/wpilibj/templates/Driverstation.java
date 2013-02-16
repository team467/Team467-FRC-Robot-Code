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
    private Joystick JoystickDriver;
    private Joystick JoystickNavigator;

    //Public joystick button objects
    public boolean JoystickDriverTrigger = false;
    public boolean JoystickDriverButton2 = false;
    public boolean JoystickDriverButton3 = false;
    public boolean JoystickDriverButton4 = false;
    public boolean JoystickDriverButton5 = false;
    public boolean JoystickDriverButton6 = false;
    public boolean JoystickDriverButton7 = false;
    public boolean JoystickDriverButton8 = false;
    public boolean JoystickDriverButton9 = false;
    public boolean JoystickDriverButton10 = false;
    public boolean JoystickDriverButton11 = false;
    public boolean JoystickDriverButton12 = false;
    public boolean JoystickDriverCalibrate = false;
    
    public boolean JoystickNaivigatorTrigger = false;
    public boolean JoystickNaivigatorButton2 = false;
    public boolean JoystickNaivigatorButton3 = false;
    public boolean JoystickNaivigatorButton4 = false;
    public boolean JoystickNaivigatorButton5 = false;
    public boolean JoystickNaivigatorButton6 = false;
    public boolean JoystickNaivigatorButton7 = false;
    public boolean JoystickNaivigatorButton8 = false;
    public boolean JoystickNaivigatorButton9 = false;
    public boolean JoystickNaivigatorButton10 = false;
    public boolean JoystickNaivigatorButton11 = false;
    public boolean JoystickNaivigatorButton12 = false;
    public boolean JoystickNaivigatorCalibrate = false;


    //Public joystick axis objects
    public double JoystickDriverY = 0.0;
    public double JoystickDriverX = 0.0;
    public double JoystickDriverTwist = 0.0;
    public double smallJoystickDriverX = 0.0;
    public double smallJoystickDriverY = 0.0;
    
    public double JoystickNaivigatorY = 0.0;
    public double JoystickNaivigatorX = 0.0;
    public double JoystickNaivigatorTwist = 0.0;
    public double smallJoystickNaivigatorX = 0.0;
    public double smallJoystickNaivigatorY = 0.0;
    
    //Blank line to append to driverstation printouts so no previous text can be seen
    private static final String BLANK_LINE = "                              ";

    //Singleton so constructor is private
    private Driverstation()
    {
        driverstation = DriverStation.getInstance();
        lcd = DriverStationLCD.getInstance();
        JoystickDriver = new Joystick(1);
        JoystickNavigator = new Joystick(2);
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
     * Read all Robot Inputs. Typically, this is called once per iteration of the main
     * event loop. Any soft filtering of inputs to remove noise or implement
     * non-linear acceleration is also done here.
     */
    public void readInputs()
    {
        //Read JoystickDriver and JoystickNaivigator buttons
        JoystickDriverTrigger = buttonStatus(JoystickDriver, J_TRIGGER);
        JoystickDriverButton2 = buttonStatus(JoystickDriver, J_BUTTON_2);
        JoystickDriverButton3 = buttonStatus(JoystickDriver, J_BUTTON_3);
        JoystickDriverButton4 = buttonStatus(JoystickDriver, J_BUTTON_4);
        JoystickDriverButton5 = buttonStatus(JoystickDriver, J_BUTTON_5);
        JoystickDriverButton6 = buttonStatus(JoystickDriver, J_BUTTON_6);
        JoystickDriverButton7 = buttonStatus(JoystickDriver, J_BUTTON_7);
        JoystickDriverButton8 = buttonStatus(JoystickDriver, J_BUTTON_8);
        JoystickDriverButton9 = buttonStatus(JoystickDriver, J_BUTTON_9);
        JoystickDriverButton10 = buttonStatus(JoystickDriver, J_BUTTON_10);
        JoystickDriverButton11 = buttonStatus(JoystickDriver, J_BUTTON_11);
        JoystickDriverButton12 = buttonStatus(JoystickDriver, J_BUTTON_12);
        
        JoystickNaivigatorTrigger = buttonStatus(JoystickDriver, J_TRIGGER);
        JoystickNaivigatorButton2 = buttonStatus(JoystickDriver, J_BUTTON_2);
        JoystickNaivigatorButton3 = buttonStatus(JoystickDriver, J_BUTTON_3);
        JoystickNaivigatorButton4 = buttonStatus(JoystickNavigator, J_BUTTON_4);
        JoystickNaivigatorButton5 = buttonStatus(JoystickNavigator, J_BUTTON_5);
        JoystickNaivigatorButton6 = buttonStatus(JoystickNavigator, J_BUTTON_6);
        JoystickNaivigatorButton7 = buttonStatus(JoystickNavigator, J_BUTTON_7);
        JoystickNaivigatorButton8 = buttonStatus(JoystickNavigator, J_BUTTON_8);
        JoystickNaivigatorButton9 = buttonStatus(JoystickNavigator, J_BUTTON_9);
        JoystickNaivigatorButton10 = buttonStatus(JoystickNavigator, J_BUTTON_10);
        JoystickNaivigatorButton11 = buttonStatus(JoystickNavigator, J_BUTTON_11);
        JoystickNaivigatorButton12 = buttonStatus(JoystickDriver, J_BUTTON_12);

        //Read JoystickDriver and JoystickNaivigator axes
        JoystickDriverCalibrate = JoystickDriver.getRawAxis(CALIBRATE) < 0.0;
        JoystickDriverY = filterJoystickInput(JoystickDriver.getRawAxis(AXIS_Y));
        JoystickDriverX = filterJoystickInput(JoystickDriver.getRawAxis(AXIS_X));
        JoystickDriverTwist = filterJoystickInput(JoystickDriver.getRawAxis(TWIST));
        smallJoystickDriverX = JoystickDriver.getRawAxis(SMALL_AXIS_X);
        smallJoystickDriverY = JoystickDriver.getRawAxis(SMALL_AXIS_Y);
        
        JoystickNaivigatorCalibrate = JoystickNavigator.getRawAxis(CALIBRATE) < 0.0;
        JoystickNaivigatorY = filterJoystickInput(JoystickNavigator.getRawAxis(AXIS_Y));
        JoystickNaivigatorX = filterJoystickInput(JoystickNavigator.getRawAxis(AXIS_X));
        JoystickNaivigatorTwist = filterJoystickInput(JoystickNavigator.getRawAxis(TWIST));
        smallJoystickNaivigatorX = JoystickNavigator.getRawAxis(SMALL_AXIS_X);
        smallJoystickNaivigatorY = JoystickNavigator.getRawAxis(SMALL_AXIS_Y);
        
       
        
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
