/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.smartdashboard.gui;

import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

/**
 *
 * @author brad
 */
public class DashboardPrefs {
    public static final String AUTOCREATE_KEY = "AutoCreate";
    public static final String CAMERAVIDEOTEAMNUMBER_KEY = "CameraVideoTeamNumber";
    public static final String DEBUGVIDEOERRORS_KEY = "debugVideoErrors";
    public static final String HEIGHT_KEY = "Height";
    public static final String SHOWCAMERAVIDEO_KEY = "ShowCameraVideo";
    public static final String WIDTH_KEY = "Width";
    public static final String X_KEY = "X";
    public static final String Y_KEY = "Y";
    public static final String SNAPTOGRID_KEY = "snapToGrid";
    public static final String LOGTOCSV_ENABLE_KEY = "logToCSVEnable";
    public static final String LOGTOCSV_FILEPATH_KEY = "logToCSVFilePath";
    
    Preferences node;
    private static DashboardPrefs instance = null;

    private DashboardPrefs() {
	node = Preferences.userNodeForPackage(getClass());
    }

    public static DashboardPrefs getInstance() {
	if (instance == null) {
	    instance = new DashboardPrefs();
	}
	return instance;
    }

    public int getWidth() {
	return node.getInt(WIDTH_KEY, 500);
    }

    public void setWidth(int width) {
	node.putInt(WIDTH_KEY, width);
    }

    public int getHeight() {
	return node.getInt(HEIGHT_KEY, 300);
    }

    public void setHeight(int height) {
	node.putInt(HEIGHT_KEY, height);
    }

    public int getX() {
	return node.getInt(X_KEY, 0);
    }

    public void setX(int x) {
	node.putInt(X_KEY, x);
    }

    public int getY() {
	return node.getInt(Y_KEY, 0);
    }

    public void setY(int y) {
	node.putInt(Y_KEY, y);
    }

    public boolean getAutoCreate() {
	return node.getBoolean(AUTOCREATE_KEY, true);
    }

    public void setAutoCreate(boolean autoCreate) {
	node.putBoolean(AUTOCREATE_KEY, autoCreate);
    }

    public void setShowCameraVideo(boolean selected) {
	node.putBoolean(SHOWCAMERAVIDEO_KEY, selected);
    }

    public boolean getShowCameraVideo() {
	return node.getBoolean(SHOWCAMERAVIDEO_KEY, false);
    }

    public void setCameraVideoTeamNumber(int team) {
        node.putInt(CAMERAVIDEOTEAMNUMBER_KEY, team);
    }

    public int getCameraVideoTeamNumber() {
        return node.getInt(CAMERAVIDEOTEAMNUMBER_KEY, 0);
    }

    public void setDebugVideoErrors(boolean value) {
        node.putBoolean(DEBUGVIDEOERRORS_KEY, value);
    }

    public boolean getDebugVideoErrors() {
        return node.getBoolean(DEBUGVIDEOERRORS_KEY, false);
    }

    public void setSnapToGrid(boolean value) {
        node.putBoolean(SNAPTOGRID_KEY, value);
    }

    public boolean getSnapToGrid() {
        return node.getBoolean(SNAPTOGRID_KEY, false);
    }

    public boolean getLogToCSVEnabled() {
        return node.getBoolean(LOGTOCSV_ENABLE_KEY, false);
    }

    public void setLogToCSVEnabled(boolean value) {
        node.putBoolean(LOGTOCSV_ENABLE_KEY, value);
    }

    public String getLogToCSVFilePath() {
        return node.get(LOGTOCSV_FILEPATH_KEY, null);
    }

    public void setLogToCSVFilePath(String value) {
        if(value != null)
            node.put(LOGTOCSV_FILEPATH_KEY, value);
    }

    public void addPreferenceChangeListener(PreferenceChangeListener pcl) {
        node.addPreferenceChangeListener(pcl);
    }

    public void removePreferenceChangeListener(PreferenceChangeListener pcl) {
        node.removePreferenceChangeListener(pcl);
    }
}
