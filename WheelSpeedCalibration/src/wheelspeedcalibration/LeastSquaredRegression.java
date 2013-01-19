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

    public static LinePoint LeastSquaredRegresstion(ArrayList<GraphPoint> arrayList)
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

        for (GraphPoint p : arrayList)
        {
            sumX += p.power;
            sumY += p.speed;
            sumXX += (p.power * p.power);
            sumYY += (p.speed * p.speed);
            sumXY += (p.power * p.speed);
            numData++;
        }
        meanX = sumX / numData;
        meanY = sumY / numData;

        //slope
        b = sumXY / sumXX;
        //yint
        a = meanY - b * meanX;
        //r term
        r = Math.sqrt((sumXY * sumXY) / (sumXX * sumYY));
        

        return null;
    }
    
    private static DualPoint computePoint(double bpos, double apos)
    {        
        //abs
        int minX = 0;
        //abs
        int maxX = 100;
        DualPoint dualPoint = new DualPoint();
        //y = bpos * x + apos   
        dualPoint.point1.y = computeY(maxX, bpos, apos);
        dualPoint.point1.x = maxX;
        dualPoint.point2.y = computeY(minX, bpos, apos);
        dualPoint.point2.x = minX;
        return dualPoint;
    }

    private static double computeY(double xVal, double slope, double yint)
    {
        //y = mx + b
        double y = slope * xVal + yint;
        return y;
    }
}
