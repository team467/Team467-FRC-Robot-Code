/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author Team467
 */
public class RobotMap
{
    public static final double WHEEL_CIRCUMFRENCE = 25.525;
    
    //Steering motor ids in array (DO NOT ALTER)
    public static final int FRONT_LEFT = 0;
    public static final int FRONT_RIGHT = 1;
    public static final int BACK_LEFT = 2;
    public static final int BACK_RIGHT = 3;

    // Drive motors - Talon, digital sidecar
    public static final int FRONT_LEFT_MOTOR_CHANNEL = 1;
    public static final int FRONT_RIGHT_MOTOR_CHANNEL = 7;
    public static final int BACK_LEFT_MOTOR_CHANNEL = 3;
    public static final int BACK_RIGHT_MOTOR_CHANNEL = 5;
    
    // Steering motors - Talon, digital sidecar
    public static final int FRONT_LEFT_STEERING_MOTOR_CHANNEL = 2;
    public static final int FRONT_RIGHT_STEERING_MOTOR_CHANNEL = 8;
    public static final int BACK_LEFT_STEERING_MOTOR_CHANNEL = 4;
    public static final int BACK_RIGHT_STEERING_MOTOR_CHANNEL = 6;

    // Steering sensors - Analog bumper
    public static final int FRONT_LEFT_STEERING_SENSOR_CHANNEL = 1;
    public static final int FRONT_RIGHT_STEERING_SENSOR_CHANNEL = 4;
    public static final int BACK_LEFT_STEERING_SENSOR_CHANNEL = 2;
    public static final int BACK_RIGHT_STEERING_SENSOR_CHANNEL = 3;
    
    // Feeder motor - Analog bumper
    public static final int FEEDER_MOTOR_CHANNEL = 9; 
    
    // Gear tooth sensor channels - Digital sidecar
    public static final int FRONT_LEFT_GEAR_TOOTH_SENSOR_CHANNEL = 1;
    public static final int FRONT_RIGHT_GEAR_TOOTH_SENSOR_CHANNEL = 2;
    public static final int BACK_LEFT_GEAR_TOOTH_SENSOR_CHANNEL = 3;
    public static final int BACK_RIGHT_GEAR_TOOTH_SENSOR_CHANNEL = 4;
    
    public static final int COMPRESSOR_CHANNEL = 0;
    public static final int PRESSURE_SWITCH_CHANNEL = 0;
    
    // Solenoids, pneumatics bumper
    public static final int FEEDER_SOLENOID_CHANNEL = 0;
    public static final int LAUNCHER_SOLENOID_CHANNEL = 0;

    // Steering motor constant array
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

    public static final int FORWARD_DEADZONES = 0;
    public static final int REVERSE_DEADZONES = 1;
    
    //Data keys (names used when saving speeds to robot)
    public static final String[][] CALIBRATION_MOTOR_DEADZONES = new String[][]
    {
        {"FrontLeftForwardDeadzone", "FrontRightForwardDeadzone", "BackLeftForwardDeadzone", "BackRightForwardDeadzone"},
        {"FrontLeftBackDeadzone", "FrontRightBackDeadzone", "BackLeftBackDeadzone", "BackRightBackDeadzone"}
    };
        
    //Number of increments on the steering sensor
    public static final double STEERING_RANGE = 953;
    
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