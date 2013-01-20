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

    public static DualPoint LeastSquaredRegresstion(ArrayList<GraphPoint> arrayList)
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
            x = p.speed;
            y = p.power;
            sumX += x;
            sumY += y;
            sumXX += (x * x);
            sumYY += (y * y);
            sumXY += (x * y);
            numData++;
        }
        double N = (double) numData;
        meanX = sumX / N;
	meanY = sumY / N;		

        //Equation: y = a + b * x
        //b = slope
        b = ((N * sumXY) - (sumX * sumY)) / ((N * sumXX) - (sumX * sumX));
        
        // y = a + b * x, so a = y - b * x
        //a = yint
        a = ((sumY * sumXX) - (sumX * sumXY)) / ((N * sumXX) - (sumX * sumX));

        //r term
        r = Math.sqrt((sumXY * sumXY) / (sumXX * sumYY));
        
        DualPoint dp = computePoint(a, b);
        
        return dp;
    }
    
    private static DualPoint computePoint(double a, double b)
    {        
        //Given a = y-intercept and b = slope, computes two
        //end points that can be used to plot this line
        //y = a + b * x   

        double minX = 0.0;
        double maxX = 10.0;
        DualPoint dualPoint = new DualPoint();
        dualPoint.point1.x = maxX;
        dualPoint.point1.y = computeY(maxX, a, b);

        dualPoint.point2.x = minX;
        dualPoint.point2.y = computeY(minX, a, b);
        return dualPoint;
    }

    private static double computeY(double x, double a, double b)
    {
        //y = a + b * x
        double y = a + b * x;
        return y;
    }
}
