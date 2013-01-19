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
    public static DoubleArrayList normalizeValues(ArrayList<GraphPoint> arrayList)
    {
        DoubleArrayList dblArrayList = new DoubleArrayList();
        ArrayList<GraphPoint> posArrayList = new ArrayList<>();
        ArrayList<GraphPoint> negArrayList = new ArrayList<>();
        double indexVal;
        for (GraphPoint p: arrayList)
        {            
            indexVal = p.index;
            p.power = -1 + (indexVal / (arrayList.size() / 2));         
            if (p.power <= 0)
            {
                posArrayList.add(p);
            }
            else
            {
                negArrayList.add(p);
            }
            //System.out.println(p.power);
        }
        dblArrayList.posArrayList = posArrayList;
        dblArrayList.negArrayList = negArrayList;
        return dblArrayList;
    }
}
