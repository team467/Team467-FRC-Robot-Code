/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelspeedcalibration;

import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * This class holds functions designed to do number crunching and filtering on
 * the data stored in the Wheel objects
 *
 * @author Kyle
 */
public class Utilities
{

    /**
     * Takes the values from the given ArrayList and converts index into power
     * value for each point, as well as splits the array list into forward and
     * backward
     *
     * @param arrayList ArrayList of entire wheel
     * @return DoubleArrayList of normalized values and split between forward
     *         and backward
     */
    public static DoubleArrayList normalizeValues(ArrayList<GraphPoint> arrayList)
    {
        DoubleArrayList dblArrayList = new DoubleArrayList();
        ArrayList<GraphPoint> posArrayList = new ArrayList<GraphPoint>();
        ArrayList<GraphPoint> negArrayList = new ArrayList<GraphPoint>();
        double indexVal;
        for (GraphPoint p : arrayList)
        {
            indexVal = (double) (p.index);
            p.power = -1 + (indexVal / ((double) (arrayList.size()) / 2.0));
            if (p.power >= 0)
            {
                posArrayList.add(p);
            }
            else
            {
                negArrayList.add(p);
            }
            //System.out.println(p.power);
        }
        dblArrayList.posArrayList = posArrayList;
        dblArrayList.negArrayList = negArrayList;
        return dblArrayList;
    }

    /**
     * Takes in ArrayList of forward (POS Values) or backward (NEG Values), and
     * computes lines, slope, y intercept, and two points to draw lines in Swing
     *
     * @param arrayList takes in ArrayList and returns DualPoint for the
     *                  LeastSquaredRegression
     * @param sign      Use the ints in WheelSpeedCalibrationMap called FORWARD
     *                  and BACKWARD, FORWARD is a positive value, BACKWARD is a
     *                  negative value. This value is used to get a point with a
     *                  negative X value for backwards, and a positive X value
     *                  for forwards
     * @return DualPoint w/ the two points for drawing lines in the Frame
     */
    public static DualPoint LeastSquaredRegression(ArrayList<GraphPoint> arrayList, int sign)
    {
        double sumX = 0;
        double sumY = 0;
        double sumXX = 0;
        double sumYY = 0;
        double sumXY = 0;
        double meanX = 0;
        double meanY = 0;
        double b = 0;
        double a = 0;
        double r = 0;
        int numData = 0;
        double x;
        double y;

        for (GraphPoint p : arrayList)
        {
            if (p.used)
            {
                x = p.speed;
                y = p.power;
                sumX += x;
                sumY += y;
                sumXX += (x * x);
                sumYY += (y * y);
                sumXY += (x * y);
                numData++;
            }
        }
        double N = (double) numData;
        meanX = sumX / N;
        meanY = sumY / N;

        //Equation: y = a + b * x
        //b = slope
        b = ((N * sumXY) - (sumX * sumY)) / ((N * sumXX) - (sumX * sumX));
        //System.out.println("Slope: " + b);
        // y = a + b * x, so a = y - b * x
        //a = yint
        a = ((sumY * sumXX) - (sumX * sumXY)) / ((N * sumXX) - (sumX * sumX));
        //System.out.println("Y Int: " + a);
        //r term
        r = Math.sqrt((sumXY * sumXY) / (sumXX * sumYY));

        DualPoint dp = computePoint(a, b, sign);


        checkForOutliers(arrayList, a, b);

        return dp;
    }

    /**
     * Calculates the number of used values for each wheel
     *
     * @param wheel wheel object to calculate number of used values from
     */
    public static void numUsedVals(Wheel wheel)
    {
        int numUsed = 0;
        for (GraphPoint p : wheel.points)
        {
            if (p.used)
            {
                numUsed++;
            }
        }
        wheel.numUsedPoints = numUsed;
    }

    /**
     * Filters out outliers in data so if values are more than a certain amount
     * away from LeastSquaredRegression line they will be set as unused.
     *
     * @param arrayList to filter
     * @param a         y intercept
     * @param b         slope
     */
    private static void checkForOutliers(ArrayList<GraphPoint> arrayList, double a, double b)
    {
        for (GraphPoint p : arrayList)
        {
            if (WheelSpeedCalibrationMap.POINT_Y_BAND < Math.abs((p.power) - (a + (p.speed * b))))
            {
                p.used = false;
            }
        }
    }

    /**
     * Given slope and y intercept, computes two points on the Least Squared
     * Regression line and places them in a DualPoint wrapper
     *
     * @param a    y intercept
     * @param b    slope
     * @param sign forward (POS) or backward (NEG)
     * @return DualPoint with the two points for drawing line in Swing
     */
    public static DualPoint computePoint(double a, double b, int sign)
    {
        //Given a = y-intercept and b = slope, computes two
        //end points that can be used to plot this line
        //y = a + b * x   

        double minX = 0.0;
        double maxX = 8.0;
        DualPoint dualPoint = new DualPoint();
        dualPoint.point1.x = sign * maxX;
        dualPoint.point1.y = computeY((sign * maxX), a, b);

        dualPoint.point2.x = sign * minX;
        dualPoint.point2.y = computeY((sign * minX), a, b);

        dualPoint.slope = b;
        dualPoint.slope = a;
        return dualPoint;
    }

    /**
     * Private class used for computing the Y value
     *
     * @param x X Value in the y = a + b * x
     * @param a Y intercept in the y = a + b * x
     * @param b Slope in the y = a + b * x
     * @return double containing the Y value for the X value given
     */
    private static double computeY(double x, double a, double b)
    {
        //y = a + b * x
        double y = a + (b * x);
        return y;
    }

    /**
     * Reads the speed value of every data point and if the value equals "NaN"
     * or "0.0", will set the point as unused
     *
     * @param arrayList takes each wheel data ArrayList
     * @return Same data ArrayList but w/ zeros set to unused
     */
    public static ArrayList<GraphPoint> removeZeros(ArrayList<GraphPoint> arrayList)
    {
        for (GraphPoint p : arrayList)
        {
            if (p.speed == 0.0)
            {
                p.used = false;
            }
        }
        return arrayList;
    }

    /**
     * Adds string to current display of data on output. Must be called just before printOutputConsole() in the frame
     * @param line Line to append to output window
     */
    public static void appendOutputWindow(String line)
    {
        WheelSpeedCalibrationMap.outputText = WheelSpeedCalibrationMap.outputText + line + "\n";        
    }

    /**
     * Resets string for printing in the output window
     */
    public static void resetOutputWindow()
    {
        WheelSpeedCalibrationMap.outputText = "";        
    }
    
    /**
     * Creates an error message with a custom body.
     * @param errorMessage String holding error message body
     */
    public static void showErrorBox(String errorMessage)
    {
        JOptionPane.showMessageDialog(null, errorMessage, "Error!", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Returns the number of unused values from each wheel
     *
     * @param wheelName name of wheel to check
     * @return int containing the number of unused values
     */
    public static int returnNumUsedVals(String wheelName)
    {
        int numUsedPoints = 0;
        for (Wheel w : WheelSpeedCalibration.wheels)
        {
            System.out.println("GIven: " + wheelName + " wname: " + w.name);
            if (w.name.equals(wheelName))
            {
                System.out.println(w.numUsedPoints + "Usedpts");
                numUsedPoints = w.numUsedPoints;
            }
        }
        return numUsedPoints;
    }
}
