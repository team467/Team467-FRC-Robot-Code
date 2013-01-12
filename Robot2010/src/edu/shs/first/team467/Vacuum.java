package edu.shs.first.team467;

import edu.wpi.first.wpilibj.*;

/**
 * The robot contains a vacuum and an ultrasonic sensor for detecting a ball.
 * This singleton class is used to control the vacuume mechanism on Team467 2010 Robot
 * @author aidan
 */
public class Vacuum
{
    //Set to true to print debug messages
    private final boolean DEBUG = false;

    //Channels
    private final int VACUUM_CHANNEL = 8;
    private final int ULTRASONIC_CHANNEL = 3;

    //Distances to detect for ball
    private final double BALL_DETECT_DISTANCE = 20.0;
    private final double BALL_GRAB_DISTANCE = 8.0;

    //Vacuum on/off constants
    private final double VACUUM_ON = 1.0;
    private final double VACUUM_OFF = 0.0;

    private double ballDistance = 0.0;

    //Make ultrasonic sensor and vacuum victor
    private AnalogChannel ultrasonic;
    private Victor vacuumMotor;

    //Singleton instance variable
    private static Vacuum vacuumInstance;

    //Singleton so constructor is private
    private Vacuum()
    {
        ultrasonic = new AnalogChannel(ULTRASONIC_CHANNEL);
        vacuumMotor = new Victor(VACUUM_CHANNEL);
    }

    //return single instance of this class
	public static synchronized Vacuum GetInstance()
    {
		if (vacuumInstance == null)
        {
			vacuumInstance = new Vacuum();
		}
		return vacuumInstance;
	}

    /**
     * Called from within periodic teleop or autonomous code to control
     * vacuum operation during robot operation.
     * @param driverstation - used for inputs and outputs from driver station
     */
    public void Update()
    {
        Team467DriverStation driverstation = Team467DriverStation.GetInstance();

        // read Ultrasonic
        ballDistance = ultrasonic.getAverageValue();

        if(DEBUG)
        {
            System.out.print("Override: " + driverstation.VacuumOverride  + ", ");
            System.out.print("Distance: " + ballDistance + ", ");
        }

        // if vacuum overridden, or no ball detected, vacuum is off
        if (driverstation.VacuumOverride || (ballDistance > BALL_DETECT_DISTANCE))
        {
            vacuumMotor.set(VACUUM_OFF);
            driverstation.SetVacuumLED(false);
        }
        else
        {
            if(DEBUG)
            {
                System.out.print("Ball Detected" + ", ");
            }
            // Ball in range - turn on vacuum
            vacuumMotor.set(VACUUM_ON);

            // Set Vacuum LED if ball close to sensor
            driverstation.SetVacuumLED(ballDistance <= BALL_GRAB_DISTANCE);

            //Turns on led if ball is in possession of robot
            if(DEBUG && (ballDistance <= BALL_GRAB_DISTANCE))
            {
                System.out.print("Ball In Grab Range" + ", ");
            }
        }
        if (DEBUG)
        {
            System.out.println();
        }
    }

    /**
     * Tune vacuum.  Used to test/validate vacuum operation. Not called from
     * production code.
     * @param driverstation - current driverstation used for inputs
     */
    public void Tune()
    {
        Team467DriverStation driverstation = Team467DriverStation.GetInstance();

        //Distance to ball variable
        double distance = ultrasonic.getAverageValue();

        if (driverstation.XboxX)
        {
            vacuumMotor.set(-VACUUM_ON);
        }
        else if (driverstation.XboxB)
        {
            vacuumMotor.set(VACUUM_ON);
        }
        else
        {
            vacuumMotor.set(VACUUM_OFF);
        }
        System.out.println("Vacuum: " + vacuumMotor.get() + " Ultrasonic Distance: " + distance);

    }

    /**
     * Determines if ball is in possession of robot
     * @return true if ball has been grabbed by vacuum, false otherwise
     */
    public boolean BallGrabbed()
    {
        return (ballDistance <= BALL_GRAB_DISTANCE);
    }
}
