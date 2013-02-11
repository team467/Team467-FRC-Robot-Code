/*
 * FIRST Team 467 Wheel Speed Calibration Utility
 * http://www.shrewsburyrobotics.org/
 * 
 * Copyright 2013 FIRST Team 467
 * Free to use under the GPLv2 license.
 * http://www.gnu.org/licenses/gpl-2.0.html
 *
 */
package wheelspeedcalibration;

import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Calibrator used with a dynamometer to calculate the proper slopes for each
 * wheel to run each wheel at an RPS/Speed instead of at a PWM
 *
 * @author Kyle
 */
public class WheelSpeedCalibration
{

    public static ArrayList<Wheel> wheels = new ArrayList<Wheel>();

    /**
     * Main Class - This is where all code starts
     *
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        updateGraph();
        setUINimbus();
        NewFrame f = new NewFrame();
        f.repaint();
//        Thread frameThread = new Thread(new RunnableThread("Frame", wheels));
//        frameThread.start();
    }

    private static void setUINimbus()
    {
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(NewFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(NewFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(NewFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(NewFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Called to calculate the lines and graph points
     */
    public static void updateGraph()
    {
        WheelSpeedCalibrationMap.regraphing = true;
        //creates the 4 wheels
        wheels.add(new Wheel("FrontRight", "FrontRightC"));
        wheels.add(new Wheel("FrontLeft", "FrontLeftC"));
        wheels.add(new Wheel("BackRight", "BackRightC"));
        wheels.add(new Wheel("BackLeft", "BackLeftC"));


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

            //prints out points for each line to draw
            if (WheelSpeedCalibrationMap.DEBUG_MODE)
            {
                System.out.println("=== Wheel: " + w.name);
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

        for (Wheel w : wheels)
        {
            DataCrunchingUtilities.numUsedVals(w);
        }

        WheelSpeedCalibrationMap.regraphing = false;
    }
}