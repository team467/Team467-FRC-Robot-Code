/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package customdashboard;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 *
 * @author aidan
 */
public class Tables
{
    //Ip of crio
    private static final String CRIO_IP = "10.4.67.2";
    
    //Network table names
    private static final String WHEEL_ANGLE_TABLE = "custom";
    
    //All network table objects
    public static NetworkTable wheelAngleTable;
    
    /**
     * Sets up the network table singleton and the individual network table objects
     */
    public static void initialize()
    {
        //Create and initialize network table
        NetworkTable.setClientMode();
        NetworkTable.setIPAddress(CRIO_IP);
        
        //Create inidividual network tables
        wheelAngleTable = NetworkTable.getTable(WHEEL_ANGLE_TABLE);
    }
}
