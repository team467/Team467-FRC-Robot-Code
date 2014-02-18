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
    private static int particles = 0;
    private static Driverstation driverstation = Driverstation.getInstance();

    private static final int START_TIME_MILIS = 500;//.5sec
    private static final int DRIVE_TO_POS_TIME_MILIS = 2000;//2sec
    private static final int WAIT_TO_SHOOT_TIMEOUT_TIME_MILIS = 9000;//2sec

    private static final int START = 0;
    private static final int DRIVE_TO_POS = 1;
    private static final int WAIT_TO_SHOOT = 2;
    private static final int SHOOT = 3;

    private static int STATE = START;

    private static boolean iSawSomething = false;
    private static boolean isFirstLoop = true;
    static long persistantTimerInMilis = 0;
    static long firstLoopTimeInMilis = 0;

    /**
     * Autonomous initialization code
     */
    public static void init()
    {
        cam = Camera467.getInstance();
        cam.startThread();
        launcher = Launcher.getInstance();
        iSawSomething = false;
        isFirstLoop = true;
        persistantTimerInMilis = 0;
        firstLoopTimeInMilis = 0;
        STATE = START;
    }

    /**
     * Periodic autonomous update function
     */
    public static void updateAutonomous(int mode)
    {
        if (isFirstLoop)
        {
            firstLoopTimeInMilis = System.currentTimeMillis();
            isFirstLoop = false;
        }
        // make sure camera is reading
        if (!cam.isReading())
        {
            cam.toggleReading();
        }

        switch (STATE)
        {
            case START:
                if (System.currentTimeMillis() - firstLoopTimeInMilis < START_TIME_MILIS)
                {
                    launcher.pullBackLauncher();
                }
                else
                {
                    STATE = DRIVE_TO_POS;
                }
                break;
            case DRIVE_TO_POS:
                if ((System.currentTimeMillis() - firstLoopTimeInMilis) < (START_TIME_MILIS + DRIVE_TO_POS_TIME_MILIS))
                {
                    //drive forward at 50% power
                    drive.crabDrive(0, .5, false);
                    launcher.pullBackLauncher();
                }
                break;
            case WAIT_TO_SHOOT:
                if (cam.isTargetDetected() || (System.currentTimeMillis() - firstLoopTimeInMilis) > (WAIT_TO_SHOOT_TIMEOUT_TIME_MILIS))
                {
                    STATE = SHOOT;
                }
                break;
            case SHOOT:
                launcher.fireLauncher();
                break;
        }

        if (!cam.isTargetDetected())
        {
            //drive forward at 50% power
            drive.crabDrive(0, .5, false);
            //pull the launcher in the down position
            launcher.pullBackLauncher();
        }
        else
        {
            //stop and launch the ball
            launcher.fireLauncher();
        }
    }

    /**
     * Reset autonomous state
     */
    public static void resetState(int mode)
    {
        particles = 1;
    }
}
