/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelspeedcalibration;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kyle
 */
public class WheelSpeedCalibration
{

    public static final String INI_FILEPATH = "C:\\Users\\Kyle\\Documents\\FRC Calibration/wpilib-preferences.ini";
    public static final String FRONT_RIGHT_STRING_MASK = "FrontRightC";
    public static final String FRONT_RIGHT_LENGTH_STRING = "FrontRightClength";
    public static final String FRONT_LEFT_STRING_MASK = "FrontLeftC";
    public static final String FRONT_LEFT_LENGTH_STRING = "FrontLeftClength";
    public static final String BACK_RIGHT_STRING_MASK = "BackRightC";
    public static final String BACK_RIGHT_LENGTH_STRING = "BackRightClength";
    public static final String BACK_LEFT_STRING_MASK = "BackLeftC";
    public static final String BACK_LEFT_LENGTH_STRING = "BackLeftClength";
    
    
    public static ArrayList<Point> FrontRightList = new ArrayList();
    public static ArrayList<Point> FrontLeftList = new ArrayList();
    public static ArrayList<Point> BackRightList = new ArrayList();
    public static ArrayList<Point> BackLeftList = new ArrayList();

    public static Wheel FrontRightWheel = new Wheel("Front Right", "FrontRightC");
    public static Wheel FrontLeftWheel = new Wheel("Front Left", "FrontLeftC");
    public static Wheel BackRightWheel = new Wheel("Back Right", "BackRightC");
    public static Wheel BackLeftWheel = new Wheel("Back Left", "BackLeftC");
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        readAndParseFile();
        //FrontRightList = NormalizePower(FrontRightList);
    }

    private static void readAndParseFile()
    {
        try
        {
            BufferedReader reader = null;
            String[] splitStringArray = null;
            try
            {
                reader = new BufferedReader(new FileReader(INI_FILEPATH));
            }
            catch (FileNotFoundException ex)
            {
                Logger.getLogger(WheelSpeedCalibration.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (reader != null)
            {
                //creates the point obj for feeding into the arrayLists
                Point pointObj = null;

                //setup variables
                String line = null;

                String readLineNoPrefix = null;
                int intFromString = 0;

                String readFloatingPointVal = null;
                double doubleFromString = 0;

                //parse the data into the four arraylists
                while ((line = reader.readLine()) != null)
                {
                    pointObj = new Point();

                    if (line.contains("="))
                    {
                        //splits the line into 2 array pieces, splitStringArray[0] and [1]
                        //[0] is the piece w/ name and number, [1] contains the num val
                        splitStringArray = line.split("=");

                        //obtains which part the line is apart of
                        if (splitStringArray[0].startsWith(FRONT_RIGHT_STRING_MASK))
                        {
                            System.out.println("Front Right");
                            //checks if it is the lenght line
                            if (!splitStringArray[0].startsWith(FRONT_RIGHT_LENGTH_STRING))
                            {
                                //----------- Deals with index ----------------------

                                //this is the index val to be used as "index" in point, just in string version
                                readLineNoPrefix = splitStringArray[0].substring(FRONT_RIGHT_STRING_MASK.length());
                                //System.out.println(readLineNoPrefix);

                                pointObj.index = getLineIndex(readLineNoPrefix);

                                //---------------------------------------------------

                                /////////////////////////////////////////////////////

                                //----------- Deals with floating point val----------

                                //assigns unedited val with to string
                                readFloatingPointVal = splitStringArray[1];
                                
                                //assigns the object val to each
                                pointObj.speed = getFloatingPointVal(readFloatingPointVal);

                                //---------------------------------------------------


                                FrontRightList.add(pointObj);
                            }
                            else
                            {
                                /*
                                 *this code is for the lenght line only
                                 *this code is intended to doublecheck th lenght of arraylist aganst
                                 *the self reported length in the ini file
                                 */

                                //gets num of vals as self reported by ini file
                                readFloatingPointVal = splitStringArray[1];
                                readFloatingPointVal = formatString(readFloatingPointVal);

                                //converts string val to integer
                                intFromString = Integer.parseInt(readFloatingPointVal);

                                if (intFromString != FrontRightList.size())
                                {
                                    System.err.println("Error: self reported num of vals: " + intFromString + " differs from read num of vals: " + FrontRightList.size() + "in array FrontRight");
                                }
                                //System.out.println(readFloatingPointVal);
                            }
                        }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////                        
                        
                        else if (splitStringArray[0].startsWith(FRONT_LEFT_STRING_MASK))
                        {
                            System.out.println("Front Left");
                            //checks if it is the lenght line
                            if (!splitStringArray[0].startsWith(FRONT_LEFT_LENGTH_STRING))
                            {
                                //----------- Deals with index ----------------------

                                //this is the index val to be used as "index" in point, just in string version
                                readLineNoPrefix = splitStringArray[0].substring(FRONT_LEFT_STRING_MASK.length());
                                //System.out.println(readLineNoPrefix);

                                pointObj.index = getLineIndex(readLineNoPrefix);

                                //---------------------------------------------------

                                /////////////////////////////////////////////////////

                                //----------- Deals with floating point val----------

                                //assigns unedited val with to string
                                readFloatingPointVal = splitStringArray[1];
                                
                                //assigns the object val to each
                                pointObj.speed = getFloatingPointVal(readFloatingPointVal);

                                //---------------------------------------------------


                                FrontLeftList.add(pointObj);
                            }
                            else
                            {
                                /*
                                 *this code is for the lenght line only
                                 *this code is intended to doublecheck th lenght of arraylist aganst
                                 *the self reported length in the ini file
                                 */

                                //gets num of vals as self reported by ini file
                                readFloatingPointVal = splitStringArray[1];
                                readFloatingPointVal = formatString(readFloatingPointVal);

                                //converts string val to integer
                                intFromString = Integer.parseInt(readFloatingPointVal);

                                if (intFromString != FrontLeftList.size())
                                {
                                    System.err.println("Error: self reported num of vals: " + 
                                            intFromString + " differs from read num of vals: " + 
                                            FrontLeftList.size() + "in array FrontLeft");
                                }
                                //System.out.println(readFloatingPointVal);
                            }
                        }
                        
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////                        
                        
                        else if (splitStringArray[0].startsWith(BACK_RIGHT_STRING_MASK))
                        {
                            System.out.println("Back Right");
                            //checks if it is the lenght line
                            if (!splitStringArray[0].startsWith(BACK_RIGHT_LENGTH_STRING))
                            {
                                //----------- Deals with index ----------------------

                                //this is the index val to be used as "index" in point, just in string version
                                readLineNoPrefix = splitStringArray[0].substring(BACK_RIGHT_STRING_MASK.length());
                                //System.out.println(readLineNoPrefix);

                                pointObj.index = getLineIndex(readLineNoPrefix);

                                //---------------------------------------------------

                                /////////////////////////////////////////////////////

                                //----------- Deals with floating point val----------

                                //assigns unedited val with to string
                                readFloatingPointVal = splitStringArray[1];
                                
                                //assigns the object val to each
                                pointObj.speed = getFloatingPointVal(readFloatingPointVal);

                                //---------------------------------------------------


                                BackRightList.add(pointObj);
                            }
                            else
                            {
                                /*
                                 *this code is for the lenght line only
                                 *this code is intended to doublecheck th lenght of arraylist aganst
                                 *the self reported length in the ini file
                                 */

                                //gets num of vals as self reported by ini file
                                readFloatingPointVal = splitStringArray[1];
                                readFloatingPointVal = formatString(readFloatingPointVal);

                                //converts string val to integer
                                intFromString = Integer.parseInt(readFloatingPointVal);

                                if (intFromString != BackRightList.size())
                                {
                                    System.err.println("Error: self reported num of vals: " + 
                                            intFromString + " differs from read num of vals: " + 
                                            BackRightList.size() + "in array BackRight");
                                }
                                //System.out.println(readFloatingPointVal);
                            }
                        }
                        
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////                       
                        
                        else if (splitStringArray[0].startsWith(BACK_LEFT_STRING_MASK))
                        {
                            System.out.println("Back Left");
                            //checks if it is the lenght line
                            if (!splitStringArray[0].startsWith(BACK_LEFT_LENGTH_STRING))
                            {
                                //----------- Deals with index ----------------------

                                //this is the index val to be used as "index" in point, just in string version
                                readLineNoPrefix = splitStringArray[0].substring(BACK_LEFT_STRING_MASK.length());
                                //System.out.println(readLineNoPrefix);

                                pointObj.index = getLineIndex(readLineNoPrefix);

                                //---------------------------------------------------

                                /////////////////////////////////////////////////////

                                //----------- Deals with floating point val----------

                                //assigns unedited val with to string
                                readFloatingPointVal = splitStringArray[1];
                                
                                //assigns the object val to each
                                pointObj.speed = getFloatingPointVal(readFloatingPointVal);

                                //---------------------------------------------------


                                BackLeftList.add(pointObj);
                            }
                            else
                            {
                                /*
                                 *this code is for the lenght line only
                                 *this code is intended to doublecheck th lenght of arraylist aganst
                                 *the self reported length in the ini file
                                 */

                                //gets num of vals as self reported by ini file
                                readFloatingPointVal = splitStringArray[1];
                                readFloatingPointVal = formatString(readFloatingPointVal);

                                //converts string val to integer
                                intFromString = Integer.parseInt(readFloatingPointVal);

                                if (intFromString != BackLeftList.size())
                                {
                                    System.err.println("Error: self reported num of vals: " + 
                                            intFromString + " differs from read num of vals: " + 
                                            BackLeftList.size() + "in array BackLeft");
                                }
                                //System.out.println(readFloatingPointVal);
                            }
                        }
                    }
                }
            }
            reader.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(WheelSpeedCalibration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /////////////////////////////////////////////////////////////////
    /**
     * formats out " and whitespace as well as replaces NaN w/ 0.0.
     *
     * @param input string to format
     * @return formatted string
     */
    private static String formatString(String input)
    {
        //replaces " with space
        input = input.replace('"', ' ');

        //gets rid of excess spaces
        input = input.trim();

        //replaces any "NaN" s with 0.0
        input = ("NaN".equals(input)) ? "0.0" : input;
        return input;
    }

    /**
     * Takes string after prefix is removed and
     * @param input string from the string array w/out the prefix
     * @return index val for the object
     */
    private static int getLineIndex(String input)
    {
        int intFromString = -1;
        //takes the
        try
        {
            intFromString = Integer.parseInt(input);
        }
        catch (Exception ex)
        {
            Logger.getLogger(WheelSpeedCalibration.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Error: tried to convert " + input + " to an int");
        }
        //System.out.println(intFromString);

        return intFromString;
    }

    /**
     * takes in an unformatted string from second part of the string array and formatts and converts to a double
     * @param input take unformatted string from second part of string array
     * @return double formatted for the val
     */
    private static double getFloatingPointVal(String input)
    {
        double doubleFromString = -1.0;
        //formats out " and whtespace as well as replaces NaN w/ 0.0
        input = formatString(input);        

        try
        {
            //parses string to double
            doubleFromString = Double.parseDouble(input);
        }
        catch (Exception ex)
        {
            Logger.getLogger(WheelSpeedCalibration.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Error: tried to convert " + input + " to an double");
        }
        return doubleFromString;
    }
}