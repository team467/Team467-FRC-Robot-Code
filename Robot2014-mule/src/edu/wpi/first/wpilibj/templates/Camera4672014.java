/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.camera.*;

/**
 *
 * @author Spencer
 */
public class Camera4672014 implements Runnable {
    private static Camera4672014 instance = null;
    private AxisCamera cam;
    
    private Camera4672014() {
        cam = AxisCamera.getInstance();
    }
    
    public static Camera4672014 getInstance() {
        if (instance == null) {
            instance = new Camera4672014();
        }
        return instance;
    }
    
    public void run() {
        
    }
}
