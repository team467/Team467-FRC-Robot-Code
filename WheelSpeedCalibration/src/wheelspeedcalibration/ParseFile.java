/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelspeedcalibration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Class used to read the preferences file from the cRIO and parse out data
 *
 * @author Kyle
 */
public class ParseFile
{

    /**
     * Reads file from default file location on local machine. Will put each
     * value form the file into the values of ArrayLists of wheels from
     * WheelSpeedCalibration
     */
    public static void readAndParseFile()
    {
        try
        {
            BufferedReader reader = null;
            String[] splitStringArray = null;

            //checks if the file exists

            File checkIfExist = new File(WheelSpeedCalibrationMap.PATH_TO_DEV_FILE);
            //File checkIfExist = new File(WheelSpeedCalibrationMap.PATH_TO_LOCAL_FILE);
            if (checkIfExist.exists())
            {
                try
                {
                    reader = new BufferedReader(new FileReader(WheelSpeedCalibrationMap.PATH_TO_DEV_FILE));
                    //reader = new BufferedReader(new FileReader(WheelSpeedCalibrationMap.PATH_TO_LOCAL_FILE));
                }
                catch (FileNotFoundException ex)
                {
                    Logger.getLogger(WheelSpeedCalibration.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (reader != null)
                {
                    //creates the point obj for feeding into the arrayLists
                    GraphPoint pointObj = null;

                    //setup variables
                    String line = null;

                    String readLineNoPrefix = null;
                    int intFromString = 0;

                    String readFloatingPointVal = null;
                    double doubleFromString = 0;


                    //parse the data into the four arraylists
                    while ((line = reader.readLine()) != null)
                    {
                        if (line.contains("="))
                        {
                            //splits the line into 2 array pieces, splitStringArray[0] and [1]
                            //[0] is the piece w/ name and number, [1] contains the num val
                            splitStringArray = line.split("=");

                            for (Wheel w : WheelSpeedCalibration.wheels)
                            {
                                if (splitStringArray[0].startsWith(w.key))
                                {
                                    if (!splitStringArray[0].startsWith(w.key + "length"))
                                    {
                                        pointObj = new GraphPoint();

                                        //----------- Deals with index ----------------------

                                        //this is the index val to be used as "index" in point, just in string version
                                        readLineNoPrefix = splitStringArray[0].substring(w.key.length());
                                        //System.out.println(readLineNoPrefix);

                                        pointObj.index = getLineIndex(readLineNoPrefix);

                                        //---------------------------------------------------

                                        /////////////////////////////////////////////////////

                                        //----------- Deals with floating point val----------                                    

                                        //assigns the object val to each
                                        pointObj.speed = getFloatingPointVal(splitStringArray[1]);

                                        //---------------------------------------------------


                                        w.points.add(pointObj);
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

                                        if (intFromString != w.points.size())
                                        {
                                            System.err.println("ParseFile.java Error: self reported num of vals: " + intFromString + " differs from read num of vals: " + w.points.size() + "in array  " + w.name);
                                            Utilities.showErrorBox("ParseFile.java Error: self reported num of vals: " + intFromString + " differs from read num of vals: " + w.points.size() + "in array  " + w.name);
                                        }                                        
                                    }
                                }
                            }
                        }
                    }
                }
                reader.close();
            }
            else
            {
                System.out.println("File does not exist. Please pull a valid file from the cRIO of the robot.");                
                WheelSpeedCalibrationMap.preferencesNotExistFlag = true;                
            }

        }
        catch (IOException ex)
        {
            Logger.getLogger(WheelSpeedCalibration.class
                    .getName()).log(Level.SEVERE, null, ex);
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
     * Takes string after prefix is removed and computes the item number from
     * that line
     *
     * @param input string from the string array without the prefix
     * @return index value for each GraphPoint object index value
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
     * takes in an unformatted string from second part of the string array and
     * formatts and converts to a double
     *
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
            Logger.getLogger(WheelSpeedCalibration.class
                    .getName()).log(Level.SEVERE, null, ex);
            System.err.println(
                    "Error: tried to convert " + input + " to an double");
        }
        return doubleFromString;
    }
}
