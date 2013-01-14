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
public class Wheel 
{   
    public String Name = "";
    public String Key = "";
    public ArrayList<Point> points = new ArrayList<>();
    
    public Wheel(String newName, String newKey)
    {
        Name = newName;
        Key = newKey;
    }
}
