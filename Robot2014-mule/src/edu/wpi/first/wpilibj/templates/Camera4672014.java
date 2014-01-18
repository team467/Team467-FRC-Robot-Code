package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.camera.*;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

/**
 *
 * @author Spencer
 */
public class Camera4672014 implements Runnable {
    private static Camera4672014 instance = null;
    private AxisCamera cam;
    private ColorImage currentImage;
    
    private Camera4672014() {
        cam = AxisCamera.getInstance();
        cam.writeResolution(AxisCamera.ResolutionT.k640x480);
        cam.writeCompression(0);
    }
    
    public static Camera4672014 getInstance() {
        if (instance == null) {
            instance = new Camera4672014();
            //new Thread(instance).start();
        }
        return instance;
    }
    
    public void readImage() {
        try {
            if (cam.freshImage()) {
                if (currentImage != null) currentImage.free();
                
                currentImage = cam.getImage();
            }
            if (currentImage == null) System.out.println("[Camera] No image!");
        } catch (AxisCameraException ex) {
            ex.printStackTrace();
        } catch (NIVisionException ex) {
            ex.printStackTrace();
        }
    } 
    
    public int getDetectedParticles() {
        if (currentImage == null) System.out.println("[Camera] No image!"); 
        
        final int HUE_LOW = 0;
        final int HUE_HIGH = 255;
        final int SATURATION_LOW = 0;
        final int SATURATION_HIGH = 255;
        final int LUMINANCE_LOW = 136;
        final int LUMINANCE_HIGH = 255;
        
        final int AREA_LOW = 900;
        final int AREA_HIGH = 1500;
        
        BinaryImage processedImage;
        ParticleAnalysisReport[] reports;
        
        int detectedParticles = 0;
        
        try {
            processedImage = currentImage.thresholdHSL(HUE_LOW, HUE_HIGH, SATURATION_LOW, SATURATION_HIGH, LUMINANCE_LOW, LUMINANCE_HIGH);
            
            reports = processedImage.getOrderedParticleAnalysisReports();
            
            for (int i = 0; i < reports.length; i++) {
                double area = reports[i].particleArea;
                
                if (area > AREA_LOW && area < AREA_HIGH) {
                    detectedParticles++;
                }
            }
        } catch (NIVisionException e) {
            e.printStackTrace();
        }

        return detectedParticles;
    }
    
    public void run() {
        
    }
    
    public int getFreshParticles() {
        readImage();
        return getDetectedParticles();
    }
}
