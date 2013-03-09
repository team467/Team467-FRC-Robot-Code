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

/**
 * Calibrator used with a dynamometer to calculate the proper slopes for each
 * wheel to run each wheel at an RPS/Speed instead of at a PWM
 *
 * @author Kyle
 */
public class WheelSpeedCalibration
{

    public static ArrayList<Wheel> wheels;

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

    public static void setUINimbus()
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
        wheels = null;
        wheels = new ArrayList<Wheel>();
        WheelSpeedCalibrationMap.regraphing = true;
        Utilities.resetOutputWindow();        

        //resets wheelvalues so when called to refresh graph it will not use previous values
        for (Wheel w : WheelSpeedCalibration.wheels)
        {
            w.points.clear();
        }
        Utilities.appendOutputWindow("Cleared Previous Values");

        //creates the 4 wheels
        wheels.add(new Wheel("FrontRight", "FrontRightC"));
        wheels.add(new Wheel("FrontLeft", "FrontLeftC"));
        wheels.add(new Wheel("BackRight", "BackRightC"));
        wheels.add(new Wheel("BackLeft", "BackLeftC"));

        Utilities.appendOutputWindow("Adding Wheels");

        //reads through the file and write the values to the wheels ArrayList
        ParseFile.readAndParseFile();

        Utilities.appendOutputWindow("Reading and Parsing File");

        for (Wheel w : wheels)
        {
            Utilities.appendOutputWindow("");
            Utilities.appendOutputWindow("========== " + w.name + " ==========");
            //filter data to remove all "NaN" and "0.0" values
            w.points = Utilities.removeZeros(w.points);

            Utilities.appendOutputWindow("Remove Zeros for " + w.name);

            //normalize data points to have a speed value between -1.0 and 1.0
            w.doubleArrayList = Utilities.normalizeValues(w.points);

            Utilities.appendOutputWindow("Normalized Power Values for " + w.name);

            //removes values from list if val is greater than 
            w.doubleArrayList.negArrayList = Utilities.removeTooLargeValues(w.doubleArrayList.negArrayList);
            w.doubleArrayList.posArrayList = Utilities.removeTooLargeValues(w.doubleArrayList.posArrayList);
            
            //runs line fit on data both forward (POS) and backward (NEG), then filters the data that is more than a 
            // certian distance away from the line to be unused.
            w.negPoints = Utilities.LeastSquaredRegression(w.doubleArrayList.negArrayList, WheelSpeedCalibrationMap.BACKWARD);
            w.posPoints = Utilities.LeastSquaredRegression(w.doubleArrayList.posArrayList, WheelSpeedCalibrationMap.FORWARD);

            //runs fit on data again to make the computed least squared regression line not use the outliers filtered out 
            //by the previous run
            w.negPoints = Utilities.LeastSquaredRegression(w.doubleArrayList.negArrayList, WheelSpeedCalibrationMap.BACKWARD);
            w.posPoints = Utilities.LeastSquaredRegression(w.doubleArrayList.posArrayList, WheelSpeedCalibrationMap.FORWARD);

            Utilities.appendOutputWindow("Least Squared Regression Complete for " + w.name);

            Utilities.appendOutputWindow("Wheel " + w.name + " Positive Slope: '" + String.valueOf(w.posPoints.slope) + "' Positive Y Intercept: '" + String.valueOf(w.posPoints.yint) + "'");
            Utilities.appendOutputWindow("Wheel " + w.name + " Negitive Slope: '" + String.valueOf(w.negPoints.slope) + "' Negitive Y Intercept: '" + String.valueOf(w.negPoints.yint) + "'");
            Utilities.numUsedVals(w);
            Utilities.appendOutputWindow("Number of used values: " + String.valueOf(w.numUsedPoints));

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

        WheelSpeedCalibrationMap.regraphing = false;        
    }
}
