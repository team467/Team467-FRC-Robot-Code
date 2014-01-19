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
public class Camera467 implements Runnable {
    private static Camera467 instance = null;
    private AxisCamera cam;
    private ColorImage currentImage;
    private Thread cameraThread = null;
    private final int CAMERA_RATE = 50;
    
    private boolean runningCamera = false;
    private boolean readingCamera = false;
    private boolean hasRun = false;
    
    private volatile int numParticles = 0;
    
    private Camera467() {
        cam = AxisCamera.getInstance();
        cam.writeResolution(AxisCamera.ResolutionT.k320x240);
        cam.writeCompression(0);
    }
    
    public static Camera467 getInstance() {
        if (instance == null) {
            instance = new Camera467();
        }
        return instance;
    }
    
    public void startThread() {
        if (cameraThread == null) {
            cameraThread = new Thread(this);
            cameraThread.start();
        } else {
            System.out.println("Thread already started.");
        }
    }
    
    public void killThread() {
        if (cameraThread != null) {
            runningCamera = false;
            cameraThread = null;
        } else {
            System.out.println("No thread to kill.");
        }
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
            long start = System.currentTimeMillis();
            processedImage = currentImage.thresholdHSL(HUE_LOW, HUE_HIGH, SATURATION_LOW, SATURATION_HIGH, LUMINANCE_LOW, LUMINANCE_HIGH);
            System.out.println(System.currentTimeMillis() - start);
            
            reports = processedImage.getOrderedParticleAnalysisReports();
            
            for (int i = 0; i < reports.length; i++) {
                double area = reports[i].particleArea;
                
                if (area > AREA_LOW && area < AREA_HIGH) {
                    detectedParticles++;
                }
            }
            
            processedImage.free();
        } catch (NIVisionException e) {
            e.printStackTrace();
        }
        
        return detectedParticles;
    }
    
    public void run() {
        runningCamera = true;
        
        while (runningCamera) {
            long startTime = System.currentTimeMillis();
            
            if (readingCamera) {
                numParticles = getFreshParticles();
            }
            
            long timeDiff = System.currentTimeMillis() - startTime;
            //System.out.println("timeDiff: " + timeDiff);
            timeDiff = timeDiff > CAMERA_RATE ? CAMERA_RATE : timeDiff;
            
            try {
                // only sample camera at a fixed interval
                Thread.sleep(CAMERA_RATE - timeDiff);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            
            //System.out.println(System.currentTimeMillis() - startTime);
        }
    }
    
    public int getFreshParticles() {
        readImage();
        return getDetectedParticles();
    }
    
    public int getNumParticles() {
        return numParticles;
    }
    
    public void toggleReading() {
        readingCamera = !readingCamera;
    }
    
    public boolean isReading() {
        return readingCamera;
    }
}
