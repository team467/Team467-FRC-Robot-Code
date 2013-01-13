/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelspeedcalibration;

import java.util.ArrayList;

/**
 *
 * @author Adam
 */
public class NormalizePowerValues 
{
    // double indexVal = p.index
    // Normalized Value = -1 + (indexVal /(arraylist.size() / 2))
    public static ArrayList<Point> normalizeValues(ArrayList<Point> arrayList)
    {
        double indexVal;
        for (Point p: arrayList)
        {
            indexVal = p.index;
            p.power = -1 + (indexVal / (arrayList.size() / 2));         
        }
        return arrayList;
    }
}
