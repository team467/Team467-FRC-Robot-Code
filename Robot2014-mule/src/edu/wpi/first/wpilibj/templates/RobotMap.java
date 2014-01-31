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
    //Steering motor ids in array (DO NOT ALTER)
    public static final int FRONT_LEFT = 0;
    public static final int FRONT_RIGHT = 1;
    public static final int BACK_LEFT = 2;
    public static final int BACK_RIGHT = 3;

    //Drive motors - Jaguar
    public static final int FRONT_LEFT_MOTOR_CHANNEL = 1;
    public static final int FRONT_RIGHT_MOTOR_CHANNEL = 7;
    public static final int BACK_LEFT_MOTOR_CHANNEL = 3;
    public static final int BACK_RIGHT_MOTOR_CHANNEL = 5;

    //Steering motors - Talon
    public static final int FRONT_LEFT_STEERING_MOTOR_CHANNEL = 2;
    public static final int FRONT_RIGHT_STEERING_MOTOR_CHANNEL = 8;
    public static final int BACK_LEFT_STEERING_MOTOR_CHANNEL = 4;
    public static final int BACK_RIGHT_STEERING_MOTOR_CHANNEL = 6;

    //Steering sensors - Analog
    public static final int FRONT_LEFT_STEERING_SENSOR_CHANNEL = 4;
    public static final int FRONT_RIGHT_STEERING_SENSOR_CHANNEL = 3;
    public static final int BACK_LEFT_STEERING_SENSOR_CHANNEL = 5;
    public static final int BACK_RIGHT_STEERING_SENSOR_CHANNEL = 2;

    //Steering motor constant array
    public static final int[] STEERING_MOTOR_CHANNELS = 
    {
        RobotMap.FRONT_LEFT_STEERING_MOTOR_CHANNEL,
        RobotMap.FRONT_RIGHT_STEERING_MOTOR_CHANNEL,
        RobotMap.BACK_LEFT_STEERING_MOTOR_CHANNEL,
        RobotMap.BACK_RIGHT_STEERING_MOTOR_CHANNEL
    };
    
    // Steering sensor constant array
    public static final int[] STEERING_SENSOR_CHANNELS = 
    {
        RobotMap.FRONT_LEFT_STEERING_SENSOR_CHANNEL,
        RobotMap.FRONT_RIGHT_STEERING_SENSOR_CHANNEL,
        RobotMap.BACK_LEFT_STEERING_SENSOR_CHANNEL,
        RobotMap.BACK_RIGHT_STEERING_SENSOR_CHANNEL
    };
    
    //Gyro channel - Analog
    public static final int GYRO_CHANNEL = 1;

    //Calibration channel - Digital
    public static final int CALIBRATION_CHANNEL = 1;
    
    //LED ring channel - Digital
    public static final int LED_CHANNEL = 4;

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
        
    //Number of increments on the steering sensor
    public static final double STEERING_RANGE = 965;
    
    // PID array 
    public static final double[][] PIDvalues = 
    {
        // Front Left PID values
        {
            0.013,  // P
            0.0,    // I
            0.0     // D
        },
        
        // Front Right PID values
        {
            0.010,  // P
            0.0,    // I
            0.0     // D
        },
        
        // Back Left PID values
        {
            0.013,  // P
            0.0,    // I
            0.0     // D
        },
        
        // Back Right PID values
        {
            0.013,  // P
            0.0,    // I
            0.0     // D
        }
    };

}
