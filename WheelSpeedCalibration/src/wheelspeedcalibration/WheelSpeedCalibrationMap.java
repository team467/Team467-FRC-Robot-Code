/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelspeedcalibration;

/**
 *
 * @author Kyle
 */
public class WheelSpeedCalibrationMap
{
    
    public static boolean OFF_LINE_MODE = true;
    public static boolean DEBUG_MODE = false;
    
    public static final String IP_ADDRESS_CRIO = "10.4.67.2";
    public static final String CRIO_USERNAME = "anonymous";
    public static final String CRIO_PASSWORD = "";
    
    public static final String PATH_TO_ROBOT_FILE = "wpilib-preferences.ini";
    public static final String PATH_TO_LOCAL_FILE = System.getProperty("user.home") + "\\wpilib-preferences.ini";
    
    public static final int FRONT_RIGHT = 0;
    public static final int FRONT_LEFT = 1;
    public static final int BACK_RIGHT = 2;
    public static final int BACK_LEFT = 3;
    
    public static final double MIN_VAL_TO_FILTER_VAL = 2.0;
    public static final boolean FILTER_DATA_DEBUG = false;
    public static final int SCREEN_X_RANGE = 16;
    public static final int SCREEN_Y_RANGE = 2;
    public static final int GRAPH_SIZE_X = 512;
    public static final int GRAPH_SIZE_Y = 512;
    public static final int SCREEN_SIZE_X = GRAPH_SIZE_X + 200;
    public static final int SCREEN_SIZE_Y = GRAPH_SIZE_Y;
    public static final int SIZE_X_SCALING = GRAPH_SIZE_X / SCREEN_X_RANGE;
    public static final int GRID_SQUARE_SIZE = 2;
}
