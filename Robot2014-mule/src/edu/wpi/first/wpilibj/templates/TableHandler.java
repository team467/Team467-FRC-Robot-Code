/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 *
 * @author aidan
 */
public class TableHandler
{
    //All network table objects
    public static NetworkTable wheelAngleTable;
    
    /**
     * Create all the network table objects
     */
    public static void init()
    {
        wheelAngleTable = NetworkTable.getTable("wheel angles");
    }
}
