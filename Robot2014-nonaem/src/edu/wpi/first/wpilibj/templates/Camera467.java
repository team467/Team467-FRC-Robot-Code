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
public class Camera467 implements Runnable
{

    private static Camera467 instance = null;
    private AxisCamera cam;
    private ColorImage currentImage;
    private Thread cameraThread = null;

    private final int CAMERA_RATE = 100;

    private boolean runningCamera = false;
    private boolean readingCamera = false;

    private boolean targetDetected = false;

    private Camera467()
    {
        cam = AxisCamera.getInstance();
        cam.writeResolution(AxisCamera.ResolutionT.k640x480);
        cam.writeCompression(0);
    }

    /**
     * Gets the singleton instance of this class
     *
     * @return
     */
    public static Camera467 getInstance()
    {
        if (instance == null)
        {
            instance = new Camera467();
        }
        return instance;
    }

    /**
     * Starts the camera thread. Note that isTargetDetected() won't work unless
     * this method has been called.
     */
    public void startThread()
    {
        if (cameraThread == null)
        {
            cameraThread = new Thread(this);
            cameraThread.start();
            targetDetected = false;
        }
        else
        {
            System.out.println("Thread already started.");
        }
    }

    /**
     * Stops the camera thread. Saves cRIO clock cycles.
     */
    public void killThread()
    {
        if (cameraThread != null)
        {
            runningCamera = false;
            readingCamera = false;
            targetDetected = false;
            cameraThread = null;
        }
        else
        {
            System.out.println("No thread to kill.");
        }
    }

