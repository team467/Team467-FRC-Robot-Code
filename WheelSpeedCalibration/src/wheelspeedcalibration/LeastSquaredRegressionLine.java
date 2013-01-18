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
    
    public void LeastSquaredRegresstion(ArrayList<Point> arrayList) 
    {
        int sumX = 0;
        int sumY = 0;
       
        //gets the sum of all the 'x's and 'y's
        for(Point p: arrayList)
        {
            sumX = sumX + (int)(p.power);
            sumY = sumY + (int)(p.speed);
        } 
        //x vals^2
        int sumXX = sumX * sumX;
        //y vals^2       
        int sumYY = sumY * sumY;
        //x vals * y vals
        int sumXY = sumX * sumY;
        //number of data points
        int numData = arrayList.size();
        //bar x (average of 'x's)
        int meanX = sumX / numData;
        //bar y (average of 'y's)
        int meanY = sumY / numData;            
        //'b' val = slope 
        int b = sumXY / sumXX;
        //'a' val = y intercept
        int a = meanY - b * meanX;
        //'r' val = correlation coefficent
        double r = Math.sqrt((sumXY * sumXY)/(sumXX * sumYY));
    
    }
}
