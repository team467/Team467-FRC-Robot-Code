/*
 * LlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlama
 * LlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlama
 * LlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlamaLlama
 *
 */


//MORE LLAMAS
//ALL THE LLAMAS


package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CANJaguar.NeutralMode;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 * @author Llama
 */
public class Llamahead
{
    //Creates class instance
    private static Llamahead instance;

    //Output objects
//    private CANJaguar launchMotor;
    private Relay scoopMotor;
    private Relay neckMotor;
    private Relay intakeMotor;

    //Input objects
    private DigitalInput ballSensor;
    private GearTooth467 gearTooth;

    //Number of teeth on the gear measuring speed
    private final int TEETH = 1;

    //Direction constants
    public static final int FORWARD = 0;
    public static final int BACKWARD = 1;
    public static final int STOP = 2;
    public static final int LAUNCH = 3;

    //Speed constants
    static final double SPEED_FRONT_KEY = 38.0;
    static final double SPEED_BACK_KEY = 42.0; //42 drops directly into basket
    static final double SPEED_BRIDGE = 50.0;

    //Proportional gain (P in PID)
    private final double GAIN = 1.0 / 64.0; //1.0 / 300.0

    //Threshold of acceptability for proportionally controlled speed
    private final double AT_SPEED_THRESHOLD = 0.5;

    //Threshold for determining when to drive at full speed
    private final double FULL_SPEED_THRESHOLD = 3.0;

    //Sampling rate constant (number of iterations waited before applying proportional
    //gain
    private final double SAMPLING_TIME = 25;

    //Number of iterations the speed must be correct for the ballSensor to launch
    private final double CORRECT_SPEED_TIME = 20;

    //Maximum speed that can be expected from the launcher in rotations / second
    private final double SPEED_MAX = 50.0;

    //Number of times a ball has been launched
    private int launchCount = 0;

    //Debounce for determining a ball has been launched
    private boolean launchCountDebounce = false;

    /**
     * Gets the single instance of this class
     * @return
     */
    public static Llamahead getInstance()
    {
        if (instance == null)
        {
            instance = new Llamahead();
        }
        return instance;
    }

    //Private constructor for singleton
    private Llamahead()
    {
//        try
//        {
//            //Create motor objects
////            launchMotor = new CANJaguar(RobotMap.LLAMAHEAD_LAUNCH_MOTOR_CHANNEL);
//        }
//        catch (CANTimeoutException ex)
//        {
//            ex.printStackTrace();
//        }
        scoopMotor = new Relay (RobotMap.LLAMAHEAD_SCOOP_MOTOR_CHANNEL);
        intakeMotor = new Relay (RobotMap.LLAMAHEAD_INTAKE_MOTOR_CHANNEL);
        neckMotor = new Relay (RobotMap.LLAMAHEAD_NECK_MOTOR_CHANNEL);

        //Create sensor objects
        gearTooth = new GearTooth467(RobotMap.LLAMAHEAD_LAUNCH_SPEED_SENSOR_CHANNEL, TEETH);
        ballSensor = new DigitalInput(RobotMap.LLAMAHEAD_BALL_SENSOR_CHANNEL);

        //Start geartooth sensor
        gearTooth.start();

        //Set motor to coast
//        try
//        {
//            launchMotor.configNeutralMode(CANJaguar.NeutralMode.kCoast);
//        }
//        catch (CANTimeoutException ex)
//        {
//            ex.printStackTrace();
//        }
    }

    /**
     * Gets status of ballSensor sensor
     * @return
     */
    public boolean ballStatus()
    {
        return ballSensor.get();
    }

    /**
     * Advances balls along towards the launcher. Can be given either Llamahead.FORWARD or Llamahead.STOP
     * @param value The value to set the ballSensor advance motor to
     */
    public void setNeckAdvance(int value)
    {
        switch (value)
        {
            case FORWARD:

                //Turns neck on
                if (ballStatus())
                {
                    neckMotor.set(Relay.Value.kReverse);
                }
                else
                {
                    neckMotor.set(Relay.Value.kOff);
                }
                break;
            case BACKWARD:
                neckMotor.set(Relay.Value.kForward);
                break;
            case STOP:
                neckMotor.set(Relay.Value.kOff);
                break;
            case LAUNCH:
                //Special case lauch state to advance balls regardless of the
                //limit switch
                neckMotor.set(Relay.Value.kReverse);
                break;
        }
    }

    /**
     * Sets the direction of the ballSensor pickup motor. Can be given Llamahead.FORWARD
     * Llamahead.REVERSE or Llamahead.STOP
     * @param value The value to set the ballSensor pickup motor to
     */
    public void setBallIntake(int value)
    {
        switch (value)
        {
            case FORWARD:
                scoopMotor.set(Relay.Value.kForward);
                intakeMotor.set(Relay.Value.kReverse);
                break;
            case BACKWARD:
                scoopMotor.set(Relay.Value.kReverse);
                intakeMotor.set(Relay.Value.kForward);
                break;
            case STOP:
                scoopMotor.set(Relay.Value.kOff);
                intakeMotor.set(Relay.Value.kOff);
                break;
        }
    }

    //Ticks to determine if speed is correct for a certain amount of time
    private int correctSpeedTicks = 0;

    //Time that the ballSensor advance has been on during the launch function (essentially
    //it is the time spent at the correct speed and advancing balls)
    private int launchTime = 0;

