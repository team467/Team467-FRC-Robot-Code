/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelspeedcalibration;

import java.util.ArrayList;

/**
 * Wheel object put in an ArrayList wheels. Stores DoublePoints for lines to be drawn. Also holds the name of each wheel,
 * the Key value, the ArrayList for GraphPoints. Key refers to the prefix used to filter out string in front of data in
 * preferences file
 * @author Kyle
 */
public class Wheel 
{   
    public String name = "";
    public String key = "";
    //holds the vals 0 - 255 w/ their speed and power vals
    public ArrayList<GraphPoint> points = new ArrayList<GraphPoint>();
    //object which is wrapper for pos array list and neg array list
    public DoubleArrayList doubleArrayList;
    //warpper for 2 points to graph lines
    public DualPoint posPoints;
    public DualPoint negPoints;
    
    public int numUsedPoints;
    
    //constructor for info in obj
    public Wheel(String newName, String newKey)
    {
        name = newName;
        key = newKey;
    }
}
