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
public class LeastSquaredRegression
{

    public static DualPoint LeastSquaredRegresstion(ArrayList<GraphPoint> arrayList, int sign)
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

        DualPoint dp = ComputePoints.computePoint(a, b, sign);

        return dp;
    }
}
