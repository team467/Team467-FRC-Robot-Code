package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.DriverStationLCD.Line;


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
    
    //Joystick objects
    private Joystick467 JoystickLeft;
    private Joystick467 JoystickRight;
    
    //Blank line to append to driverstation printouts so no previous text can be seen
    private static final String BLANK_LINE = "                              ";

    //Singleton so constructor is private
    private Driverstation()
    {
        driverstation = DriverStation.getInstance();
        lcd = DriverStationLCD.getInstance();
        JoystickLeft = new Joystick467(1);
        JoystickRight = new Joystick467(2);
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
    
    public double getBatteryVoltage() {
        return driverstation.getBatteryVoltage();
    }

    /**
     * Read all Robot Inputs. Typically, this is called once per iteration of the main
     * event loop. Any soft filtering of inputs to remove noise or implement
     * non-linear acceleration is also done here.
     */
    public void readInputs()
    {
        JoystickLeft.readInputs();
        JoystickRight.readInputs();
    }
    
    /**
     * Gets left joystick instance.
     * @return 
     */
    public Joystick467 getLeftJoystick() {
        return JoystickLeft;
    }
    
    /**
     * Gets right joystick instance.
     * @return 
     */
    public Joystick467 getRightJoystick() {
        return JoystickRight;
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
     * Clears the driverstation screen.
     */
    public void clearPrint() {
        for (int i = 1; i <= 6; i++) {
            printFinal(BLANK_LINE, i);
        }
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
