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
    
    public static final int GYRO_CHANNEL = 2;
    
    public static final double SHOOTER_RUN_SPEED = 1.0;

    //Steering motor ids in array (DO NOT ALTER)
    public static final int FRONT_LEFT = 0;
    public static final int FRONT_RIGHT = 1;
    public static final int BACK_LEFT = 2;
    public static final int BACK_RIGHT = 3;
    //Drive motors - Jaguar
    public static final int FRONT_LEFT_MOTOR_CHANNEL = 1;
    public static final int BACK_LEFT_MOTOR_CHANNEL = 3;    
    public static final int BACK_RIGHT_MOTOR_CHANNEL = 5;
    public static final int FRONT_RIGHT_MOTOR_CHANNEL = 7;    
    //Steering motors - Jaguar
    public static final int FRONT_LEFT_STEERING_MOTOR_CHANNEL = 2;
    public static final int BACK_LEFT_STEERING_MOTOR_CHANNEL = 4;
    public static final int BACK_RIGHT_STEERING_MOTOR_CHANNEL = 6;
    public static final int FRONT_RIGHT_STEERING_MOTOR_CHANNEL = 8;    
    //Steering sensors - Analog
    public static final int FRONT_LEFT_STEERING_SENSOR_CHANNEL = 4;
    public static final int FRONT_RIGHT_STEERING_SENSOR_CHANNEL = 1;
    public static final int BACK_LEFT_STEERING_SENSOR_CHANNEL = 3;
    public static final int BACK_RIGHT_STEERING_SENSOR_CHANNEL = 7; //was 2 before worlds
    //Steering ranges
    public static final double FRONT_LEFT_SENSOR_RANGE = 963.0;
    public static final double FRONT_RIGHT_SENSOR_RANGE = 963.0;
    public static final double BACK_LEFT_SENSOR_RANGE = 960.0;
    public static final double BACK_RIGHT_SENSOR_RANGE = 963.0;
    
    public static final String FRONT_RIGHT_STEERING_KEY = "FrontRight";
    public static final String FRONT_LEFT_STEERING_KEY = "FrontLeft";
    public static final String BACK_RIGHT_STEERING_KEY = "BackRight";
    public static final String BACK_LEFT_STEERING_KEY = "BackLeft";
                    

    /* =========================================================================
     * PID Values
     * =========================================================================
     */
    //Front Left PID values
    public static final double FRONT_LEFT_STEERING_PID_P = 0.02; //TBD
    public static final double FRONT_LEFT_STEERING_PID_I = 0.0; //TBD
    public static final double FRONT_LEFT_STEERING_PID_D = 0.0; //TBD
    //Front Right PID values
    public static final double FRONT_RIGHT_STEERING_PID_P = -0.015; //TBD
    public static final double FRONT_RIGHT_STEERING_PID_I = 0.0; //TBD
    public static final double FRONT_RIGHT_STEERING_PID_D = 0.0; //TBD
    //Back Left PID values
    public static final double BACK_LEFT_STEERING_PID_P = -0.01; //TBD -0.015
    public static final double BACK_LEFT_STEERING_PID_I = 0.0; //TBD
    public static final double BACK_LEFT_STEERING_PID_D = 0.0; //TBD
    //Back Right PID values
    public static final double BACK_RIGHT_STEERING_PID_P = -0.01; //TBD -0.015
    public static final double BACK_RIGHT_STEERING_PID_I = 0.0; //TBD
    public static final double BACK_RIGHT_STEERING_PID_D = 0.0; //TBD    
}
