/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot
{

    Driverstation driverstation = Driverstation.getInstance();
    CANJaguar jag1;
    CANJaguar jag2;
    CANJaguar jag3;
    CANJaguar jag4;
    CANJaguar jag5;
    CANJaguar jag6;
    CANJaguar jag7;
    CANJaguar jag8;
    CANJaguar jag9;
    CANJaguar jag10;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit()
    {
        //This is the only diviation from the standard RobotTemplate
        //Calls the Prefrences object
//        Preferences.getInstance();
        try
        {
            jag1 = new CANJaguar(1);
        }
        catch(Exception ex)
        {
            System.out.println("1 fail");
        }
        try
        {
            jag2 = new CANJaguar(2);
        }
        catch(Exception ex)
        {
            System.out.println("2 fail");
        }
        try
        {
            jag3 = new CANJaguar(3);
        }
        catch(Exception ex)
        {
            System.out.println("3 fail");
        }
        try
        {
            jag4 = new CANJaguar(4);
        }
        catch(Exception ex)
        {
            System.out.println("4 fail");
        }
        try
        {
            jag5 = new CANJaguar(5);
        }
        catch(Exception ex)
        {
            System.out.println("5 fail");
        }
        try
        {
            jag6 = new CANJaguar(6);
        }
        catch(Exception ex)
        {
            System.out.println("6 fail");
        }
        try
        {
            jag7 = new CANJaguar(7);
        }
        catch(Exception ex)
        {
            System.out.println("7 fail");
        }
        try
        {
            jag8 = new CANJaguar(8);
        }
        catch(Exception ex)
        {
            System.out.println("8 fail");
        }
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic()
    {
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic()
    {
        if (driverstation.joystickDriveTrigger)
        {
            try
            {
                jag1.setX(1.0);
            }
            catch (CANTimeoutException ex)
            {
                ex.printStackTrace();
            }
        }
        if (driverstation.joystickDriveButton2)
        {
            try
            {
                jag2.setX(1.0);
            }
            catch (CANTimeoutException ex)
            {
                ex.printStackTrace();
            }
        }
        if (driverstation.joystickDriveButton3)
        {
            try
            {
                jag3.setX(1.0);
            }
            catch (CANTimeoutException ex)
            {
                ex.printStackTrace();
            }
        }
        if (driverstation.joystickDriveButton4)
        {
            try
            {
                jag4.setX(1.0);
            }
            catch (CANTimeoutException ex)
            {
                ex.printStackTrace();
            }
        }
        if (driverstation.joystickDriveButton5)
        {
            try
            {
                jag5.setX(1.0);
            }
            catch (CANTimeoutException ex)
            {
                ex.printStackTrace();
            }
        }
        if (driverstation.joystickDriveButton6)
        {
            try
            {
                jag6.setX(1.0);
            }
            catch (CANTimeoutException ex)
            {
                ex.printStackTrace();
            }
        }
        if (driverstation.joystickDriveButton7)
        {
            try
            {
                jag7.setX(1.0);
            }
            catch (CANTimeoutException ex)
            {
                ex.printStackTrace();
            }
        }
        if (driverstation.joystickDriveButton8)
        {
            try
            {
                jag8.setX(1.0);
            }
            catch (CANTimeoutException ex)
            {
                ex.printStackTrace();
            }
        }
        
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic()
    {
    }
}
