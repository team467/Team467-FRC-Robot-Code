/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.shs.first.team467;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SmartDashboard;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCamera.WhiteBalanceT;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.*;

/**
 *
 * @author USFIRST
 */
public class Team467Camera implements Runnable
{
    //Team467Camera instance
    private static Team467Camera instance = null;

    //Camera object instance
    AxisCamera cam;

    //Field of view constants
    private final double CAMERA_X_FOV = 55.0; // 55 degree field of view in the X dimension
    private final double CAMERA_Y_FOV = 35.0; // 35 degree field of view in the Y dimension

    // limiting rate in milliseconds to read camera. Camera will wait this
    // time between subsequent image fetches.
    private final int CAMERA_RATE = 100;

    // Camera Image
    private ColorImage image;

    //Particle detection objects
    private ParticleAnalysisReport[] particleResults;;
    private ParticleAnalysisReport largestParticle = null;

    private CamData camData = new CamData();

    //Led channel
    private final int LED_LIGHT_CHANNEL = 2;

    //Led light relay
    private Relay ledLight;

    //Camera image width and height
    private int height = 0;
    private int width = 0;
    
    //Make constructor private so instances can't be created outside class
    private Team467Camera()
    {
        cam = AxisCamera.getInstance();
        cam.writeResolution(AxisCamera.ResolutionT.k160x120);
        cam.writeCompression(30);
        cam.writeMaxFPS(10);
        cam.writeBrightness(0);
        cam.writeWhiteBalance(WhiteBalanceT.fixedFlour2);
        ledLight = new Relay(LED_LIGHT_CHANNEL);
        new Thread(this).start();
    }

    /**
     * Get the instance of the Team467Camera object
     * @return The instance
     */
    public static Team467Camera getInstance()
    {
        if (instance == null)
        {
            instance = new Team467Camera();
        }
        return instance;
    }

    /**
     * Sets the LED light to being on or off
     * @param b Whether the light is on or not
     */
    private void setLED(boolean lightOn)
    {
        if (lightOn)
        {
            ledLight.set(Relay.Value.kForward);
        }
        else
        {
            ledLight.set(Relay.Value.kOff);
        }
    }

    /**
     * Gets the angle to the largest particle
     * @return The angle to the largest particle
     */
    private double getAngleToParticle()
    {
        try
        {
            double xDiff = 0.0;
            double yDiff = 0.0;
            if (camData.targetVisible)
            {
                xDiff = largestParticle.center_mass_x - (image.getWidth() / 2);
                yDiff = largestParticle.center_mass_y - (image.getHeight() / 2);
            }
            double angle = (CAMERA_X_FOV / image.getWidth()) * xDiff;
            return angle;
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
        return 0.0;
    }


    /**
     * Detects any bright particles in the camera image and reads them into the
     * particleResults variable.
     */
    private void detectParticles()
    {
        BinaryImage thresholdHSL;
        try
        {
            thresholdHSL = image.thresholdHSL(0, 255, 0, 255, 150, 255);//(117, 255, 10, 255, 137, 255);
            particleResults = thresholdHSL.getOrderedParticleAnalysisReports();
            thresholdHSL.free();
        }
        catch (NIVisionException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Reads the largest particle of the particle results into the largestParticle variable
     */
    private void getLargestParticle()
    {
        if ((particleResults != null) && (particleResults.length > 0))
        {
            largestParticle = particleResults[0];
        }
    }

    /**
     * Gets the height of the camera image
     * @return The height of the camera image
     */
    private int getHeight()
    {
        return height;
    }

    /**
     * Gets the width of the camera image
     * @return The width of the camera image
     */
    private int getWidth()
    {
        return width;
    }
    /**
     * Reads the image into the image variable.
     */
    private void readImage()
    {
        try 
        {
            largestParticle = null;
            if (cam.freshImage())
            {
                if (image != null)
                {
                    image.free();
                }
                image = cam.getImage();
            }
            if (image == null)
            {
                width = height = 0;
                System.out.println("No Image");
            }
            else
            {
                width = image.getWidth();
                height = image.getHeight();
            }
        } 
        catch (AxisCameraException ex)
        {
            ex.printStackTrace();
        } 
        catch (NIVisionException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Used to determine the average value of an array of doubles.
     * @param d An array of doubles.
     * @return The average value of the array.
     */
    private static double getAverage(double[] d)
    {
        double values = 0;
        for (int i = 0; i < d.length; i++)
        {
            values += d[i];
        }
        values /= d.length;
        return values;
    }

    private boolean readingCamera = false;
    
    /**
     * Starts camera reading
     */
    public void startCameraReading()
    {
        setLED(true);
        readingCamera = true;
    }

    /**
     * Stops camera reading
     */
    public void stopCameraReading()
    {
        setLED(false);
        readingCamera = false;
    }

    /**
     * Read camera and store in variables
     */
    private void processCamera()
    {
        readImage();
        if (image != null)
        {
            detectParticles();
            getLargestParticle();
            setDataFromParticle();
        }
    }

    private boolean lastParticleVisible = false;

    private double targetMinimumHeight = 5.0;

    /**
     * Sets the minimum target height to look for. Targets less than this height
     * will not be returned.
     * @param height the minimum height target that is acceptable
     */
    public void setMimimumTargetHeight(double height)
    {
        targetMinimumHeight = height;
    }

    /**
     * Set camera particle data
     */
    private synchronized void setDataFromParticle()
    {
        if ((largestParticle != null) && (largestParticle.boundingRectHeight >= targetMinimumHeight))
        {
            camData.targetXPos = largestParticle.center_mass_x - getWidth() / 2;
            camData.targetYPos = largestParticle.center_mass_y - getHeight() / 2;
            camData.targetHeight = largestParticle.boundingRectHeight;
            camData.angleToTarget = getAngleToParticle() / 90.0;
            camData.targetVisible = true;
            lastParticleVisible = true;
        }
        else
        {
            if (!lastParticleVisible)
            {
                camData.targetVisible = false;
            }
            lastParticleVisible = false;
        }
    }

    /**
     * Gets the camera data
     * @return The camera data
     */
    public synchronized CamData getCamData()
    {
        return camData;
    }

    /**
     * Run camera thread
     */
    public void run()
    {
        while (true)
        {
            long startTime = System.currentTimeMillis();
            if (readingCamera)
            {
                processCamera();
                SmartDashboard.log(camData.targetHeight, "Target Height");
                SmartDashboard.log(camData.targetVisible, "Target Visible");
                SmartDashboard.log(camData.targetXPos, "Target XPos");
            }
            long timeDiff = System.currentTimeMillis() - startTime;
            //System.out.println(timeDiff);
            timeDiff = timeDiff > CAMERA_RATE ? CAMERA_RATE : timeDiff;
            try
            {
                // only sample camera at a fixed interval
                Thread.sleep(CAMERA_RATE - timeDiff);
            }
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Class to hold camera data (distance, circle coordinates, ect.)
     */
    public class CamData
    {
        public double targetXPos = 0.0;
        public double targetYPos = 0.0;
        public boolean targetVisible = false;
        public double angleToTarget = 0.0;
        public double targetHeight = 0.0;
    }
}
