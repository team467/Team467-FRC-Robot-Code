/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelspeedcalibration;

import java.awt.Point;
import java.util.ArrayList;

/**
 * Wheel obj used to hold vals
 * @author Adam
 */
public class Wheel 
{   
    public String Name = "";
    public String Key = "";
    //holds the vals 0 - 255 w/ their speed and power vals
    public ArrayList<GraphPoint> points = new ArrayList<>();
    //object which is wrapper for pos array list and neg array list
    public DoubleArrayList doubleArrayList;
    //warpper for 2 points to graph lines
    public DualPoint posPoints;
    public DualPoint negPoints;
    
    //constructor for info in obj
    public Wheel(String newName, String newKey)
    {
        Name = newName;
        Key = newKey;
    }
}
