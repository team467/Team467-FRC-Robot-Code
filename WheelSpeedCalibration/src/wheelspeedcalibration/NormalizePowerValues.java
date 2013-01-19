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
    public static ArrayList<GraphPoint> normalizeValues(ArrayList<GraphPoint> arrayList)
    {
        double indexVal;
        for (GraphPoint p: arrayList)
        {
            indexVal = p.index;
            p.power = -1 + (indexVal / (arrayList.size() / 2));         
            //System.out.println(p.power);
        }
        return arrayList;
    }
}
