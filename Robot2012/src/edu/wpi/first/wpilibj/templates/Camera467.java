/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.*;


/**
 *
 * @author USFIRST
 */
public class Camera467 implements Runnable
{
    //Team467Camera instance
    private static Camera467 instance = null;
    
    //bool to image to toggle saving image to the cRIO
    private boolean WRITE_IMAGE = true;

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
    private ParticleAnalysisReport[] particleResults;
    private ParticleAnalysisReport largestParticle = null;

    //Led channel
    private final int LED_LIGHT_CHANNEL = 4;

    //Led light relay
    private Relay ledLight;

    //Camera image width and height
    private int height = 0;
    private int width = 0;
    
    //creates
    private ParticleAnalysisReport[] filteredParticles = new ParticleAnalysisReport[4];
    private int filteredParticlesCount = 0;
    
    //oragnized paricles
    private ParticleAnalysisReport topMost = null;
    private ParticleAnalysisReport bottomMost = null;
    private ParticleAnalysisReport leftMost = null;
    private ParticleAnalysisReport rightMost = null;
    
    //center point variables (center between squares)
    private int centerX = 0;
    private int centerY = 0;
    
    //flag to always save at least one copy of the image for debug
    boolean imageSaved = false;
    
    //Make constructor private so instances can't be created outside class
    private Camera467()
    {
        cam = AxisCamera.getInstance();
        cam.writeResolution(AxisCamera.ResolutionT.k320x240);
        cam.writeCompression(0);
        //cam.writeMaxFPS(30);
        //cam.writeBrightness(50);
        //cam.writeWhiteBalance(WhiteBalanceT.automatic);
        //cam.writeRotation(AxisCamera.RotationT.k0);
        ledLight = new Relay(LED_LIGHT_CHANNEL);
        new Thread(this).start();
    }

