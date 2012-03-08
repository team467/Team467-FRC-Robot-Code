/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

/**
 *
 * @author shrewsburyrobotics This class contains only static variables and functions,
 * and simply acts as a container for all the calibration code.
 */
public class Calibration 
{   
    //Creates objects
    private static GearTooth467 geartooth;
    private static Drive drive;
    private static Memory data;
    private static Driverstation driverstation;
    
    //Number of teeth on the gear
    private final static int TOOTH_NUMBER = 50;
    
    //Amount to increment power each iteration
    private final static double INCREMENT_VALUE = 1.0 / 128.0;
    
    //Creates 4 arrays, 1 for each motor    
    private static double[][] motorSpeeds = new double[][]
    {
        new double[256], //Front left
        new double[256], //Front right
        new double[256], //Back left
        new double[256]  //Back right
    };
    
    //Total iterations, to 256
    private static int iterationTicker = 0;
    
    //Time per iteration of spinning, 50
    private static int timeTicker = 0;
    
    //Incremented angle used for calibrating wheels
    private static double calibrationAngle = 0.0;
    
    //double for motor speed
    private static double motorPower = 0.0;
    
    //Trigger debounce
    private static boolean trigDebounce = false;
    
    
    //Note these values are based on the graph of power on the x axis
    //and speed on the y axis - they are flipped in code
    
    //Function constants
//    private static final double FL_POSITIVE_START = (RobotMap.MAIN_ROBOT == true) ? -0.125000 : -0.16406;
//    private static final double FL_NEGATIVE_START = (RobotMap.MAIN_ROBOT == true) ?  0.078125 :  0.117188;
//    private static final double FR_POSITIVE_START = (RobotMap.MAIN_ROBOT == true) ? -0.125000 : -0.08594;
//    private static final double FR_NEGATIVE_START = (RobotMap.MAIN_ROBOT == true) ?  0.093750 : 0.0625;
//    private static final double BL_POSITIVE_START = (RobotMap.MAIN_ROBOT == true) ? -0.085940 : -0.08594;
//    private static final double BL_NEGATIVE_START = (RobotMap.MAIN_ROBOT == true) ?  0.062500 : 0.0625;
//    private static final double BR_POSITIVE_START = (RobotMap.MAIN_ROBOT == true) ? -0.085940 : -0.08594;
//    private static final double BR_NEGATIVE_START = (RobotMap.MAIN_ROBOT == true) ?  0.062500 : 0.0625;
//    
//    private static final double FL_POSITIVE_SLOPE = (RobotMap.MAIN_ROBOT == true) ? 0.667067 : 0.6572422;
//    private static final double FL_NEGATIVE_SLOPE = (RobotMap.MAIN_ROBOT == true) ? 0.720215 : 0.689697;
//    private static final double FR_POSITIVE_SLOPE = (RobotMap.MAIN_ROBOT == true) ? 0.699345 : 0.686553;
//    private static final double FR_NEGATIVE_SLOPE = (RobotMap.MAIN_ROBOT == true) ? 0.719246 : 0.633446;
//    private static final double BL_POSITIVE_SLOPE = (RobotMap.MAIN_ROBOT == true) ? 0.686553 : 0.686553;
//    private static final double BL_NEGATIVE_SLOPE = (RobotMap.MAIN_ROBOT == true) ? 0.633446 : 0.633446;
//    private static final double BR_POSITIVE_SLOPE = (RobotMap.MAIN_ROBOT == true) ? 0.686553 : 0.686553;
//    private static final double BR_NEGATIVE_SLOPE = (RobotMap.MAIN_ROBOT == true) ? 0.633446 : 0.633446;
    
    private static final double FL_POSITIVE_START = (RobotMap.MAIN_ROBOT == true) ?  0.0 : -0.16406;
    private static final double FL_NEGATIVE_START = (RobotMap.MAIN_ROBOT == true) ?  0.0 :  0.117188;
    private static final double FR_POSITIVE_START = (RobotMap.MAIN_ROBOT == true) ?  0.0 : -0.08594;
    private static final double FR_NEGATIVE_START = (RobotMap.MAIN_ROBOT == true) ?  0.0 : 0.0625;
    private static final double BL_POSITIVE_START = (RobotMap.MAIN_ROBOT == true) ?  0.0 : -0.08594;
    private static final double BL_NEGATIVE_START = (RobotMap.MAIN_ROBOT == true) ?  0.0 : 0.0625;
    private static final double BR_POSITIVE_START = (RobotMap.MAIN_ROBOT == true) ?  0.0 : -0.08594;
    private static final double BR_NEGATIVE_START = (RobotMap.MAIN_ROBOT == true) ?  0.0 : 0.0625;
    
