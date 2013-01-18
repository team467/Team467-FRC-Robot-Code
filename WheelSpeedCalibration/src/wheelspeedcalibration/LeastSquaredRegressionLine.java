/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelspeedcalibration;

import java.util.ArrayList;

/**
 *
 * @author Cameron Knight
 */
public class LeastSquaredRegressionLine
{

    public static void LeastSquaredRegresstion(ArrayList<Point> arrayList)
    {
        /*
         * Note to what I need to do:
         * need to split the array into 2 parts and then write to file
         */

        //pos vals
        int sumXpos = 0;
        int sumYpos = 0;
        //number of data points
        int numDataPos = 0;

        //neg vals
        int sumXneg = 0;
        int sumYneg = 0;
        //number of data points
        int numDataNeg = 0;


        //gets the sum of all the 'x's and 'y's
        for (Point p : arrayList)
        {
            if (p.used)
            {
                //pos vals
                if (p.power >= 0)
                {
                    System.out.println(sumXpos);
                    sumXpos = sumXpos + (int) (p.power);
                    sumYpos = sumYpos + (int) (p.speed);
                    numDataPos += 1;
                }
                //neg vals
                else
                {
                    System.out.println(sumXneg);
                    sumXneg = sumXneg + (int) (p.power);
                    sumYneg = sumYneg + (int) (p.speed);
                    numDataNeg += 1;
                }
            }
        }
        
        //===============pos vals===================
        //x vals^2
        int sumXXpos = sumXpos * sumXpos;
        //y vals^2       
        int sumYYpos = sumYpos * sumYpos;
        //x vals * y vals
        int sumXYpos = sumXpos * sumYpos;

        //numData = arrayList.size();

        //bar x (average of 'x's)
        int meanXpos = sumXpos / numDataPos;
        //bar y (average of 'y's)
        int meanYpos = sumYpos / numDataPos;
        //===========================================
        
        
        //===========Neg Vals========================
        //x vals^2
        int sumXXneg = sumXneg * sumXneg;
        //y vals^2       
        int sumYYneg = sumYneg * sumYneg;
        //x vals * y vals
        int sumXYneg = sumXneg * sumYneg;

        //numData = arrayList.size();

        //bar x (average of 'x's)
        int meanXneg = sumXneg / numDataNeg;
        //bar y (average of 'y's)
        int meanYneg = sumYneg / numDataNeg;
        //============================================
        
        
        //======final slope vals=====================
        //'b' val = slope 
        int bpos = sumXYpos / sumXXpos;
        //'a' val = y intercept
        int apos = meanYpos - bpos * meanXpos;
        //'r' val = correlation coefficent
        double rpos = Math.sqrt((sumXYpos * sumXYpos) / (sumXXpos * sumYYpos));               

        /////////////////////////////////////////////
        
        //'b' val = slope 
        int bneg = sumXYneg / sumXXneg;
        //'a' val = y intercept
        int aneg = meanYneg - bneg * meanXneg;
        //'r' val = correlation coefficent
        double rneg = Math.sqrt((sumXYneg * sumXYneg) / (sumXXneg * sumYYneg));
        //==============================================
    }
}
