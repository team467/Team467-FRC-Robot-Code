/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelspeedcalibration;

import java.awt.Color;
import java.awt.Toolkit;

/**
 * Map of all variables used throughout the code
 * @author Kyle
 */
public class WheelSpeedCalibrationMap
{
    public static String outputText = "";

    //used in least squared regression to indicate if to compute the points on the line should
    //have a positive X value or a negative X value
    public static final int FORWARD = 1;
    public static final int BACKWARD = -1;

//    public static final int NUM_BUTTONS_GUI = 7;

    //used to lock draw function while the file is being read and computed thru
    public static boolean regraphing = false;

    public static final double JFREECHART_GRAPH_X_RANGE = 1.1;
    public static final double JFREECHART_GRAPH_Y_RANGE = 8.0;
    
    public static final double TOO_LARGE_VALUE_RANGE = 10.0;

    //Colors used for each wheel, plus
    public static final Color UNUSED_COLOR = Color.BLUE;
    public static final Color FRONT_LEFT_COLOR = Color.decode("#A020F0"); //Purple (X11)
    public static final Color FRONT_RIGHT_COLOR = Color.decode("#008800");// Dark Green
    public static final Color BACK_RIGHT_COLOR = Color.decode("#D50000"); //Rosso corso
    public static final Color BACK_LEFT_COLOR = Color.decode("#0014A8");//Zaffre
    public static final Color BACKGROUND_COLOR = Color.decode("#DDDDDD");//Periwinkle
    public static final Color GRIDLINE_COLOR = Color.RED;//Color.decode("#999999");//Periwinkle

    //val used to flter out vals above and below this range
    public static final double POINT_Y_BAND = 0.2;

    public static boolean pullFromRobot = false;
    public static boolean preferencesNotExistFlag = false;
    public static final boolean DEBUG_MODE = false;

    public static final String IP_ADDRESS_CRIO = "10.4.67.2";
    public static final String CRIO_USERNAME = "anonymous";
    public static final String CRIO_PASSWORD = "";

    public static final String PATH_TO_ROBOT_FILE = "wpilib-preferences.ini";
    //public static final String PATH_TO_LOCAL_FILE = System.getProperty("user.home") + "/wpilib-preferences.ini";
    public static final String PATH_TO_DEV_FILE = System.getProperty("user.dir") + "/wpilib-preferences.ini";

    //States used in drawing lines
    public static final int FRONT_RIGHT = 0;
    public static final int FRONT_LEFT = 1;
    public static final int BACK_RIGHT = 2;
    public static final int BACK_LEFT = 3;

    //used to move the frame bottom above the window bar
    public static final int BOTTOM_BAR_HEIGHT = 40;

    public static final double MIN_VAL_TO_FILTER_VAL = 2.0;
    public static final boolean FILTER_DATA_DEBUG = false;
    public static final int SCREEN_X_RANGE = 16;
    public static final int SCREEN_Y_RANGE = 2;
    public static final int GRAPH_SIZE_Y = Toolkit.getDefaultToolkit().getScreenSize().height - BOTTOM_BAR_HEIGHT;
    //sets graph to be square
    public static final int GRAPH_SIZE_X = GRAPH_SIZE_Y;
    public static final int SCREEN_SIZE_X = Toolkit.getDefaultToolkit().getScreenSize().width - 50; //make up for Ubuntu Side Bar
    public static final int SCREEN_SIZE_Y = GRAPH_SIZE_Y;
    public static final int SIZE_X_SCALING = GRAPH_SIZE_X / SCREEN_X_RANGE;
    public static final int GRID_SQUARE_SIZE = 2;
}