    /**
     * Get the instance of the Team467Camera object
     * @return The instance
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
     * Sets the LED light to being on or off
     * @param lightOn Whether the light is on or not
     */
    public void setLED(boolean lightOn)
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
     * Detects any bright particles in the camera image and reads them into the
     * particleResults variable.
     */
    private void detectParticles()
    {
        //constants for thresholding the image
        final int HUE_LOW = 0;
        final int HUE_HIGH = 255;
        final int SATURATION_LOW = 0;
        final int SATURATION_HIGH = 255;
        final int LUMINANCE_LOW = 176;
        final int LUMINANCE_HIGH = 255;
        
        //thresholded binary image
        BinaryImage thresholdHSL;
        
        //reset particle count
        filteredParticlesCount = -1;
        
        try
        {
            //apply HSL threshold to the image
            thresholdHSL = image.thresholdHSL(HUE_LOW, HUE_HIGH, SATURATION_LOW, SATURATION_HIGH, LUMINANCE_LOW, LUMINANCE_HIGH);
            
            //analyze the filtered image to idenify particles
            particleResults = thresholdHSL.getOrderedParticleAnalysisReports(15);
            
            // counts how many particles match rectangle criteria
            int correctRects = 0;
            
            //checks if there is a particeResults and if it has values
            if ((particleResults != null) && (particleResults.length > 0))
            {
                int i;                              //index for looping
                ParticleAnalysisReport report;      //particle report at current index
                double boxHeight;                   //stores particle bounding box height  
                double boxWidth;                    //stores bounding box width
                int boxCenterX;                     //stores the center of the bounding box in X
                int boxCenterY;                     //stores the center of the bounding box in Y
                double ratio;                       //stores boxWidth/boxHeight
                double quality;                     //stores particle quality
                
                //constants for desired height to width ratio
                final double IDEAL_WIDTH = 24;  //inches
                final double IDEAL_HEIGHT = 18; //inches
                final double IDEAL_RATIO = IDEAL_WIDTH/IDEAL_HEIGHT;

                //constants for filtering size and ratio to within specified limit
                final double MIN_RATIO = IDEAL_RATIO - 0.2;
                final double MAX_RATIO = IDEAL_RATIO + 0.2;
                final double MIN_QUALITY = 20.0;
                final double MAX_QUALITY = 50.0;
                final int MIN_BOX_WIDTH = 9;  //pixels
                final int MIN_BOX_HEIGHT = 7; //pixels
               
                //accesses each particle in particleResults
                for (i = 0; i < particleResults.length; i++) 
                {
                    report = particleResults[i];

                    //read height, width, and quality
                    boxHeight = (double)report.boundingRectHeight;
                    boxWidth = (double)report.boundingRectWidth;
                    quality = report.particleQuality;
                    
                    
                    //apply filter to only allow particles above the minimum size
                    if (boxHeight > MIN_BOX_HEIGHT && boxWidth > MIN_BOX_WIDTH && MAX_QUALITY > quality && MIN_QUALITY < quality)
                    {
                        
                        //read box parameters into variables
                        boxCenterX = report.center_mass_x;
                        boxCenterY = report.center_mass_y;
                        ratio = boxWidth / boxHeight;
                        

                        //filters particles to get rectangles that meat the correct ratio and are large enough
                        if (ratio >= MIN_RATIO && ratio <= MAX_RATIO)
                        {
                            correctRects++;
                            //will print out multipul values if the debug boolean WRITE_IMAGE = true , which is set at the top
                            if (WRITE_IMAGE)
                            {
                                //prints out details of this element for debug
                                System.out.print(i + " ");
                                System.out.print(" H: " + boxHeight  + " ");
                                System.out.print(" W: " + boxWidth);
                                System.out.print(" Q: " + quality);
                                System.out.print(" R: " + ratio);
                                System.out.print(" X: " + boxCenterX);
                                System.out.print( "Y: " + boxCenterY);
                                System.out.println();
                            }

                            //checks to see of array filteredParticles is full
                            if (filteredParticlesCount < 4) 
                            {
                                filteredParticlesCount++;
                                filteredParticles[filteredParticlesCount] = report;
                            } 
                            else 
                            {
                                System.out.println("Too Many Rectangles!");
                            }

                        }
                    }
                }
                
            }
                //will save raw and filtered images if the debug boolean WRITE_IMAGE = true , which is set at the top
                if (WRITE_IMAGE)
                {
                    //look to see if image is good, and if so it saves the image 'testImage.bmp' for debug
                    if (!imageSaved) 
                    {
                        System.out.println("Image Saved!");
                        image.write("/ni-rt/images/rawImage.bmp");
                        thresholdHSL.write("/ni-rt/images/filteredImage.bmp");
                        imageSaved = true;
                    }   
                }
            //resets the thresholding
            thresholdHSL.free();
            thresholdHSL = null;
        
        }
        catch (NIVisionException ex)
        {
            ex.printStackTrace();
        }
    }
    
