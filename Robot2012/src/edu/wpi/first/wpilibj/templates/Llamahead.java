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
import edu.wpi.first.wpilibj.can.CANNotInitializedException;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 * @author Llama
 */
public class Llamahead
{
    //Creates class instance
    private static Llamahead instance;
    
    //Output objects
    private CANJaguar launchMotor;
    private Relay scoopMotor;
    private Relay neckMotor;
    private Relay intakeMotor;
    
    //Input objects
    private DigitalInput ball;
    private GearTooth467 gearTooth;  
    
    //Number of teeth on the gear measuring speed
    private final int TEETH = 1;
    
    //Direction constants
    public static final int FORWARD = 0;
    public static final int BACKWARD = 1;
    public static final int STOP = 2;
    
    //Proportional gain (p in PID)
    private final double GAIN = 1.0 / 400.0;
     
    //Threshold of acceptability for pIDJaguar speed
    private final double AT_SPEED_THRESHOLD = 1.0;
    
    //Threshold for determining when to drive at full speed
    private final double FULL_SPEED_THRESHOLD = 25.0;
    
    //Sampling rate constant (number of iterations waited before applying proportional
    //gain
    private final double SAMPLING_TIME = 10;
    
    //Number of iterations the speed must be correct for the ball to launch
    private final double CORRECT_SPEED_TIME = 10;
    
