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
       
        for(Point p: arrayList)
        {
            sumX = sumX + (int)(p.power);
            sumY = sumY + (int)(p.speed);
        } 
        int sumXX = sumX * sumX;
        int sumYY = sumY * sumY;
        int sumXY = sumX * sumY;
        int numData = arrayList.size();
        int meanX = sumX / numData;
        int meanY = sumY / numData;
        int b = sumXY / sumXX;
        int a = meanY - b * meanX;
        double r = Math.sqrt((sumXY * sumXY)/(sumXX * sumYY));
    
    }
}
