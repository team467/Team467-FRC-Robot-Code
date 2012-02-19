/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author aidan
 * This class serves simply as a way of storing all the steering PID values
 */
public class PIDValues
{
    //Front Left PID values
    private static final double FRONT_LEFT_STEERING_PID_P = 0.02; //TBD
    private static final double FRONT_LEFT_STEERING_PID_I = 0.0; //TBD
    private static final double FRONT_LEFT_STEERING_PID_D = 0.0; //TBD

    //Front Right PID values
    private static final double FRONT_RIGHT_STEERING_PID_P = 0.02; //TBD
    private static final double FRONT_RIGHT_STEERING_PID_I = 0.0; //TBD
    private static final double FRONT_RIGHT_STEERING_PID_D = 0.0; //TBD
    
    //Back Left PID values
    private static final double BACK_LEFT_STEERING_PID_P = 0.02; //TBD
    private static final double BACK_LEFT_STEERING_PID_I = 0.0; //TBD
    private static final double BACK_LEFT_STEERING_PID_D = 0.0; //TBD

    //Back Right PID values
    private static final double BACK_RIGHT_STEERING_PID_P = 0.02; //TBD
    private static final double BACK_RIGHT_STEERING_PID_I = 0.0; //TBD
    private static final double BACK_RIGHT_STEERING_PID_D = 0.0; //TBD
    
    //PID array (DO NOT ALTER)
    public static final double[][] values = 
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
