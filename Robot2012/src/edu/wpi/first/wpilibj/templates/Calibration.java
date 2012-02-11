/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

/**
 *
 * @author shrewsburyrobotics
 */
public class Calibration 
{
    private static Memory data;
    private static Driverstation driverstation;
    
    //double for motor speed
    static double motorSpeed = -1;
    
    //creates objects
    private static GearTooth467 geartooth;
    private static Drive drive;
    
    //number of teeth on the gear
    final static int TOOTH_NUMBER = 50;
    
    final static double INCREMENT_VALUE = 1.0 / 128.0;
    
    //creates 4 arrays, 1 for each motor    
    static double[][] motorSpeeds = new double[][]
    {
        new double[256], //Front left
        new double[256], //Front right
        new double[256], //Back left
        new double[256]  //Back right
    };
    
    //total iterations, to 256
    static int iterationTicker = 0;
    //time per iteration of spinning, 50
    static int timeTicker = 0;
    
    //Incremented angle used for calibrating wheels
    static double calibrationAngle = 0.0;
    
    //
    static boolean trigDebounce = false;
    
    /**
     * Initialize calibration code
     */
    public static void init()
    {
        //makes the objects
        geartooth = new GearTooth467(RobotMap.CALIBRATION_CHANNEL, TOOTH_NUMBER);
        drive = Drive.getInstance(); 
        data = Memory.getInstance();
        driverstation = Driverstation.getInstance();
    }
    
    /**
     * Updates steering calibration
     * @param motorId The id of the motor to calibrate
     */
    public static void updateSteeringCalibrate(int motorId)
    {
        //Drive motor based on twist angle
        //Increase wheel angle by a small amount based on joystick twist
        calibrationAngle += driverstation.joystickTwist / 100.0;
        
        if (calibrationAngle > 1.0) calibrationAngle -= 2.0;
        if (calibrationAngle < -1.0) calibrationAngle += 2.0;

        //Drive specified steering motor with no speed to allow only steering
        drive.individualSteeringDrive(calibrationAngle, 0, motorId);
        
        //Write and set new center if trigger is pressed
        if (driverstation.joystickTrigger && !trigDebounce)
        {   
            double currentAngle = drive.getSteeringAngle(motorId);
            
            //Write data to robot
            data.putDouble(RobotMap.STEERING_KEYS[motorId], currentAngle);
            data.save();
            
            //Set new steering center
            drive.setSteeringCenter(motorId, currentAngle);
            
            //Reset calibration angle
            calibrationAngle = 0.0;
            
            trigDebounce = true;
        }
        if (!driverstation.joystickTrigger)
        {
            trigDebounce = false;
        }
    }
    
    static boolean calibratingWheels = false;
    
    /**
     * Update wheel calibration
     * @param motorId The id of the motor to calibrate
     */
    public static void updateWheelCalibrate(int motorId)
    {   
        if (calibratingWheels)
        {
            if (iterationTicker <= 256) 
            {
                if (timeTicker <= 50) 
                {
                    drive.individualWheelDrive(motorSpeed, motorId);
                    timeTicker++;
                } 
                else 
                {
                    motorSpeeds[motorId][iterationTicker] = geartooth.getAngularSpeed();
                    timeTicker = 0;
                }
                //0.0078125 is the difference between each iteration
                motorSpeed = motorSpeed + INCREMENT_VALUE;
                iterationTicker++;
            }
        }
        
    }
    
    /**
     * Start the wheels being calibrated
     */
    public static void switchWheelCalibrate()
    {
        calibratingWheels = !calibratingWheels;
        
        if (calibratingWheels)
        {
            //Starts the sensor
            geartooth.start();
        
            //Resest tickers
            iterationTicker = 0;
            timeTicker = 0;
        }
        else
        {
            stopWheelCalibrate();
        }
        
    }
    
    /**
     * Stop the wheels being calibrated
     */
    public static void stopWheelCalibrate()
    {
        geartooth.stop();
        calibratingWheels = false;
    }
}