    /**
     * Organizes particles so they are 'leftMost', 'rightMost', 'topMost', 'bottomMost' in respect to the diamond arrangement 
     */
    private void organizeParticles()
    {
            //runs if there are 2 or more rectangles
            if (filteredParticlesCount > 1)
            {
                ParticleAnalysisReport report;      //particle report at current index
                
                //initialize positions to a single particle
                leftMost = rightMost = topMost = bottomMost = filteredParticles[0];
                
                //sets the particles to their positions
                for (int i = 0; i < filteredParticlesCount + 1; i++) 
                {
                    report = filteredParticles[i]; 
                    
                    if (report.center_mass_x < leftMost.center_mass_x) 
                    {
                        leftMost = report;                        
                    }
                    if (report.center_mass_x > rightMost.center_mass_x) 
                    {
                        rightMost = report;                        
                    }
                    if (report.center_mass_x < topMost.center_mass_y) 
                    {                        
                        topMost = report;                        
                    }
                    if (report.center_mass_x > bottomMost.center_mass_y) 
                    {
                        bottomMost = report;                        
                    }
                    
                }

                //runs if no particles are missing
                if (filteredParticles.length == 4) 
                {
                    //assign center X and Y the center values
                    centerX = (leftMost.center_mass_x + rightMost.center_mass_x) / 2;
                    centerY = (topMost.center_mass_y + bottomMost.center_mass_y) / 2;
                }

                //runs if one particle is missing
                if (filteredParticles.length == 3) 
                {
                    //check if leftMost missing
                    if (leftMost.center_mass_x == topMost.center_mass_x || leftMost.center_mass_x == bottomMost.center_mass_x) 
                    {
                        centerX = (topMost.center_mass_x + bottomMost.center_mass_x) / 2;
                        int averageY = (topMost.center_mass_y + bottomMost.center_mass_y) / 2;
                        centerY = (averageY + rightMost.center_mass_y) / 2;
                        
                    }
                    //check if rightMost missing
                    if (rightMost.center_mass_x == topMost.center_mass_x || rightMost.center_mass_x == bottomMost.center_mass_x) 
                    {
                        centerX = (topMost.center_mass_x + bottomMost.center_mass_x) / 2;
                        int averageY = (topMost.center_mass_y + bottomMost.center_mass_y) / 2;
                        centerY = (averageY + leftMost.center_mass_y) / 2;
                        
                    }
                    //check if topMost missing
                    if (topMost.center_mass_y == leftMost.center_mass_y || topMost.center_mass_y == rightMost.center_mass_y) 
                    {
                        centerY = (leftMost.center_mass_y + rightMost.center_mass_y) / 2;
                        int averageX = (leftMost.center_mass_x + rightMost.center_mass_x) / 2;
                        centerX = (averageX + bottomMost.center_mass_x) / 2;   
                        
                    }
                    //check if bottomMost missing
                    if (bottomMost.center_mass_y == leftMost.center_mass_y || bottomMost.center_mass_y == rightMost.center_mass_y) 
                    {
                        centerY = (leftMost.center_mass_y + rightMost.center_mass_y) / 2;
                        int averageX = (leftMost.center_mass_x + rightMost.center_mass_x) / 2;
                        centerX = (averageX + topMost.center_mass_x) / 2;
                       
                        
                    }
                    else
                    {
                        System.err.println("Error: Did not find the misplaced 4th particle, yet there are only 3 particles, see lines 292 - 327");
                    }
                }
                //runs if two particles are missing
                //this assumes topMost is correctly set as the top hoop
                if (filteredParticles.length == 2) 
                {
                    //rightMost and bottomMost missing
                    if (topMost.equals(rightMost) && leftMost.equals(bottomMost))
                    {
                        centerY = topMost.center_mass_y;
                        centerX = leftMost.center_mass_x;
                      
                    }
                    //leftMost and bottomMost missing
                    if (topMost.equals(leftMost) && rightMost.equals(bottomMost))
                    {
                        centerY = topMost.center_mass_y;
                        centerX = rightMost.center_mass_x;
                  
                    }
                    //rightMost and LeftMost missing
                    if ((topMost.equals(leftMost) || topMost.equals(rightMost)) && (bottomMost.equals(leftMost) || bottomMost.equals(rightMost)))
                    {
                        centerY = (topMost.center_mass_y + bottomMost.center_mass_y) / 2;
                        centerX = (topMost.center_mass_x + bottomMost.center_mass_x) / 2;
                
                    }
                }
                //System.out.print("X: " + centerX);
                //System.out.println(" Y: " + centerY);
            }
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

    private boolean readingCamera = false;
    
    /**
     * Starts camera reading
     */
    public void startCameraReading()
    {
        System.out.println("in startCameraReading()");
        //setLED(true);
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
            organizeParticles();
        }       
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
            }
            long timeDiff = System.currentTimeMillis() - startTime;
            //System.out.println("timeDiff: " + timeDiff);
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
     * Returns Bounding Box Height of topMost particle
     * @return int box height in pixels
     */
    public int returnBoxHeight()
    {
        int boxHeight = topMost.boundingRectHeight;
        return boxHeight;
    }
    
    /**
     * Gives the distance from the camera to the backboard in a double
     * This is unused!
     * @return double for distance
     */
    public double robotDistance()
    {   
        //final double CAMERA_VIEW_ANGLE = 47.0;
        final double TAN_OF_VIEW_ANGLE = 16.1227249;
        double distance = 0.0;
        double boundingHeight = 0.0;
        if (topMost != null)
        {
            boundingHeight = topMost.boundingRectHeight;
            distance = boundingHeight / TAN_OF_VIEW_ANGLE;
            return distance;
        }
        else
        {
            //spits out 9999 if there is an error
            double error = 9999.0;
            return error;
        }
    }
}