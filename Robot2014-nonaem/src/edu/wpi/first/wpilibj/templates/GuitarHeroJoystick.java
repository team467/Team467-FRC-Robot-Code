/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author kyle
 */
public class GuitarHeroJoystick extends Joystick467
{

    //Xbox Buttons
    public static final int GUITAR_TEAL = 1;
    public static final int GUITAR_RED = 2;
    public static final int GUITAR_BLUE = 3;
    public static final int GUITAR_YELLOW = 4;
    public static final int GUITAR_ORANGE = 5;
//    public static final int GUITAR_RB = 6;
    public static final int GUITAR_BACK = 8;
    public static final int GUITAR_START = 7;
//    public static final int GUITAR_LEFTSTICK_CLICK = 9;
//    public static final int GUITAR_RIGHTSTICK_CLICK = 10;
//    public static final int GUITAR_LEFTSTICK_X = 1;
//    public static final int GUITAR_LEFTSTICK_Y = 2;
//    public static final int GUITAR_TRIGGER = 3;  // both triggers on same channel
//    public static final int GUITAR_RIGHTSTICK_X = 4;
//    public static final int GUITAR_RIGHTSTICK_Y = 5;

    public boolean firingState = true;//defaults to firing position

    public GuitarHeroJoystick(int port)
    {
        super(port);
    }

    /**
     * Fires the launcher if true, pulls back if false. Retains state between
     * calls.
     *
     * @return true = fire; false  = cock;
     */
    public boolean getFlap()
    {
        if (this.getJoystick().getRawButton(GUITAR_START) && !firingState)//if fire button down and not fired already
        {
            firingState = true;
        }
        else if (this.getJoystick().getRawButton(GUITAR_BACK) && firingState)//if cock button down and not cocked
        {
            firingState = false;
        }
        return firingState;
    }

    /**
     * Used for feeding in or out. Red button in, yellow button out.
     * @return 1 intake, -1 spit out, 0 no move
     */
    public double getHatY()
    {
        if(this.getJoystick().getRawButton(GUITAR_RED)) //intake
        {
            return 1;
        }
        else if(this.getJoystick().getRawButton(GUITAR_YELLOW))//spit out
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }

    /**
     * Puts the feeder arm up or down. Uses state of Teal button.
     * @return -1 down, 0 up
     */
    public double getStickY()
    {
        if (this.getJoystick().getRawButton(GUITAR_TEAL))//if button down, put arm down
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }
    
    
    
    
}