    /**
     * Read the image from the camera.
     */
    private void readImage()
    {
        try
        {
            if (cam.freshImage())
            {
                if (currentImage != null)
                {
                    currentImage.free();
                }

                currentImage = cam.getImage();
            }
            if (currentImage == null)
            {
                System.out.println("[Camera] No image!");
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
     * Checks if there is a target in the image. The optimal range is 17.5 feet,
     * however this method should work anywhere between 17 and 18 feet.
     *
     * @return Whether there is a target in the current image.
     */
    private boolean checkTarget()
    {
        if (currentImage == null)
        {
            System.out.println("[Camera] No image to process!");
            return false;
        }

        // Image bounds
        final int HUE_LOW = 72;
        final int HUE_HIGH = 138;
        final int SATURATION_LOW = 155;
        final int SATURATION_HIGH = 255;
        final int LUMINANCE_LOW = 155;
        final int LUMINANCE_HIGH = 255;

        final int HORIZONTAL_WIDTH_LOW = 41;
        final int HORIZONTAL_WIDTH_HIGH = 49;
        final int HORIZONTAL_HEIGHT_LOW = 4;
        final int HORIZONTAL_HEIGHT_HIGH = 12;

        final int VERTICAL_WIDTH_LOW = 4;
        final int VERTICAL_WIDTH_HIGH = 12;
        final int VERTICAL_HEIGHT_LOW = 56;
        final int VERTICAL_HEIGHT_HIGH = 67;

        final int DISTANCE_LOW = 1500;
        final int DISTANCE_HIGH = 2200;

        int horizontalParticleIndex = -1;
        int verticalParticleIndex = -1;

        BinaryImage processedImage;
        ParticleAnalysisReport[] reports;

        try
        {
            // isolate only the brightest parts of the image
            processedImage = currentImage.thresholdHSL(HUE_LOW, HUE_HIGH,
                    SATURATION_LOW, SATURATION_HIGH, LUMINANCE_LOW, LUMINANCE_HIGH);

            // get isolated particles
            reports = processedImage.getOrderedParticleAnalysisReports();

            // if a horizontal or vertical target is detected, record which particle
            //    it is.
            for (int i = 0; i < reports.length; i++) // should be foreach loop, but source 1.3 doesn't support it.
            {
                if (checkParticle(reports[i], HORIZONTAL_HEIGHT_LOW, HORIZONTAL_HEIGHT_HIGH,
                        HORIZONTAL_WIDTH_LOW, HORIZONTAL_WIDTH_HIGH))
                {
                    horizontalParticleIndex = i;
                }
                else if (checkParticle(reports[i], VERTICAL_HEIGHT_LOW, VERTICAL_HEIGHT_HIGH,
                        VERTICAL_WIDTH_LOW, VERTICAL_WIDTH_HIGH))
                {
                    verticalParticleIndex = i;
                }
            }

            // free up image memory
            processedImage.free();

            if (horizontalParticleIndex == -1 || verticalParticleIndex == -1)
            {
                // if there is no horizontal or vertical target, return false.
                return false;
            }
            else // test distance between horizontal and vertical target
            // (helps detect the L-configuration)
            {

                ParticleAnalysisReport horizParticle = reports[horizontalParticleIndex];
                ParticleAnalysisReport vertiParticle = reports[verticalParticleIndex];

                double horizX = horizParticle.center_mass_x;
                double horizY = horizParticle.center_mass_y;
                double vertiX = vertiParticle.center_mass_x;
                double vertiY = vertiParticle.center_mass_y;

                double dx = Math.abs(horizX - vertiX);
                double dy = Math.abs(horizY - vertiY);

                double distance = dx * dx + dy * dy;

                System.out.println("DIST: " + inBounds(distance, DISTANCE_LOW, DISTANCE_HIGH));

                return inBounds(distance, DISTANCE_LOW, DISTANCE_HIGH);
            }
        }
        catch (NIVisionException e)
        {
            e.printStackTrace();
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }

        // catch-all return false, if something goes wrong.
        return false;
    }

    /**
     * Tests whether a value is between a lower and upper bound. Note that it
     * does not matter in which order the bounds are specified.
     *
     * @param value
     * @param bound1
     * @param bound2
     * @return true if value is between lower and upper, and false otherwise.
     */
    private boolean inBounds(double value, double bound1, double bound2)
    {
        double lower = Math.min(bound1, bound2);
        double upper = Math.max(bound1, bound2);

        return (value > lower) && (value < upper);
    }

    /**
     * Check if a particle fits in given criteria.
     *
     * @param particle particle to test
     * @param heightLow
     * @param heightHigh
     * @param widthLow
     * @param widthHigh
     * @return
     */
    private boolean checkParticle(ParticleAnalysisReport particle, double heightLow,
            double heightHigh, double widthLow, double widthHigh)
    {
        double particleHeight = particle.boundingRectHeight;
        double particleWidth = particle.boundingRectWidth;
        double particleArea = particle.particleArea;

        double areaHigh = heightHigh * widthHigh;
        double areaLow = heightLow * widthLow;

        return inBounds(particleArea, areaLow, areaHigh)
                && inBounds(particleHeight, heightLow, heightHigh)
                && inBounds(particleWidth, widthLow, widthHigh);
    }

    /**
     * Runs when a new camera thread is created. Updates targetDetected while
     * the camera is reading. Note that if the camera is not reading,
     * isTargetDetected() will be inaccurate.
     */
    public void run()
    {
        runningCamera = true;

        while (runningCamera)
        {
            long startTime = System.currentTimeMillis();

            if (readingCamera)
            {
                targetDetected = getFreshTarget();
            }

            long timeDiff = System.currentTimeMillis() - startTime;
            //System.out.println("timeDiff: " + timeDiff);
            timeDiff = (timeDiff > CAMERA_RATE) ? CAMERA_RATE : timeDiff;

            try
            {
                // only sample camera at a fixed interval
                Thread.sleep(CAMERA_RATE - timeDiff);
            }
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }

            //System.out.println(System.currentTimeMillis() - startTime);
        }
    }

    /**
     * Reads the image from the camera and immediately checks for a hot goal.
     *
     * @return
     */
    private boolean getFreshTarget()
    {
        readImage();
        return checkTarget();
    }

    /**
     * Has the target been detected by the thread?
     *
     * @return
     */
    public boolean isTargetDetected()
    {
        return targetDetected;
    }

    /**
     * Toggles the camera reading. Turn off reading when it isn't in use.
     */
    public void toggleReading()
    {
        setReading(!readingCamera);
    }
    
    public void setReading(boolean state) {
        readingCamera = state;
        
        if (readingCamera)
        {
            cam.writeResolution(AxisCamera.ResolutionT.k320x240);
        }
        else
        {
            cam.writeResolution(AxisCamera.ResolutionT.k640x480);
        }
    }

    /**
     * Returns whether the target has been detected.
     *
     * @return
     */
    public boolean isReading()
    {
        return readingCamera;
    }
}
