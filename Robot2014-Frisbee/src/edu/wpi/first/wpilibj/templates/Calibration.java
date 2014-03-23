/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author shrewsburyrobotics This class contains only static variables and
 * functions, and simply acts as a container for all the calibration code.
 */
public class Calibration
{
    //Creates objects
    private static Drive drive;
    private static DataStorage data;
    private static Driverstation driverstation;
    
    //Incremented angle used for calibrating wheels
    private static double calibrationAngle = 0.0;

    /**
     * Initialize calibration code
     */
    public static void init()
    {
        //makes the objects
        drive = Drive.getInstance();
        data = DataStorage.getInstance();
        driverstation = Driverstation.getInstance();
    }

    /**
     * Updates steering calibration
     *
     * @param motorId The id of the motor to calibrate
     */
    public static void updateSteeringCalibrate(int motorId)
    {
        Joystick467 joy = driverstation.getRightJoystick();
        
        //Drive motor based on twist angle
        //Increase wheel angle by a small amount based on joystick twist
        calibrationAngle += joy.getTwist() / 100.0;

        if (calibrationAngle > 1.0)
        {
            calibrationAngle -= 2.0;
        }
        if (calibrationAngle < -1.0)
        {
            calibrationAngle += 2.0;
        }

        //Drive specified steering motor with no speed to allow only steering
        drive.individualSteeringDrive(calibrationAngle, 0, motorId);

        //Write and set new center if trigger is pressed
        if (joy.buttonPressed(Joystick467.TRIGGER))
        {
            double currentAngle = drive.getSteering(motorId).getSensorValue();
            
            System.out.println(currentAngle);

            //Write data to robot
            data.putDouble(RobotMap.STEERING_KEYS[motorId], currentAngle);
            data.save();

            //Set new steering center
            drive.getSteering(motorId).setCenter(currentAngle);

            //Reset calibration angle
            calibrationAngle = 0.0;
        }
    }
}
