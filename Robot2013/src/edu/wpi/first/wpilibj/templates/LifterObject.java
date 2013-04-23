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

    private static LifterObject instance;

    private LifterObject lifterobject;
    //Solenoid objects
    private Solenoid armLeft;
    private Solenoid armRight;
    //Arm position constants
    public static final boolean ARM_UP = false;
    public static final boolean ARM_DOWN = true;

    public LifterObject()
    {
        lifterobject = LifterObject.getInstance();
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
        armLeft.set(position);
        armRight.set(position);
    }
}