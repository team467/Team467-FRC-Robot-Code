package edu.shs.first.team467;

import edu.wpi.first.wpilibj.*;

/**
 * Singleton class to control hook mechanism on Team467 2010 Robot
 * @author greg
 */
public class Kicker
{
    // constant definitions
    private final int KICK1_PORT = 2;
    private final int KICK2_PORT = 3;
    private final int LATCH_PORT = 9;
    private final int KICKER_UP_SENSOR = 1;
    private final int KICKER_SPIN_SENSOR = 2;

    private Relay kick1;
    private Relay kick2;
    private Servo latch;
    private DigitalInput kickerUp;
    private DigitalInput kickerSpin;

    private boolean kickerUpValue = false;   // true if kicker is in primed position
    
    // kicker spinner is a micro switch used to limit the duration of rotation
    // while unwinding the kicker mechanism.  It has proven unreliable, so we also
    // back this up with a time limit value.

    private boolean kickerSpinValue = false; // actual value of the micro switch
    private boolean lastKickerSpin = false;  // used to debounce kickerSpinValue
    private int kickerSpinCount = 0;         // count of number of clicks on spinner
    private final int UNWIND_TIME = 75;      // number of periods (20ms) to allow kicker to unwind
    private int unwindCount = 0;             // counter to track this timer.

    // latch mechanism is controlled by a servo. It takes a finite time to activate so a timer is also used
    private final double LATCH_UNLATCH = 1;
    private final double LATCH_LATCH = 0;
    private final int LATCH_TIME = 50;       // time for the latch servo mechanism to activate (in periods)
    private int latchCount = 0;              // counter to track this timer

    // enums not supported in this version of Java - so use hard coded values for state machine
    private final int STATE_KICKER_DOWN = 1;
    private final int STATE_KICKER_UP = 2;
    private final int STATE_KICKER_WAITING = 3;
    private final int STATE_KICKER_UNWINDING = 4;
    private final int STATE_KICKER_PRIMED = 5;
    private final int STATE_KICKER_FIRE = 6;
    private int kickerState;

    // Singleton instance variable
    private static Kicker kickerInstance;

    // Singleton so constructor is private
    private Kicker()
    {
        kick1 = new Relay(KICK1_PORT);
        kick2 = new Relay(KICK2_PORT);
        latch = new Servo(LATCH_PORT);
        kickerUp = new DigitalInput(KICKER_UP_SENSOR);
        kickerSpin = new DigitalInput(KICKER_SPIN_SENSOR);

        kickerUpValue = !kickerUp.get();
        kickerSpinValue = !kickerSpin.get();

        if (kickerUpValue)
        {
            // If kicker is in the up position then initialize to the primed state
            kickerState = STATE_KICKER_PRIMED;
        }
        else
        {
            // assume kicker is in the fully extended position.
            kickerState = STATE_KICKER_DOWN;
        }
    }

    // return single instance of this class
	public static synchronized Kicker GetInstance()
    {
		if (kickerInstance == null)
        {
			kickerInstance = new Kicker();
		}
		return kickerInstance;
	}

    /**
     * debug function called as neccessary to print out state of kicker mechanism.
     */
    private void Print()
    {
        System.out.print("UNWIND:" + kickerSpinValue + "PRIMED:" + kickerUpValue);
        System.out.print(" STATE: ");
        switch (kickerState)
        {
            case STATE_KICKER_DOWN:      System.out.println("DOWN");      break;
            case STATE_KICKER_UP:        System.out.println("UP");        break;
            case STATE_KICKER_WAITING:   System.out.println("WAITING");   break;
            case STATE_KICKER_UNWINDING: System.out.println("UNWINDING"); break;
            case STATE_KICKER_PRIMED:    System.out.println("PRIMED");    break;
            case STATE_KICKER_FIRE:      System.out.println("FIRE");      break;
        }

    }


