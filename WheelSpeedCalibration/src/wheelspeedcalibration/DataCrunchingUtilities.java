/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelspeedcalibration;

import java.util.ArrayList;

/**
 * This class holds functions designed to do number crunching and filtering on the data stored in the Wheel objects
 * @author Kyle
 */
public class DataCrunchingUtilities
{
    /**
     * Takes the values from the given ArrayList and converts index into power value for each point,
     * as well as splits the array list into forward and backward
     * @param arrayList ArrayList of entire wheel
     * @return DoubleArrayList of normalized values and split between forward and backward
     */
    public static DoubleArrayList normalizeValues(ArrayList<GraphPoint> arrayList)
    {
        DoubleArrayList dblArrayList = new DoubleArrayList();
        ArrayList<GraphPoint> posArrayList = new ArrayList<>();
        ArrayList<GraphPoint> negArrayList = new ArrayList<>();
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
     * Takes in ArrayList of forward (POS Values) or backward (NEG Values), and computes lines, slope, y intercept, 
     * and two points to draw lines in Swing
     * @param arrayList takes in ArrayList and returns DualPoint for the LeastSquaredRegression
     * @param sign Use the ints in WheelSpeedCalibrationMap called FORWARD and 
     * BACKWARD, FORWARD is a positive value, BACKWARD is a negative value. This value 
     * is used to get a point with a negative X value for backwards, and a positive X value for forwards
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
     * Filters out outliers in data so if values are more than a certain amount away from LeastSquaredRegression line they 
     * will be set as unused.
     * @param arrayList to filter
     * @param a y intercept
     * @param b slope
     */
    private static void checkForOutliers(ArrayList<GraphPoint> arrayList, double a, double b)
    {
        for (GraphPoint p: arrayList)
        {
            if (WheelSpeedCalibrationMap.POINT_Y_BAND < Math.abs((p.power) - (a + (p.speed * b))))
            {
                p.used = false;
            }
        }
    }
    
    /**
     * Given slope and y intercept, computes two points on the Least Squared Regression line and places them in a DualPoint wrapper
     * @param a y intercept
     * @param b slope
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
     * Reads the speed value of every data point and if the value equals "NaN" or "0.0", will set the point as unused
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
}