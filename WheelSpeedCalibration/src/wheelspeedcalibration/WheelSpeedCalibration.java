/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelspeedcalibration;

import java.util.ArrayList;

/**
 *
 * @author Kyle
 */
public class WheelSpeedCalibration
{

    public static ArrayList<Wheel> wheels = new ArrayList<>();

    /**
     * Main Class - This is where all code starts
     *
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        //creats the 4 wheels
        wheels.add(new Wheel("FrontRight", "FrontRightC"));
        wheels.add(new Wheel("FrontLeft", "FrontLeftC"));
        wheels.add(new Wheel("BackRight", "BackRightC"));
        wheels.add(new Wheel("BackLeft", "BackLeftC"));

        //pulls file from robot if online
        if (!WheelSpeedCalibrationMap.OFF_LINE_MODE)
        {
            FTPUtilities.transmitPreferences(ServerOperationEnum.PULL);
        }

        //reads through the file and write the values to the wheels ArrayList
        ParseFile.readAndParseFile();


        for (Wheel w : wheels)
        {
            //filter data to remove all "NaN" and "0.0" values
            w.points = DataCrunchingUtilities.removeZeros(w.points);

            //normalize data points to have a speed value between -1.0 and 1.0
            w.doubleArrayList = DataCrunchingUtilities.normalizeValues(w.points);

            //runs line fit on data both forward (POS) and backward (NEG), then filters the data that is more than a 
            // certian distance away from the line to be unused.
            w.negPoints = DataCrunchingUtilities.LeastSquaredRegression(w.doubleArrayList.negArrayList, WheelSpeedCalibrationMap.BACKWARD);
            w.posPoints = DataCrunchingUtilities.LeastSquaredRegression(w.doubleArrayList.posArrayList, WheelSpeedCalibrationMap.FORWARD);

            //runs fit on data again to make the computed least squared regression line not use the outliers filtered out 
            //by the previous run
            w.negPoints = DataCrunchingUtilities.LeastSquaredRegression(w.doubleArrayList.negArrayList, WheelSpeedCalibrationMap.BACKWARD);
            w.posPoints = DataCrunchingUtilities.LeastSquaredRegression(w.doubleArrayList.posArrayList, WheelSpeedCalibrationMap.FORWARD);

            //prints out slope and y int vals
            if (WheelSpeedCalibrationMap.DEBUG_MODE)
            {
                System.out.println("=== Point 1 Pos ===");
                System.out.println(w.posPoints.point1.x);
                System.out.println(w.posPoints.point1.y);
                System.out.println("=== Point 2 Pos ===");
                System.out.println(w.posPoints.point2.x);
                System.out.println(w.posPoints.point2.y);
                System.out.println("=== Point 1 Neg ===");
                System.out.println(w.negPoints.point1.x);
                System.out.println(w.negPoints.point1.y);
                System.out.println("=== Point 2 Neg ===");
                System.out.println(w.negPoints.point2.x);
                System.out.println(w.negPoints.point2.y);
            }
        }

        //starts the thread frame which handles writing and pushing to cRIO
        Thread frameThread = new Thread(new RunnableThread("Frame", wheels));
        frameThread.start();
    }
}