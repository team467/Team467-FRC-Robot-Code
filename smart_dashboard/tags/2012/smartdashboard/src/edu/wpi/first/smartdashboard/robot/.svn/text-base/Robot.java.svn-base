/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.smartdashboard.robot;

import edu.wpi.first.wpilibj.networking.NetworkTable;

/**
 *
 * @author Joe
 */
public class Robot {

    public static final String PREF_SAVE_FIELD = "~S A V E~";
    public static final String TABLE_NAME = "SmartDashboard";
    public static final String PREFERENCES_NAME = "Preferences";

    public static NetworkTable getTable() {
        return NetworkTable.getTable(TABLE_NAME);
    }

    public static NetworkTable getPreferences() {
        return NetworkTable.getTable(PREFERENCES_NAME);
    }
}
