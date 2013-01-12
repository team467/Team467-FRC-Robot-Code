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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        readFile();
    }
    
    private static void readFile()
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
                            //checks if it is the lenght line
                            if (!splitStringArray[0].startsWith(FRONT_RIGHT_LENGTH_STRING))
                            {
                                //----------- Deals with index ----------------------

                                //this is the index val to be used as "index" in point, just in string version
                                readLineNoPrefix = splitStringArray[0].substring(FRONT_RIGHT_STRING_MASK.length());
                                //System.out.println(readLineNoPrefix);

                                //takes the
                                try
                                {
                                    intFromString = Integer.parseInt(readLineNoPrefix);                                    
                                }
                                catch (Exception e)
                                {
                                    System.err.println("Error: tried to convert " + readLineNoPrefix + " to an int");
                                }
                                //System.out.println(intFromString);

                                pointObj.index = intFromString;

                                //---------------------------------------------------

                                /////////////////////////////////////////////////////

                                //----------- Deals with floating point val----------

                                //assigns unedited val with to string
                                readFloatingPointVal = splitStringArray[1];

                                //formats out " and whtespace as well as replaces NaN w/ 0.0
                                readFloatingPointVal = formatString(readFloatingPointVal);

                                //System.out.println(readFloatingPointVal);

                                try
                                {
                                    //parses string to double
                                    doubleFromString = Double.parseDouble(readFloatingPointVal);
                                }
                                catch (Exception e)
                                {
                                    System.err.println("Error: tried to convert " + readFloatingPointVal + " to an double");
                                }

                                //assigns the object val to each
                                pointObj.X = doubleFromString;

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
                        else if (splitStringArray[0].startsWith(FRONT_LEFT_STRING_MASK))
                        {
                        }
                        else if (splitStringArray[0].startsWith(BACK_RIGHT_STRING_MASK))
                        {
                        }
                        else if (splitStringArray[0].startsWith(BACK_LEFT_STRING_MASK))
                        {
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
}
//                            //if not the summry length statement
//                            if (!splitStringArray[0].startsWith(FRONT_RIGHT_LENGTH_STRING))
//                            {                                
//                                readLineTemp = splitStringArray[0].substring(FRONT_RIGHT_STRING_MASK.length());
//                                fromString = Integer.parseInt(readLineTemp);
//                                
//                                //substitiutes for bad vals
//                                if ("NaN".equals(readLineTemp))
//                                {
//                                    readLineTemp = "0";
//                                }
//                                
//                                //feeds index val to obj as int                                
//                                pointObj = new Point();
//                                //System.out.println(readLineTemp);                                
//                                //System.out.println(fromString);
//                                pointObj.index = fromString;
//                                FrontRightList.add(pointObj);
//                            }
//                            else
//                            {
//                                fromString = Integer.parseInt(readLineTemp);
//                                if (FrontRightList.size() == fromString)
//                                {
//                                    System.out.println("YSYS");
//                                }
//                                else
//                                {
//                                    System.out.println("Lenght of real list: " + FrontRightList.size() + " Lenght it claims: " + fromString);
//                                }
//                                System.out.println(readLineTemp);
//                            }
//                        }
//                        else if (true)
//                        {
//                        }
//                    }
