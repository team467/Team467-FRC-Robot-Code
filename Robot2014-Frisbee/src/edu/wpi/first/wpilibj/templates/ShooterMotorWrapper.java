/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Jaguar;

/**
 *
 * @author Spencer
 */
public class ShooterMotorWrapper {
    private Jaguar motor;
    
    private static ShooterMotorWrapper instance;
    
    public static ShooterMotorWrapper getInstance() {
        if (instance == null) {
            instance = new ShooterMotorWrapper(RobotMap.SHOOTER_CHANNEL);
        }
        
        return instance;
    }
    
    private ShooterMotorWrapper(int channel) {
        motor = new Jaguar(channel);
    }
    
    public void drive(double speed) {
        motor.set(speed);
    }
}
