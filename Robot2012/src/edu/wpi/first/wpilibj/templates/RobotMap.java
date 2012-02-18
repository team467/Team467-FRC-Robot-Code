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
    
    //Drive motors
    public static final int FRONT_LEFT_MOTOR_CHANNEL = 10;
    public static final int FRONT_RIGHT_MOTOR_CHANNEL = 2;
    public static final int BACK_LEFT_MOTOR_CHANNEL = 6;
    public static final int BACK_RIGHT_MOTOR_CHANNEL = 5;
    
    //Steering motors
    public static final int FRONT_LEFT_STEERING_MOTOR_CHANNEL = 9;
    public static final int FRONT_RIGHT_STEERING_MOTOR_CHANNEL = 3;
    public static final int BACK_LEFT_STEERING_MOTOR_CHANNEL = 8;
    public static final int BACK_RIGHT_STEERING_MOTOR_CHANNEL = 4;
    
    //Steering sensors
    public static final int FRONT_LEFT_STEERING_SENSOR_CHANNEL = 4;
    public static final int FRONT_RIGHT_STEERING_SENSOR_CHANNEL = 3;
    public static final int BACK_LEFT_STEERING_SENSOR_CHANNEL = 5;
    public static final int BACK_RIGHT_STEERING_SENSOR_CHANNEL = 2;
    
    //Calibration channel
    public static final int CALIBRATION_CHANNEL = 0; //TBD
    
    //Arm channel
    public static final int ARM_LOWER_CHANNEL = 0; //TBD
    public static final int ARM_RAISE_CHANNEL = 0; //TBD
    
    //Compressor Channel
    public static final int COMPRESSOR_CHANNEL = 0; //TBD
    
    //Llamahead motors
    public static final int LLAMAHEAD_LAUNCH_MOTOR_CHANNEL = 7; //TBD
    public static final int LLAMAHEAD_SCOOP_MOTOR_CHANNEL = 3; //TBD
    public static final int LLAMAHEAD_INTAKE_MOTOR_CHANNEL = 1; //TBD
    public static final int LLAMAHEAD_NECK_MOTOR_CHANNEL = 2; //TBD
    
    //Llamahead sensors
    public static final int LLAMAHEAD_LAUNCH_SPEED_SENSOR_CHANNEL = 0; //TBD
    public static final int LLAMAHEAD_BALL_FIRST_SENSOR_CHANNEL = 0; //TBD
    public static final int LLAMAHEAD_BALL_SECOND_SENSOR_CHANNEL = 0; //TBD
    public static final int LLAMAHEAD_BALL_THIRD_SENSOR_CHANNEL = 0; //TBD
       
    //Llamahead constants
    public static final int LLAMAHEAD_TEETH = 0; //TBD
    
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
    
    
}
