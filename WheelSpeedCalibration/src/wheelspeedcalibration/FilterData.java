/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelspeedcalibration;

import java.util.ArrayList;

/**
 * Filters out zero vals
 * @author Kyle
 */
public class FilterData
{

    /**
     * sets used bool to false on the Points w/ speed val of 0.0.
     *
     * @param arrayList takes given arrrayList
     * @return array w/ zeros set to not used
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

    public static ArrayList<GraphPoint> removeOutliers(ArrayList<GraphPoint> arrayList)
    {
        double lowAverageSpeed = 0;
        double highAverageSpeed = 0;
        GraphPoint p;
        GraphPoint temp;
        for (int i = 0; i < arrayList.size(); i++)
        {
            //gets the current val from the list
            p = arrayList.get(i);

            //if not in the first 5 or last 5
            if (i >= 5 && i < arrayList.size() - 5)
            {
                //System.out.println(i);
                //gets the bottom 5 vals average
                for (int j = i - 5; j < i; j++)
                {
                    temp = arrayList.get(j);
                    lowAverageSpeed += temp.speed;
                }
                //gets the top 5 vals average
                lowAverageSpeed = lowAverageSpeed / 5;

                for (int j = i + 1; j < i + 6; j++)
                {
                    temp = arrayList.get(j);
                    highAverageSpeed += temp.speed;
                }
                highAverageSpeed = highAverageSpeed / 5;
            }

            //gets the absolute value of difference between the average of the top and bottom and the actual val 
            double absValOfPointDifference = Math.abs(((lowAverageSpeed + highAverageSpeed) / 2) - p.speed);

            //if p.speed is less than or greater than certan amount different that previous and post 5 results,
            //ignors first and last 5            
            //ensures not already set to not used
            if (absValOfPointDifference > WheelSpeedCalibrationMap.MIN_VAL_TO_FILTER_VAL
                    && (i >= 5 && i < arrayList.size()) && p.used)
            {
                if (WheelSpeedCalibrationMap.FILTER_DATA_DEBUG)
                {
                    System.out.println(p.speed + " Outlier");
                    System.out.println("Num in list: " + i
                            + " Average: " + ((lowAverageSpeed + highAverageSpeed) / 2)
                            + " Actual Val: " + p.speed);
                }
                p.used = false;
            }
        }
        return arrayList;
    }
}
