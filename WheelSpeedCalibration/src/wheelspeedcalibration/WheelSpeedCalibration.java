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
        wheels.add(new Wheel("FrontRight", "FrontRightC"));
        wheels.add(new Wheel("FrontLeft", "FrontLeftC"));
        wheels.add(new Wheel("BackRight", "BackRightC"));
        wheels.add(new Wheel("BackLeft", "BackLeftC"));

        if (!WheelSpeedCalibrationMap.OFF_LINE_MODE)
        {
            FTPClass.connectToServer(ServerOperationEnum.PULL);
        }

        ParseFile.readAndParseFile();

        for (Wheel w : wheels)
        {
            w.points = FilterData.removeZeros(w.points);
            w.doubleArrayList = NormalizePowerValues.normalizeValues(w.points);
//            System.out.println("Neg Vals ==================================================");
//            for (GraphPoint p : w.doubleArrayList.negArrayList)
//            {
//                System.out.println("power: " + p.power);
//                System.out.println("speed: " + p.speed);
//            }  
//            System.out.println("Pos Vals ==================================================");
//            for (GraphPoint p : w.doubleArrayList.posArrayList)
//            {
//                System.out.println("power: " + p.power);
//                System.out.println("speed: " + p.speed);
//            }
            w.negPoints = LeastSquaredRegression.LeastSquaredRegresstion(w.doubleArrayList.negArrayList, -1);
            w.posPoints = LeastSquaredRegression.LeastSquaredRegresstion(w.doubleArrayList.posArrayList, 1);
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

        Thread frameThread = new Thread(new RunnableThread("Frame", wheels));
        frameThread.start();
    }
}