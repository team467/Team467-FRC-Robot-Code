package edu.shs.first.team467;

import edu.wpi.first.wpilibj.*;

/**
 * Singleton class to control hook mechanism on Team467 2010 Robot
 * @author aidan
 */
public class Hook
{
    //Constants
    private final int HOOK_ARM_EXTENDED_SENSOR_CHANNEL = 14;
    private final int HOOK_ARM_CHANNEL = 4;
    private final int HOOK_WINCH_CHANNEL = 7;
    private final double HOOK_WINCH_SPEED = 1.0;

    //Sensors
    private DigitalInput armExtendedSensor;

    //Motors
    private Relay hookArm;
    private Victor hookWinch;

    // Singleton instance variable
    private static Hook hookInstance;

    // Singleton so constructor is private
    private Hook()
    {
        armExtendedSensor = new DigitalInput(HOOK_ARM_EXTENDED_SENSOR_CHANNEL);
        hookArm = new Relay(HOOK_ARM_CHANNEL);
        hookWinch = new Victor(HOOK_WINCH_CHANNEL);
    }

    // return single instance of this class
	public static synchronized Hook GetInstance()
    {
		if (hookInstance == null)
        {
			hookInstance = new Hook();
		}
		return hookInstance;
	}

    /**
     * Called from main periodic control loop to update the hook in response to
     * driverstation input.
     */
    public void Update()
    {
        Team467DriverStation driverstation = Team467DriverStation.GetInstance();

        //Hook arm code
        if (driverstation.HookArmExtend && armExtendedSensor.get())
        {
            hookArm.set(Relay.Value.kForward);
        }
        else if (driverstation.HookArmRetract)
        {
            hookArm.set(Relay.Value.kReverse);
        }
        else
        {
            hookArm.set(Relay.Value.kOff);
        }

        //Hook winch code
        if (driverstation.HookWinchIn)
        {
            hookWinch.set(-1.0);
        }
        else
        {
            hookWinch.set(0.0);
        }
    }

    /**
     * Used during system bringup to test/validate hook functionality
     */
    public void Tune()
    {
        Team467DriverStation driverstation = Team467DriverStation.GetInstance();

        if (driverstation.HookArmExtend)
        {
            hookArm.set(Relay.Value.kForward);
            System.out.println("OUT");
        }
        else if (driverstation.HookArmRetract)
        {
            hookArm.set(Relay.Value.kReverse);
            System.out.println("IN");
        }
        else
        {
            hookArm.set(Relay.Value.kOff);
        }

        if (driverstation.HookWinchIn)
        {
            hookWinch.set(1.0);
            System.out.println("WinchIN");
        }
        else if (driverstation.XboxLB)
        {
            hookWinch.set(-1.0);
            System.out.println("WinchOUT");
        }
        else
        {
            hookWinch.set(0);
        }

        System.out.println("Arm_Extended_Sensor: " + !armExtendedSensor.get() + " " 
                + driverstation.HookArmExtend + " " + driverstation.HookArmRetract);
    }
}
