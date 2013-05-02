/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Solenoid;

/**
 *
 * @author kyle
 */
public class LifterObject
{
    //Singleton instance
    private static LifterObject instance;

    //Solenoid objects
    private Solenoid arms;
    //Arm position constants
    public static final boolean ARM_UP = false;
    public static final boolean ARM_DOWN = true;

    public LifterObject()
    {
        arms = new Solenoid(RobotMap.ROBOT_LIFTER_CHANNEL);
    }

    /**
     * Returns the single instance of this class
     * @return
     */
    public static LifterObject getInstance()
    {
	if (instance == null)
        {
            instance = new LifterObject();
	}
	return instance;
    }

    /**
     * Set the position of the arm to either PneumaticArm.ARM_UP or PneumaticArm.ARM_DOWN
     * True puts the arm down, False picks the arm up
     * @param position
     */
    public void moveArms(boolean position)
    {
        arms.set(position);

    }
}