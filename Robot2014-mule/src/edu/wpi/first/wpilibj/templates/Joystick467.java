/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author Spencer
 */
public class Joystick467 {
    private Joystick joystick;
    private boolean[] buttons = new boolean[12];
    private boolean[] prevButtons = new boolean[12];
    private double stickX = 0.0;
    private double stickY = 0.0;
    private double hatX = 0.0;
    private double hatY = 0.0;
    private double twist = 0.0;
    private boolean flap = false;
    
    public static final int TRIGGER = 1;
    private static final double DEADZONE = 0.1;
    
    private static final int AXIS_X = 1;
    private static final int AXIS_Y = 2;
    private static final int TWIST_AXIS = 3;
    private static final int FLAP_AXIS = 4;
    private static final int HAT_AXIS_X = 5;
    private static final int HAT_AXIS_Y = 6;
    
    /**
     * Create a new joystick on a given channel
     * @param stick 
     */
    public Joystick467(int stick) {
        joystick = new Joystick(stick);
    }
    
    /**
     * Read all inputs from the underlying joystick object.
     */
    public void readInputs() {
        // read all buttons
        for (int i = 1; i <= 12; i++) {
            prevButtons[i - 1] = buttons[i - 1];
            buttons[i - 1] = joystick.getRawButton(i);
        }
        
        //Read Joystick Axes
        flap = joystick.getRawAxis(FLAP_AXIS) < 0.0;
        stickY = filterJoystickInput(joystick.getRawAxis(AXIS_Y));
        stickX = filterJoystickInput(joystick.getRawAxis(AXIS_X));
        twist = filterJoystickInput(joystick.getRawAxis(TWIST_AXIS));
        hatX = joystick.getRawAxis(HAT_AXIS_X);
        hatY = joystick.getRawAxis(HAT_AXIS_Y);
    }
    
    /**
     * Check if a specific button is being held down.
     * @param button
     * @return 
     */
    public boolean buttonDown(int button) {
        return buttons[button - 1] && prevButtons[button - 1];
    }
    
    /**
     * Check if a specific button has just been pressed. (Ignores holding.)
     * @param button
     * @return 
     */
    public boolean buttonPressed(int button) {
        return buttons[button - 1] && !prevButtons[button - 1];
    }
    
    /**
     * Check if a specific button has just been released.
     * @param button
     * @return 
     */
    public boolean buttonReleased(int button) {
        return !buttons[button - 1] && prevButtons[button - 1];
    }
    
    public double getStickX() {
        return stickX;
    }
    
    public double getStickY() {
        return stickY;
    }
    
    public double getHatX() {
        return hatX;
    }
    
    public double getHatY() {
        return hatY;
    }
    
    public boolean getFlap() {
        return flap;
    }
    
    public double getTwist() {
        return twist;
    }
        
    /**
     * Calculate the distance of the stick from the center position, preserving the sign of the Y
     * component
     * 
     * @return
     */
    public double getStickDistance()
    {
        return Math.sqrt(stickX * stickX + stickY * stickY);
    }
    
    /**
     * Calculate the angle of this joystick.
     * @return Joystick Angle in range -1.0 to 1.0
     */
    public double getStickAngle()
    {
        // This shouldn't be necessary, deadzone filtering should already
        // be performed - however it doesn't hurt to make sure.
        if ((Math.abs(stickX) < DEADZONE) && (Math.abs(stickY) < DEADZONE))
        {
            return 0.0;
        }

        if (stickY == 0.0)
        {
            // In Y deadzone avoid divide by zero error
            return (stickX > 0.0) ? 0.5 : -0.5;
        }

        // Return value in range -1.0 to 1.0

        double stickAngle = MathUtils.atan(stickX / -stickY);

        if (stickY > 0)
        {   
            stickAngle += (stickX > 0) ? Math.PI : -Math.PI;
        }

        return (stickAngle / (Math.PI));
    }
    
    /**
     * Implement a dead zone for Joystick centering - and a non-linear
     * acceleration as the user moves away from the zero position.
     * @param input
     * @return
     */
    private double filterJoystickInput(double input)
    {
        // Ensure that there is a dead zone around zero
        if (Math.abs(input) < DEADZONE)
        {
            return 0.0;
        }
        // Simply square the input to provide acceleration
        // ensuring that the sign of the input is preserved
        return (input * Math.abs(input));
    }
}