    private static final double FL_POSITIVE_SLOPE = (RobotMap.MAIN_ROBOT == true) ?  0.7 : 0.6572422;
    private static final double FL_NEGATIVE_SLOPE = (RobotMap.MAIN_ROBOT == true) ?  0.7 : 0.689697;
    private static final double FR_POSITIVE_SLOPE = (RobotMap.MAIN_ROBOT == true) ?  0.7 : 0.686553;
    private static final double FR_NEGATIVE_SLOPE = (RobotMap.MAIN_ROBOT == true) ?  0.7 : 0.633446;
    private static final double BL_POSITIVE_SLOPE = (RobotMap.MAIN_ROBOT == true) ?  0.7 : 0.686553;
    private static final double BL_NEGATIVE_SLOPE = (RobotMap.MAIN_ROBOT == true) ?  0.7 : 0.633446;
    private static final double BR_POSITIVE_SLOPE = (RobotMap.MAIN_ROBOT == true) ?  0.7 : 0.686553;
    private static final double BR_NEGATIVE_SLOPE = (RobotMap.MAIN_ROBOT == true) ?  0.7 : 0.633446;
    
           
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
//        for (int i = 0; i < 4; i++)
//        {
//            motorSpeeds[i] = data.getDoubleArray(RobotMap.CALIBRATION_SPEED_KEYS[i], motorSpeeds[i], 256);
//        }
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
    
    //Wheel calibration variables
    static boolean calibratingWheels = false;
    static boolean calibrationComplete = false;
    
    //Wheel calibration state constants
    static final int STATE_RAMP_DOWN = 0;
    static final int STATE_UP = 1;
    static final int STATE_FINISHED = 2;
    
    //Wheel calibration state
    static int state = 0;
    
    /**
     * Update wheel calibration
     * @param motorId The id of the motor to calibrate
     */
    public static void updateWheelCalibrate(int motorId)
    {   
        if (calibratingWheels)
        {
            switch (state)
            {
                case STATE_RAMP_DOWN:
                    //Print state to the driverstation
                    driverstation.println("Calibrating...", 5);
                    
                    if (timeTicker < 100)
                    {
                        motorPower -= 0.01;
                        drive.individualWheelDrive(motorPower, motorId);
                        timeTicker++;
                    }
                    else
                    {
                        timeTicker = 0;
                        state = STATE_UP;
                    }
                    break;
                case STATE_UP:
                    if (iterationTicker < 256)
                    {
                        //Print state to the driverstation
                        driverstation.println("Calibrating...", 5);

                        if (timeTicker <= 12)
                        {
                            drive.individualWheelDrive(motorPower, motorId);
                            timeTicker++;
                        }
                        else
                        {
                            double speed = geartooth.getAngularSpeed();
                            System.out.println("Current Speed: " + speed + 
                                "   Writing Power: " + motorPower);
                            
                            //Calculate positive or negative direction and account for discrepencies in
                            //direction the speed shoud be moving in (speed for one power is lower
                            //than speed for a greater power etc.)
                            if (iterationTicker < 128)
                            {
                                motorSpeeds[motorId][iterationTicker] = -speed;
                            }
                            else
                            {
                                motorSpeeds[motorId][iterationTicker] = speed;
                            }
                            
                            //Move to next power
                            motorPower += INCREMENT_VALUE;
                            timeTicker = 0;
                            iterationTicker++;
                        }
                    }
                    else
                    {
                        iterationTicker = 0;
                        state = STATE_FINISHED;
                    }
                    break;
                case STATE_FINISHED:
                    //Stop wheel moving
                    drive.individualWheelDrive(0.0, motorId);
                    
                    if (!calibrationComplete)
                    {
                        //Prin speeds for debugging
                        System.out.println("Original Speeds:");
                        for (int i = 0; i < 256; i++)
                        {
                            System.out.println( i + " - " + motorSpeeds[motorId][i]);
                        }
                        System.out.println();
                        
                        //Write speed array to the cRIO
                        data.putDoubleArray(RobotMap.CALIBRATION_SPEED_KEYS[motorId], motorSpeeds[motorId]);
                        data.save();

                        //Signal that the calibration is complete
                        calibrationComplete = true;
                    }
                    else
                    {
                        //Print completed calibration state to the driverstation
                        driverstation.println("Calibration Complete!", 5);
                    }
                    break;
            }
        }
        
    }
    
