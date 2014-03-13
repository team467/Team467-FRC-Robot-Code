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

    private static final int DRIVE_TO_POS_TIME = 1000;       // Drive forward for 1 second
    private static final int WAIT_TO_SHOOT_TIMEOUT = 8000;   // Shoot after 8 seconds

    private static final int START = 0;
    private static final int DRIVE_TO_POS = 1;
    private static final int WAIT_TO_SHOOT = 2;
    private static final int SHOOT = 3;

    private static int state = START;

    static long startTime = 0;

    /**
     * Autonomous initialization code
     */
    public static void init()
    {
        cam = Camera467.getInstance();
        cam.startThread();
        launcher = Launcher.getInstance();
        feeder = Feeder.getInstance();
        resetState();
    }

    /**
     * Periodic autonomous update function
     */
    public static void updateAutonomous()
    {
        long currentTime = System.currentTimeMillis();
        if (startTime == 0)
        {
            startTime = currentTime;
        }
        long deltaTime = currentTime - startTime;

        // make sure camera is reading
        if (!cam.isReading())
        {
            cam.toggleReading();
        }

        switch (state)
        {
            case START:
                drive.crabDrive(0, 0, false);
                launcher.pullBackLauncher();
                feeder.lowerFeeder();
                state = DRIVE_TO_POS;
                break;

            case DRIVE_TO_POS:
                drive.crabDrive(0, 0.5, false);
                if (deltaTime > DRIVE_TO_POS_TIME)
                {
                    state = WAIT_TO_SHOOT;
                }
                break;

            case WAIT_TO_SHOOT:
                drive.crabDrive(0, 0, false);
                if (cam.isTargetDetected() || (deltaTime > WAIT_TO_SHOOT_TIMEOUT))
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
