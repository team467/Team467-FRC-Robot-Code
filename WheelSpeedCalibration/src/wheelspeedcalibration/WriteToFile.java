/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelspeedcalibration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class setup to write values out to the preferences file
 *
 * @author Kyle
 */
public class WriteToFile
{
    //setup constants for printing

    //val used to seperate vals
    static final String START_OF_VALS = "SlopesAndYInts=\"NaN\"";
    static final String YINT = "Yintercept";
    static final String SLOPE = "Slope";
    static final String POS = "Pos";
    static final String NEG = "Neg";
    static final String QUOTE = "\"";
    static final String NEW_LINE = "\n";

    /**
     * Removes the extra
     */
    public static void addToFile()
    {
        try
        {
            BufferedReader reader = null;
            String[] splitStringArray = null;
            try
            {
                reader = new BufferedReader(new FileReader(WheelSpeedCalibrationMap.PATH_TO_DEV_FILE));
                //reader = new BufferedReader(new FileReader(WheelSpeedCalibrationMap.PATH_TO_LOCAL_FILE));
            }
            catch (FileNotFoundException ex)
            {
                Utilities.showErrorBox("File does not exist! Please pull a file and try again!");
                Logger.getLogger(WheelSpeedCalibration.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (reader != null)
            {
                //creates the point obj for feeding into the arrayLists
                GraphPoint pointObj = null;

                //setup variables
                String line = null;
                String totalFile = null;

                //parse the data into the four arraylists
                while ((line = reader.readLine()) != null)
                {
                    line = line.trim();
                    if (totalFile == null)
                    {
                        totalFile = line + NEW_LINE;
                    }
                    else
                    {
                        totalFile = totalFile + line + NEW_LINE;
                    }
                }
                if (totalFile.contains(START_OF_VALS))
                {
                    //splits the file so that only the raw values are taken, not the printed slopes and y ints at the bottom
                    splitStringArray = totalFile.split(START_OF_VALS);
                    totalFile = splitStringArray[0];
                    totalFile = appendFile(totalFile);
                }
                else
                {
                    totalFile = appendFile(totalFile);
                }
                fileWriter(totalFile);
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(WheelSpeedCalibration.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *  Normalizes speed vals between -1 and 1 before writing to file
     */
    private static void normalizePowerValuesForRobot()
    {
                
    }

    /**
     * Appends the slopes and y intercepts to the end of the preferences file
     * after the raw calibration data
     *
     * @param totalFile String containing the file with raw calibration data
     * @return String containing the raw calibration data and the slopes and y
     *         intercepts at the bottom
     */
    private static String appendFile(String totalFile)
    {
        String line = null;
        totalFile = totalFile + START_OF_VALS;
        
        System.out.println("Writing to file");
        // ==================== normalizes speed to btw -1 and 1
        double lowestPosSlope = 0;
        double lowestNegSlope = 0;
        for (Wheel w : WheelSpeedCalibration.wheels)
        {            
            if (lowestPosSlope > w.posPoints.slope || lowestPosSlope == 0.0)
            {
                lowestPosSlope = w.posPoints.slope;
                
            }
            if (lowestPosSlope > w.negPoints.slope || lowestNegSlope == 0.0)
            {
                lowestPosSlope = w.negPoints.slope;
            }
        }
        
        lowestPosSlope = Math.abs(lowestPosSlope);
        lowestNegSlope = Math.abs(lowestNegSlope);
        
        //adds normalized slopes
        for (Wheel w : WheelSpeedCalibration.wheels)
        {
            System.out.println(w.posPoints.slope + " " + w.negPoints.slope);
            line = NEW_LINE + w.name + POS + SLOPE + "=" + QUOTE + (w.posPoints.slope / lowestPosSlope) + QUOTE;
            line = line + NEW_LINE + w.name + POS + YINT + "=" + QUOTE + w.posPoints.yint + QUOTE;
            line = line + NEW_LINE + w.name + NEG + SLOPE + "=" + QUOTE + (w.negPoints.slope / lowestNegSlope) + QUOTE;
            line = line + NEW_LINE + w.name + NEG + YINT + "=" + QUOTE + w.negPoints.yint + QUOTE;
            totalFile = totalFile + line;
        }
        totalFile = totalFile + NEW_LINE;
        return totalFile;
    }

    /**
     * Function used to write actual string data to the .ini file
     *
     * @param content String to print write into the file
     */
    private static void fileWriter(String content)
    {
        try
        {
            File file = new File(WheelSpeedCalibrationMap.PATH_TO_DEV_FILE);
            //File file = new File(WheelSpeedCalibrationMap.PATH_TO_LOCAL_FILE);
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

            System.out.println("Done Writing");

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Enum used to set state for push or pull of FTP
     */
    public enum WheelPositionEnum
    {

        FRONTLEFT, FRONTRIGHT, BACKLEFT, BACKRIGHT
    }
}