    //Maximum speed that can be expected from the launcher in rotations / second
    private final double SPEED_MAX = 53.0;
    
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
        try
        {
            //Creating motor control objects
            launchMotor = new CANJaguar(RobotMap.LLAMAHEAD_LAUNCH_MOTOR_CHANNEL);
        }
        catch (CANTimeoutException ex)
        {
            ex.printStackTrace();
        }
        scoopMotor = new Relay (RobotMap.LLAMAHEAD_SCOOP_MOTOR_CHANNEL);
        intakeMotor = new Relay (RobotMap.LLAMAHEAD_INTAKE_MOTOR_CHANNEL); 
        neckMotor = new Relay (RobotMap.LLAMAHEAD_NECK_MOTOR_CHANNEL);
        //Create sensor objects
        gearTooth = new GearTooth467(RobotMap.LLAMAHEAD_LAUNCH_SPEED_SENSOR_CHANNEL, TEETH);
        //ball = new DigitalInput(RobotMap.LLAMAHEAD_BALL_SENSOR_CHANNEL);
    }
    
    /**
     * Gets status of ball sensor
     * @return 
     */
    public boolean ballStatus()
    {
        return false;
//        return ball.get();        
    }
    
    /**
     * Advances balls along. Can be given either Llamahead.FORWARD or Llamahead.STOP
     * @param value The value to set the ball advance motor to
     */
    public void setBallAdvance(int value)
    {
        switch (value)
        {
            case FORWARD:
                //Assumes that if there is no ball the sensor will return false 

                //Turns neck on
//                if (!ballStatus())
//                {
                    neckMotor.set(Relay.Value.kReverse);
//                }
                break;
            case BACKWARD:
                System.out.println("Ball Advance does not drive backward!!");
                break;
            case STOP:
                neckMotor.set(Relay.Value.kOff);
                break;
        }
    }
    
    /**
     * Sets the direction of the ball pickup motor. Can be given Llamahead.FORWARD
     * Llamahead.REVERSE or Llamahead.STOP
     * @param value The value to set the ball pickup motor to
     */
    public void setBallIntake(int value)
    {
        switch (value)
        {
            case FORWARD:              
                scoopMotor.set(Relay.Value.kReverse);
                intakeMotor.set(Relay.Value.kForward);
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
    
    //Time that the ball advance has been on during the launch function (essentially
    //it is the time spent at the correct speed and advancing balls)
    private int launchTime = 0;
    
    //Variable to determine the correct pwm value to use (saves this value when
    //speed is correct for a long enough period of time
    private double correctpwm = 0;
    
    //Variable to determine whether the launcher is finding the correct speed 
    //or whether it has already been found and needs to be used directly
    private boolean findingSpeed = true;
    
    /**
     * Launch function that will drive the launch motor to the correct speed and
     * once it rests at that speed for a certain time, launches the balls
     * @param speed 
     */
    public void launch(double speed)
    {
        if (findingSpeed)
        {
            //Drive launcher wheel
            setLauncherWheel(speed);

            //Determine if at correct speed yet
            if (atSpeed())
            {
                //Remember owm value if speed has been correct for long enough
                if (correctSpeedTicks > CORRECT_SPEED_TIME)
                {
                    try
                    {
                        correctpwm = launchMotor.getX();
                    }
                    catch (CANTimeoutException ex)
                    {
                        ex.printStackTrace();
                    }
                    findingSpeed = false;
                }
                correctSpeedTicks++;
            }
            else
            {
                correctSpeedTicks = 0;
            }
        }
        else
        {
            //Drive at determined correct power
            driveLaunchMotor(correctpwm);
            
            //Determine if at correct speed yet
            if (atSpeed())
            {
                //Launch if speed has been correct for enough time
                if (correctSpeedTicks > CORRECT_SPEED_TIME)
                {
                    launchTime++;
                    setBallAdvance(FORWARD);
                }
                else
                {
                    setBallAdvance(STOP);
                }
                correctSpeedTicks++;
            }
            else
            {
                correctSpeedTicks = 0;
                setBallAdvance(STOP);
            }
        }
        
        //Determine if at target speed
        atSpeed = (Math.abs(speed - getLauncherSpeed()) < AT_SPEED_THRESHOLD);
    }
    
    /**
     * Get the amount of time the ball advance has been moving while the launcher \
     * wheel is at the correct speed 
     * @return The launch time in iterations
     */
    public int getLaunchTime()
    {
        return launchTime;
    }
    
    /**
     * Stops the launcher wheel completely
     */
    public void stopLauncherWheel()
    {
        setLauncherWheel(0.0);
        pwm = 0.0;
        launchTime = 0;
        findingSpeed = true;
    }
    
    //Whether or not the luanch motor is at the correct speed
    private boolean atSpeed = false;
    
    //Motor pwm value
    private double pwm = 0.0;
    
    //Number of iterations
    private int samplingTicks = 0;
    
    /**
     * Drives the wheel that launches the ball at the given speed (speed range is
     * from 0.0 to 1.0
     * @param speed The speed in revolutions per second
     */
    private void setLauncherWheel(double targetSpeed)
    {
        //Dont allow neg speeds
        if (targetSpeed < 0.0) targetSpeed = 0.0;
        
        //Determine special cases
        if (targetSpeed == 0.0)
        {
            //Drive straight to 0 if target speed is 0.0
            driveLaunchMotor(0.0);
        }
        else if (targetSpeed - getLauncherSpeed() > FULL_SPEED_THRESHOLD)
        {
            //Drive at full power if difference between target and current speed
            //is greater than a set threshold (ramp up quickly)
            driveLaunchMotor(1.0);
            
            //Estimate where the pwm needs to be when switching over to proportional
            //control
            pwm = targetSpeed / SPEED_MAX;
        }
        else
        {
            //Apply proportional gain only after a number of ticks corresponding 
            //to the sampling rate
            if (samplingTicks == SAMPLING_TIME)
            {
                pwm += (targetSpeed - getLauncherSpeed()) * GAIN;
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
        }
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
     * Function to reduce space occupied by the motor drive call (eliminates need 
     * to have try-catch every time setX is called)
     * @param d 
     */
    private void driveLaunchMotor(double d)
    {
        try
        {
            launchMotor.setX(d);
        }
        catch (CANTimeoutException ex)
        {
            ex.printStackTrace();
        }
    }
}
