/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelspeedcalibration;

import java.awt.Color;
import java.awt.Toolkit;

/**
 *
 * @author Kyle
 */
public class WheelSpeedCalibrationMap
{
    //used in least squared regression to indicate if to compute the points on the line should
    //have a positive X value or a negative X value
    public static final int FORWARD = 1;
    public static final int BACKWARD = -1;
    
    //used to lock draw function while the file is being read and computed thru
    public static boolean regraphing = false;
    
    //Colors used for each wheel, plus unused
    public static final Color UNUSED_COLOR = Color.BLUE;
    public static final Color FRONT_LEFT_COLOR = Color.ORANGE;
    public static final Color FRONT_RIGHT_COLOR = Color.GREEN;
    public static final Color BACK_RIGHT_COLOR = Color.RED;
    public static final Color BACK_LEFT_COLOR = Color.GRAY;
    
    
    public static final int FRAME_SLEEP = 100;     
    
    //val used to flter out vals above and below this range
    public static final double POINT_Y_BAND = 0.2; 
    
    public static boolean PULL_FROM_ROBOT = false;
    public static boolean DEBUG_MODE = false;
    
    public static final String IP_ADDRESS_CRIO = "10.4.67.2";
    public static final String CRIO_USERNAME = "anonymous";
    public static final String CRIO_PASSWORD = "";
    
    public static final String PATH_TO_ROBOT_FILE = "wpilib-preferences.ini";
    //public static final String PATH_TO_LOCAL_FILE = System.getProperty("user.home") + "\\wpilib-preferences.ini";
    public static final String PATH_TO_DEV_FILE = System.getProperty("user.dir") + "\\wpilib-preferences.ini";
    
    public static final int FRONT_RIGHT = 0;
    public static final int FRONT_LEFT = 1;
    public static final int BACK_RIGHT = 2;
    public static final int BACK_LEFT = 3;
    
    public static final double MIN_VAL_TO_FILTER_VAL = 2.0;
    public static final boolean FILTER_DATA_DEBUG = false;
    public static final int SCREEN_X_RANGE = 16;
    public static final int SCREEN_Y_RANGE = 2;
    public static final int GRAPH_SIZE_Y = Toolkit.getDefaultToolkit().getScreenSize().height;
    //sets graph to be square
    public static final int GRAPH_SIZE_X = GRAPH_SIZE_Y;    
    public static final int SCREEN_SIZE_X = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int SCREEN_SIZE_Y = GRAPH_SIZE_Y;
    public static final int SIZE_X_SCALING = GRAPH_SIZE_X / SCREEN_X_RANGE;
    public static final int GRID_SQUARE_SIZE = 2;
}
