/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author aidan
 */
public class RobotMap
{
    /* =========================================================================
     * Map of robot values
     * =========================================================================
     */
    
    //Steering motor ids in array (DO NOT ALTER)
    public static final int FRONT_LEFT = 0;
    public static final int FRONT_RIGHT = 1;
    public static final int BACK_LEFT = 2;
    public static final int BACK_RIGHT = 3;
    
    //Drive motors - Jaguar
    public static final int FRONT_LEFT_MOTOR_CHANNEL = 3;
    public static final int FRONT_RIGHT_MOTOR_CHANNEL = 9;
    public static final int BACK_LEFT_MOTOR_CHANNEL = 4;
    public static final int BACK_RIGHT_MOTOR_CHANNEL = 10;
    
    //Steering motors - Jaguar
    public static final int FRONT_LEFT_STEERING_MOTOR_CHANNEL = 2;
    public static final int FRONT_RIGHT_STEERING_MOTOR_CHANNEL = 12;
    public static final int BACK_LEFT_STEERING_MOTOR_CHANNEL = 5;
    public static final int BACK_RIGHT_STEERING_MOTOR_CHANNEL = 11;
    
    //Steering sensors - Analog
    public static final int FRONT_LEFT_STEERING_SENSOR_CHANNEL = 4;
    public static final int FRONT_RIGHT_STEERING_SENSOR_CHANNEL = 1;
    public static final int BACK_LEFT_STEERING_SENSOR_CHANNEL = 3;
    public static final int BACK_RIGHT_STEERING_SENSOR_CHANNEL = 2;
    
    //Steering ranges
    public static final double FRONT_LEFT_SENSOR_RANGE = 963.0;
    public static final double FRONT_RIGHT_SENSOR_RANGE = 963.0;
    public static final double BACK_LEFT_SENSOR_RANGE = 960.0;
    public static final double BACK_RIGHT_SENSOR_RANGE = 963.0;
    
    //Calibration channel - Digital
    public static final int CALIBRATION_CHANNEL = 1;
    
    //Data keys (names used when saving centers to robot)
    public static final String[] STEERING_KEYS = new String[]
    {
        "FrontLeft", "FrontRight", "BackLeft", "BackRight"       
    };
    
    //Data keys (names used when saving centers to robot)
    public static final String[] CALIBRATION_SPEED_KEYS = new String[]
    {
        "FrontLeftC", "FrontRightC", "BackLeftC", "BackRightC"      
    };
    
    /* =========================================================================
     * PID Values
     * =========================================================================
     */
    
    //Front Left PID values
    private static final double FRONT_LEFT_STEERING_PID_P = 0.02; //TBD
    private static final double FRONT_LEFT_STEERING_PID_I = 0.0; //TBD
    private static final double FRONT_LEFT_STEERING_PID_D = 0.0; //TBD
    
    //Front Right PID values
    private static final double FRONT_RIGHT_STEERING_PID_P = -0.015; //TBD
    private static final double FRONT_RIGHT_STEERING_PID_I = 0.0; //TBD
    private static final double FRONT_RIGHT_STEERING_PID_D = 0.0; //TBD
    
    //Back Left PID values
    private static final double BACK_LEFT_STEERING_PID_P = -0.015; //TBD
    private static final double BACK_LEFT_STEERING_PID_I = 0.0; //TBD
    private static final double BACK_LEFT_STEERING_PID_D = 0.0; //TBD
    
    //Back Right PID values
    private static final double BACK_RIGHT_STEERING_PID_P = -0.015; //TBD
    private static final double BACK_RIGHT_STEERING_PID_I = 0.0; //TBD
    private static final double BACK_RIGHT_STEERING_PID_D = 0.0; //TBD
    
    //PID array (DO NOT ALTER)
    public static final double[][] pidValues =
    {
        //Front Left PID values
        {
            FRONT_LEFT_STEERING_PID_P,
            FRONT_LEFT_STEERING_PID_I,
            FRONT_LEFT_STEERING_PID_D
        },
        //Front Right PID values
        {
            FRONT_RIGHT_STEERING_PID_P,
            FRONT_RIGHT_STEERING_PID_I,
            FRONT_RIGHT_STEERING_PID_D
        },
        //Back Left PID values
        {
            BACK_LEFT_STEERING_PID_P,
            BACK_LEFT_STEERING_PID_I,
            BACK_LEFT_STEERING_PID_D
        },
        //Back Right PID values
        {
            BACK_RIGHT_STEERING_PID_P,
            BACK_RIGHT_STEERING_PID_I,
            BACK_RIGHT_STEERING_PID_D
        }
    };
}
