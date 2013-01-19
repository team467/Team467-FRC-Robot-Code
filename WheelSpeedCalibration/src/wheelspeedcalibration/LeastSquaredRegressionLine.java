/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelspeedcalibration;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Cameron Knight
 */
public class LeastSquaredRegressionLine
{

    public static void LeastSquaredRegresstion(ArrayList<Wheel> wheelArrayList)
    {
        /*
         * Note to what I need to do:
         * need then write to file
         */
        for (Wheel w : wheelArrayList)
        {
            //pos vals
            double sumXpos = 0;
            double sumYpos = 0;
            //number of data points
            int numDataPos = 0;

            //neg vals
            double sumXneg = 0;
            double sumYneg = 0;
            //number of data points
            int numDataNeg = 0;


            //gets the sum of all the 'x's and 'y's
            for (GraphPoint p : w.points)
            {
                if (p.used)
                {
                    //pos vals
                    if (p.power >= 0)
                    {
                        sumXpos = sumXpos + (p.power);
                        sumYpos = sumYpos + (p.speed);
                        //System.out.println((p.power));
                        numDataPos += 1;
                    }
                    //neg vals
                    else
                    {
                        sumXneg = sumXneg + (p.power);
                        sumYneg = sumYneg + (p.speed);
                        //System.out.println(sumXneg);
                        numDataNeg += 1;
                    }
                }
            }

            //===============pos vals===================
            //x vals^2
            double sumXXpos = sumXpos * sumXpos;
            //y vals^2       
            double sumYYpos = sumYpos * sumYpos;
            //x vals * y vals
            double sumXYpos = sumXpos * sumYpos;

            //numData = arrayList.size();

            //bar x (average of 'x's)
            double meanXpos = sumXpos / numDataPos;
            //bar y (average of 'y's)
            double meanYpos = sumYpos / numDataPos;
            //===========================================


            //===========Neg Vals========================
            //x vals^2
            double sumXXneg = sumXneg * sumXneg;
            //y vals^2       
            double sumYYneg = sumYneg * sumYneg;
            //x vals * y vals
            double sumXYneg = sumXneg * sumYneg;

            //numData = arrayList.size();

            //bar x (average of 'x's)
            double meanXneg = sumXneg / numDataNeg;
            //bar y (average of 'y's)
            double meanYneg = sumYneg / numDataNeg;
            //============================================


            //======final slope vals=====================
            //'b' val = slope 
            double bpos = sumXYpos / sumXXpos;
            //'a' val = y intercept
            double apos = meanYpos - bpos * meanXpos;
            //'r' val = correlation coefficent
            double rpos = Math.sqrt((sumXYpos * sumXYpos) / (sumXXpos * sumYYpos));

            /////////////////////////////////////////////

            //'b' val = slope 
            double bneg = sumXYneg / sumXXneg;
            //'a' val = y intercept
            double aneg = meanYneg - bneg * meanXneg;
            //'r' val = correlation coefficent
            double rneg = Math.sqrt((sumXYneg * sumXYneg) / (sumXXneg * sumYYneg));
            //==============================================

            //============lines for computing ==============
            
            //spits out the 2 line vals per wheel
            QuadPoint qp = computePoint(bpos, apos, bneg, aneg);
            w.posPoint1 = qp.point1pos;
            w.posPoint2 = qp.point2pos;
            w.negPoint1 = qp.point1neg;
            w.negPoint2 = qp.point2neg;

            //==============================================
        }
    }

    /**
     * 
     * @param bpos slope pos
     * @param apos y int pos
     * @param bneg slope neg
     * @param aneg y int pos
     * @return quadPoint
     */
    private static QuadPoint computePoint(double bpos, double apos, double bneg, double aneg)
    {        
        //abs
        int minX = 0;
        //abs
        int maxX = 1;
        QuadPoint quadPoint = new QuadPoint();
        //y = bpos * x + apos   
        quadPoint.point1pos.y = computeY(maxX, bpos, apos);
        quadPoint.point1pos.x = maxX;
        quadPoint.point2pos.y = computeY(minX, bpos, apos);
        quadPoint.point2pos.x = minX;
        quadPoint.point1neg.y = computeY(-maxX, bneg, aneg);
        quadPoint.point1neg.x = -maxX;
        quadPoint.point2neg.y = computeY(-minX, bneg, aneg);
        quadPoint.point2neg.x = -minX;
        return quadPoint;
    }
    
    
    private static double computeY(double xVal, double slope, double yint)
    {
        double y = slope * xVal + yint;
        return y;        
    }

    static class QuadPoint
    {
        LinePoint point1pos = new LinePoint();
        LinePoint point2pos = new LinePoint();
        LinePoint point1neg = new LinePoint();
        LinePoint point2neg = new LinePoint();
    }
}
