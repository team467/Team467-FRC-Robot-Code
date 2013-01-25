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
 *
 * @author Kyle
 */
public class WriteToFile
{
    //setup constants for printing

    static final String START_OF_VALS = "Slopes and Y Ints:";
    static final String YINT = "yintercept";
    static final String SLOPE = "slope";
    static final String NEW_LINE = "\n";

    public static void addToFile()
    {
        try
        {
            BufferedReader reader = null;
            String[] splitStringArray = null;
            try
            {
                reader = new BufferedReader(new FileReader(WheelSpeedCalibrationMap.PATH_TO_LOCAL_FILE));
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
                String totalFile = null;

                /*
                 * How the file should look, starting w/ the last current val:
                 * 
                 * BLClength="256"
                 * Slopes and Y Ints:
                 * FRONTRIGHT
                 * FRFSlope="x.xxxxx"
                 * FRFyint="x.xxxxx"
                 * FRBSlope="x.xxxxx"
                 * FRByint="x.xxxxx"
                 * FRONTLEFT
                 * FLFSlope="x.xxxxx"
                 * FLFyint="x.xxxxx"
                 * FLBSlope="x.xxxxx"
                 * FLtByint="x.xxxxx"
                 * BACKRIGHT
                 * BRFSlope="x.xxxxx"
                 * BRFyint="x.xxxxx"
                 * BRBSlope="x.xxxxx"
                 * BRByint="x.xxxxx"
                 * BACKLEFT
                 * BLFSlope="x.xxxxx"
                 * BLFyint="x.xxxxx"
                 * BLBSlope="x.xxxxx"
                 * BLByint="x.xxxxx"
                 */

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
                    //splits to just the 
                    splitStringArray = totalFile.split(START_OF_VALS);
                    totalFile = splitStringArray[0];
                    totalFile = appendFile(totalFile);
                }
                else
                {
                    totalFile = appendFile(totalFile);
                }
                fileWriter(totalFile);
                //System.out.println(totalFile);
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(WheelSpeedCalibration.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String appendFile(String totalFile)
    {
        String line = null;
        totalFile = totalFile + START_OF_VALS;
        for (Wheel w : WheelSpeedCalibration.wheels)
        {
            //TODO: Finish
            line = NEW_LINE + w.Name + SLOPE + "=" + w.posPoints.slope;
            line = line + NEW_LINE + w.Name + YINT + "=" + w.posPoints.yint;
            line = line + NEW_LINE + w.Name + SLOPE + "=" + w.negPoints.slope;
            line = line + NEW_LINE + w.Name + YINT + "=" + w.negPoints.yint;
            totalFile = totalFile + line;
        }        
        return totalFile;
    }

    private static void fileWriter(String content)
    {
        try
        {

            File file = new File(WheelSpeedCalibrationMap.PATH_TO_LOCAL_FILE);
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

    public enum WheelPositionEnum
    {

        FRONTLEFT, FRONTRIGHT, BACKLEFT, BACKRIGHT
    }
}
