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

    public static ArrayList<Wheel> wheels = new ArrayList<>();
    public static DrawLineStates drawLineStates = new DrawLineStates();

    /**
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
            FTPClass.connectToServer(ServerOperationEnum.PULL);
        }

        //reads thryu the file and pull out vals
        ParseFile.readAndParseFile();

        for (Wheel w : wheels)
        {
            //filter data
            w.points = FilterData.removeZeros(w.points);
            //normalize vals
            w.doubleArrayList = NormalizePowerValues.normalizeValues(w.points);

            //runs line fit on data both pos and neg
            w.negPoints = LeastSquaredRegression.LeastSquaredRegresstion(w.doubleArrayList.negArrayList, -1);
            w.posPoints = LeastSquaredRegression.LeastSquaredRegresstion(w.doubleArrayList.posArrayList, 1);
            
            //runs fit on data again to filter unused data out of lines
            w.negPoints = LeastSquaredRegression.LeastSquaredRegresstion(w.doubleArrayList.negArrayList, -1);
            w.posPoints = LeastSquaredRegression.LeastSquaredRegresstion(w.doubleArrayList.posArrayList, 1);

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