    /**
     * Launch function that will drive the launch motor to the correct speed and
     * once it rests at that speed for a certain time, launches the balls
     * @param speed
     */
    public void launch(double speed)
    {
        //Drive launcher wheel
        setLauncherWheel(speed);

        //Determine if at correct speed yet
        if (atSpeed())
        {
            //Launch if speed has been correct for enough time
            if (correctSpeedTicks > CORRECT_SPEED_TIME)
            {
                launchTime++;
                setNeckAdvance(LAUNCH);
            }
            else
            {
                setNeckAdvance(FORWARD);
            }
            correctSpeedTicks++;
        }
        else
        {
            correctSpeedTicks = 0;
            setNeckAdvance(FORWARD);
        }
    }

    /**
     * Get the amount of time the ballSensor advance has been moving while the launcher \
     * wheel is at the correct speed
     * @return The launch time in iterations
     */
    public int getLaunchTime()
    {
        return launchTime;
    }

    /**
     * Get the number of times a ball has been launched since resetLaunchCount() was
     * last called
     * @return
     */
    public int getLaunchCount()
    {
        return launchCount;
    }

    /**
     * Resets the count on the number of balls launched
     */
    public void resetLaunchCount()
    {
        launchCount = 0;
        launchCountDebounce = false;
    }

    /**
     * Stops the launcher wheel completely and resets all variables associated with
     * launching
     */
    public void stopLauncherWheel()
    {
        setLauncherWheel(0.0);
        setNeckAdvance(STOP);
        pwm = 0.0;
        correctSpeedTicks = 0;
        launchTime = 0;
    }

    //Whether or not the launch motor is at the correct speed
    private boolean atSpeed = false;

    //Motor pwm value
    private double pwm = 0.0;

    //Number of iterations
    private int samplingTicks = 0;

    double speedError = 0;
    /**
     * Drives the wheel that launches the ballSensor at the given speed (speed range is
     * from 0.0 to 1.0
     * @param speed The speed in revolutions per second
     */
    public void setLauncherWheel(double targetSpeed)
    {
        if (targetSpeed == -1)
        {
            //Print launch speed
            Driverstation.getInstance().println("Launch Speed: " + getLauncherSpeed(), 3);
            driveLaunchMotor(1.0);
            return;
        }
        speedError = targetSpeed - getLauncherSpeed();
        //Don't allow neg speeds
        if (targetSpeed < 0.0) targetSpeed = 0.0;

        //Determine special cases
        if (targetSpeed == 0.0)
        {
            //Drive straight to 0 if target speed is 0.0
            driveLaunchMotor(0.0);
        }
        else if (speedError > FULL_SPEED_THRESHOLD)
        {
            //Drive at full power if difference between target and current speed
            //is greater than a set threshold (ramp up quickly)
            driveLaunchMotor(1.0);

            //Estimate where the pwm needs to be when switching over to proportional
            //control
            pwm = getLauncherSpeed() / SPEED_MAX;

            //Increment ball launch count if speed has gone from proportional control
            //down to full speed control
            if (launchCountDebounce && speedError > FULL_SPEED_THRESHOLD + 1.0)
            {
                launchCount ++;
                launchCountDebounce = false;
            }
        }
        else
        {
            //Apply proportional gain only after a number of ticks corresponding
            //to the sampling rate
            if (samplingTicks == SAMPLING_TIME)
            {
                pwm += speedError * GAIN;
                if (pwm > 1.0)
                {
                    pwm = 1.0;
                }
                if (pwm < 0.0)
                {
                    pwm = 0.0;
                }
                samplingTicks = 0;
            }

            //Drive to target speed
            driveLaunchMotor(pwm);

            //Signal that proportional control has taken over for sake of counting
            //launches
            launchCountDebounce = true;

            samplingTicks ++;
        }
        //Determine if at target speed
        atSpeed = (Math.abs(speedError) < AT_SPEED_THRESHOLD);

        Driverstation.getInstance().println("At speed: " + atSpeed(), 5);

        //Print launch speed
        Driverstation.getInstance().println("Launch Speed: " + getLauncherSpeed(), 3);

        //Print error
        Driverstation.getInstance().println("Error: " + speedError, 4);
    }

    /**
     * Whether or not the launch motor is at the target speed
     * @return
     */
    public boolean atSpeed()
    {
        return atSpeed;
    }

    /**
     * Returns the speed of the launcher motor as read by the geartooth sensor
     * @return
     */
    public double getLauncherSpeed()
    {
        return gearTooth.getAngularSpeed();
    }

    /**
     * Get the error between the desired launch speed and the actual launch speed
     * @return
     */
    public double getError()
    {
        return speedError;
    }

    boolean brakeDebounce = true;
    boolean coastDebounce = true;

    /**
     * Function to reduce space occupied by the motor drive call (eliminates need
     * to have try-catch every time setX is called)
     * @param d
     */
    public void driveLaunchMotor(double d)
    {
        if (d == 0.0 && coastDebounce)
        {
            //Set motor to coast
//            try
//            {
//                launchMotor.configNeutralMode(CANJaguar.NeutralMode.kCoast);
//            }
//            catch (CANTimeoutException ex)
//            {
//                ex.printStackTrace();
//            }
            brakeDebounce = true;
            coastDebounce = false;
        }
        if (d != 0.0 && brakeDebounce)
        {
            //Set motor to brake
//            try
//            {
//                launchMotor.configNeutralMode(CANJaguar.NeutralMode.kBrake);
//            }
//            catch (CANTimeoutException ex)
//            {
//                ex.printStackTrace();
//            }
            brakeDebounce = false;
            coastDebounce = true;
        }


        //Drive motor
//        try
//        {
//            launchMotor.setX(d);
//        }
//        catch (CANTimeoutException ex)
//        {
//            ex.printStackTrace();
//        }
    }

    public void setJaguarMode(NeutralMode mode)
    {
//        try
//        {
//            launchMotor.configNeutralMode(mode);
//        }
//        catch (CANTimeoutException ex)
//        {
//            ex.printStackTrace();
//        }
    }
}
