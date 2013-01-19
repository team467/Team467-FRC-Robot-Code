/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelspeedcalibration;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Adam
 */
public class Wheel 
{   
    public String Name = "";
    public String Key = "";
    public ArrayList<GraphPoint> points = new ArrayList<>();
    public DoubleArrayList doubleArrayList;
    public LinePoint posPoint1;
    public LinePoint posPoint2;
    public LinePoint negPoint1;
    public LinePoint negPoint2;
    
    public Wheel(String newName, String newKey)
    {
        Name = newName;
        Key = newKey;
    }
}
