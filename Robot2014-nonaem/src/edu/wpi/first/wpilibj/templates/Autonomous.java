/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author Team467 This class consists of only static functions and variables,
 * and simply acts as a container for all autonomous code.
 */
public class Autonomous
{

    private static Drive drive = Drive.getInstance();
    private static Camera467 cam;
    private static Launcher launcher;
    private static Feeder feeder;
    private static Compressor467 compressor;

    private static final int DRIVE_TO_POS_TIME = 1500;
    private static final int STABILIZE_TIME = 3000;
    private static final int DROP_FEEDER_TIME = 4000;
    private static final int WAIT_TO_SHOOT_TIMEOUT = 6000;

    private static final int START = 0;
    private static final int DRIVE_TO_POS = 1;
    private static final int WAIT_FOR_STABILIZE = 2;
    private static final int LOWER_FEEDER = 3;
    private static final int WAIT_TO_SHOOT = 4;
    private static final int SHOOT = 5;

    private static int state = START;

    private static final boolean USE_CAMERA = false;

    static long startTime = 0;

    /**
     * Autonomous initialization code
     */
    public static void init()
    {
        cam = Camera467.getInstance();
        if (USE_CAMERA)
        {
            cam.startThread();
        }
        launcher = Launcher.getInstance();
        feeder = Feeder.getInstance();
        compressor = Compressor467.getInstance();
        resetState();
        startTime = 0;
    }

    /**
     * Periodic autonomous update function
     */
    public static void updateAutonomous()
    {
        compressor.update();
        long currentTime = System.currentTimeMillis();
        if (startTime == 0)
        {
            startTime = currentTime;
        }
        long deltaTime = currentTime - startTime;

        // make sure camera is reading
        if (USE_CAMERA)
        {
            cam.setReading(true);
        }

        switch (state)
        {
            case START:
                drive.crabDrive(0, 0, false);
                launcher.pullBackLauncher();
                state = DRIVE_TO_POS;
                break;

            case DRIVE_TO_POS:
                drive.crabDrive(0, 0.4, false);                
                if (deltaTime > DRIVE_TO_POS_TIME)
                {
                    state = WAIT_FOR_STABILIZE;
                }
                break;

            case WAIT_FOR_STABILIZE:                
                drive.crabDrive(0, 0, false);
                if (deltaTime > STABILIZE_TIME)
                {
                    state = LOWER_FEEDER;
                }
                break;
                
            case LOWER_FEEDER:
                feeder.lowerFeeder();        
                drive.crabDrive(0, 0, false);
                if (deltaTime > DROP_FEEDER_TIME)
                {
                    state = WAIT_TO_SHOOT;
                }
                break;

            case WAIT_TO_SHOOT:
                drive.crabDrive(0, 0, false);                
                if ((USE_CAMERA && cam.isTargetDetected()) || (deltaTime > WAIT_TO_SHOOT_TIMEOUT))
                {
                    state = SHOOT;
                }
                break;

            case SHOOT:
                drive.crabDrive(0, 0, false);
                launcher.fireLauncher();
                break;
        }
    }

    /**
     * Reset autonomous state
     */
    public static void resetState()
    {
        startTime = 0;
        state = START;
    }
}