    /**
     * Called from main periodic control loop to update the kicker in response to
     * driverstation input and to move the kicker through it's prime/fire state
     * machine
     */
    public void Update()
    {
        Team467DriverStation driverstation = Team467DriverStation.GetInstance();
        if (driverstation.KickerOverride)
        {
            // Kicker override switch enabled - turn off all motors and don't do anything
            kick1.set(Relay.Value.kOff);
            kick2.set(Relay.Value.kOff);
            return;
        }

        // Read Inputs
        kickerUpValue = !kickerUp.get();
        kickerSpinValue = !kickerSpin.get();

        // State Machine used to automatically prime and fire kicker
        switch (kickerState)
        {
            case STATE_KICKER_DOWN:
                if (kickerUpValue == true)
                {
                    // Kicker fully up - turn off motor and move to next state
                    kick1.set(Relay.Value.kOff);
                    kick2.set(Relay.Value.kOff);
                    kickerState = STATE_KICKER_UP;
                }
                else
                {
                    // Prime kicker
                    kick1.set(Relay.Value.kReverse);
                    kick2.set(Relay.Value.kReverse);
                }
                break;

            case STATE_KICKER_UP:
                // Drive Servo to latch kicker in up position
                // Servos take time to activate, so wait until they are done
                latch.set(LATCH_LATCH);
                if (latchCount == LATCH_TIME) 
                {
                    // servo timer elapsed, move to next state
                    kickerState = STATE_KICKER_UNWINDING;
                    latchCount = 0;
                }
                else
                {
                    // keep marking time
                    latchCount++;
                }
                break;

            case STATE_KICKER_UNWINDING:
                if ((unwindCount > UNWIND_TIME) || (kickerSpinCount == 4))
                {
                    // unwind complete - move to next state
                    kick1.set(Relay.Value.kOff);
                    kick2.set(Relay.Value.kOff);
                    kickerState = STATE_KICKER_PRIMED;
                    kickerSpinCount = 0;
                    unwindCount = 0;
                }
                else
                {
                    unwindCount++;
                    kick1.set(Relay.Value.kForward);
                    kick2.set(Relay.Value.kForward);

                    // count number of times spinner has been triggered
                    if (kickerSpinValue == false && lastKickerSpin == true)
                    {
                        kickerSpinCount++;
                    }
                    lastKickerSpin = kickerSpinValue;
                }
                break;

            case STATE_KICKER_PRIMED:
                driverstation.SetKickerLED(true);
                if (driverstation.KickerFire == true)
                {
                    // Fire button pressed
                    kickerState = STATE_KICKER_FIRE;
                }
                break;

            case STATE_KICKER_FIRE:
                // don't time servo unlatch, kicker firing and priming time
                // will mask this. 
                if (kickerUpValue == false)
                {
                    kickerState = STATE_KICKER_DOWN;
                    driverstation.SetKickerLED(false);
                }
                else
                {
                    latch.set(LATCH_UNLATCH);
                }
                break;

        }

    }
    
    /**
     * Fire the kicker.
     * This function should only be called when firing from autonomous. 
     * In periodic mode, the kicker.Update() function directly reads the
     * driverstation button.
     */
    public void Fire()
    {
        if (kickerState == STATE_KICKER_PRIMED)
        {
            kickerState = STATE_KICKER_FIRE;
        }
    }

    /**
     * Is kicker Primed?
     * @return true if kicker is in primed state and ready to fire, false otherwise. 
     */
    public boolean Primed()
    {
        return (kickerState == STATE_KICKER_PRIMED);
    }

    /**
     * Used during system bringup to test/validate kicker functionality
     */
    public void Tune()
    {
        Team467DriverStation driverstation = Team467DriverStation.GetInstance();
       // Read Inputs
        kickerUpValue = !kickerUp.get();
        kickerSpinValue = !kickerSpin.get();

        if (driverstation.XboxLB)
        {
            kick1.set(Relay.Value.kReverse);
            kick2.set(Relay.Value.kReverse);
            if (kickerSpinValue == true && lastKickerSpin == false)
            {
                kickerSpinCount++;
            }
            lastKickerSpin = kickerSpinValue;
        }
        else if (driverstation.XboxRB)
        {
            kick1.set(Relay.Value.kForward);
            kick2.set(Relay.Value.kForward);
            if (kickerSpinValue == true && lastKickerSpin == false)
            {
                kickerSpinCount--;
            }
            lastKickerSpin = kickerSpinValue;
        }
        else
        {
            kickerSpinCount = 0;
            kick1.set(Relay.Value.kOff);
            kick2.set(Relay.Value.kOff);
        }

        if (driverstation.XboxA)
        {
            latch.set(LATCH_LATCH);
        }
        else if (driverstation.XboxY)
        {
            latch.set(LATCH_UNLATCH);
        }

        System.out.println("Kicker Up: " + kickerUpValue + " Spin Sensor: " + kickerSpinValue + " Spin Count: " + kickerSpinCount);
    }
}