    /**
     * Start the wheels being calibrated
     */
    public static void toggleWheelCalibrate()
    {
        //Alternate wheel calibration boolean
        calibratingWheels = !calibratingWheels;
        
        if (calibratingWheels)
        {
            //Starts the sensor
            geartooth.reset();
            geartooth.start();
        
            //Reset
            iterationTicker = 0;
            timeTicker = 0;
            motorPower = 0.0;
            state = STATE_RAMP_DOWN;
            calibrationComplete = false;
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
    
    /**
     * Get the modified wheel power needed for a certain speed
     * @param speed The desired speed
     * @param motorId The id of the wheel
     */
    public static double adjustWheelPower(double speed, int motorId)
    {    
        //Special case for speed of 0
        if (Math.abs(speed) <= 0.1)
        {
            return 0.0;
        }
        
        double power = 0.0;
        if (speed > 0.0)
        {
            switch (motorId)
            {
                case RobotMap.FRONT_LEFT:
                    power = FL_POSITIVE_START + (speed * FL_POSITIVE_SLOPE);
                    break;
                case RobotMap.FRONT_RIGHT:
                    power = FR_POSITIVE_START + (speed * FR_POSITIVE_SLOPE);
                    break;
                case RobotMap.BACK_LEFT:
                    power = BL_POSITIVE_START + (speed * BL_POSITIVE_SLOPE);
                    break;
                case RobotMap.BACK_RIGHT:
                    power = BR_POSITIVE_START + (speed * BR_POSITIVE_SLOPE);
                    break;
            }
        }
        else
        {
            switch (motorId)
            {
                case RobotMap.FRONT_LEFT:
                    power = FL_NEGATIVE_START + (speed * FL_NEGATIVE_SLOPE);
                    break;
                case RobotMap.FRONT_RIGHT:
                    power = FR_NEGATIVE_START + (speed * FR_NEGATIVE_SLOPE);
                    break;
                case RobotMap.BACK_LEFT:
                    power = BL_NEGATIVE_START + (speed * BL_NEGATIVE_SLOPE);
                    break;
                case RobotMap.BACK_RIGHT:
                    power = BR_NEGATIVE_START + (speed * BR_NEGATIVE_SLOPE);
                    break;
            }
        }
        
        //This cancels out all calibration code for testing purposes
        //power = speed;
        
        //Limit speed
        if (power > 1.0) power = 1.0;
        if (power < -1.0) power = -1.0;
        
        return power;
    }
    
    
    
    /**
     * Search through an array of doubles and find the index of the double that
     * is closest to the given key.
     * @param array The array to search through
     * @param key The double to find
     * @param first Start value when searching
     * @param upto End value when searching
     * @return 
     */
    private static int binarySearch(double[] array, double key, int first, int upto)
    {
        while (first < upto)
        {
            int mid = (first + upto) / 2;  // Compute mid point.
            if (key < array[mid])
            {
                //Special case if value is at 0 so negative array value isn't tried
                if (mid == 0)
                {
                    return mid;
                }
                if (key > array[mid - 1])
                {
                    //Determine which index the desired value is closer to
                    double dif = array[mid] - array[mid - 1];
                    if (key - array[mid - 1] < dif / 2.0)
                    {
                        return mid - 1;
                    }
                    else
                    {
                        return mid;
                    }
                }
                else
                {
                    upto = mid;     // repeat search in bottom half.
                }
            }
            else
            {
                if (key > array[mid])
                {
                    first = mid + 1;  // Repeat search in top half.
                }
                else
                {
                    return mid;     // Found it. return position
                }
            }
        }
        return -(first + 1);    // Failed to find key
    }
}
