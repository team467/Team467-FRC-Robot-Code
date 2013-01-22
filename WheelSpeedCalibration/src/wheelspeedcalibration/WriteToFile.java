/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelspeedcalibration;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kyle
 */
public class WriteToFile
{

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
                 * BackLeftClength="256"
                 * FrontRightFSlope="x.xxxxx"
                 * FrontRightFyint="x.xxxxx"
                 * FrontRightBSlope="x.xxxxx"
                 * FrontRightByint="x.xxxxx"
                 * FrontLeftFSlope="x.xxxxx"
                 * FrontLeftFyint="x.xxxxx"
                 * FrontLeftBSlope="x.xxxxx"
                 * FrontLefttByint="x.xxxxx"
                 * BackRightFSlope="x.xxxxx"
                 * BackRightFyint="x.xxxxx"
                 * BackRightBSlope="x.xxxxx"
                 * BackRightByint="x.xxxxx"
                 * BackLeftFSlope="x.xxxxx"
                 * BackLeftFyint="x.xxxxx"
                 * BackLeftBSlope="x.xxxxx"
                 * BackLeftByint="x.xxxxx"
                 */            

                //parse the data into the four arraylists
                while ((line = reader.readLine()) != null)
                {                    
                    totalFile = totalFile + line + "\n";                    
                }
                System.out.println(totalFile);
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(WheelSpeedCalibration.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
}
