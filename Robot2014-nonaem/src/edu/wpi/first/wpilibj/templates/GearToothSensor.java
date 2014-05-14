package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Counter;

/**
 *
 * @author Team467
 */
public class GearToothSensor
{

    private int ticksPerWheel;
    private double wheelCircumfrence;
    private long startTime;
    private long prevTime;
    private long curTime;
    private int prevTicks;
    private int curTicks;
    private boolean counting;
    
    private int channel = 0;

    private boolean accurateRPMenabled = true;

    private int rpmResolution = 20;
    private double[] rpmCache = new double[rpmResolution];

    private Counter counter;

    public GearToothSensor(int counterChannel, double circumfrence, int ticksPerWheel)
    {
        this.wheelCircumfrence = circumfrence;
        this.ticksPerWheel = ticksPerWheel;
        counter = new Counter(counterChannel);
        channel = counterChannel;

        counting = false;
    }

    public GearToothSensor(int counterChannel, double wheelDiameter, int ticksPerWheel, boolean accurateRPM)
    {
        this(counterChannel, wheelDiameter, ticksPerWheel);

        accurateRPMenabled = accurateRPM;
    }

    /**
     * Starts the counter.
     */
    public void start()
    {
        counting = true;

        counter.start();

        startTime = System.currentTimeMillis();
    }

    /**
     * Stops the counter.
     */
    public void stop()
    {
        counting = false;

        counter.stop();
    }

    /**
     * Resets the counter. Note that this method does not change the running
     * state of the counter, it only sets it to 0.
     */
    public void reset()
    {
        counter.reset();
    }

    /**
     * Checks whether the counter is active or not.
     *
     * @return
     */
    public boolean isCounting()
    {
        return counting;
    }

    /**
     * Updates the GearToothSensor to most current values. This should be called
     * before doing <i>anything</i> else to the sensor.
     * 
     */
    public void update()
    {
        if (counting)
        {
            prevTicks = curTicks;
            curTicks = counter.get();

            prevTime = curTime;
            curTime = System.currentTimeMillis();

            if (accurateRPMenabled)
            {
                for (int i = 1; i <= rpmResolution - 1; i++)
                {
                    rpmCache[i - 1] = rpmCache[i];
                }

                rpmCache[rpmResolution - 1] = getRawRPM();
            }
        }
    }

    /**
     * Gets the total ticks the sensor has counted.
     *
     * @return
     */
    public double getTicks()
    {
        return curTicks;
    }

    /**
     * Gets the current RPM.
     *
     * @return
     */
    public double getRawRPM()
    {
        double dTicks = curTicks - prevTicks;
        double dTime = curTime - prevTime;

        return (dTicks / (dTime * ticksPerWheel)) * 60000;
    }

    /**
     * Returns the average of the RPM values over several intervals, giving a
     * more stable RPM output for calculations.
     *
     * <b>Note:</b> this method is substantially slower than
     * {@code getRawRPM()}, and has a significant lag time between a change in
     * the RPM and a change in the readout.
     *
     * @return if enabled, accurate RPM. if disabled, raw RPM.
     */
    public double getAccurateRPM()
    {
        if (accurateRPMenabled)
        {
            double sum = 0;

            for (int i = 0; i <= rpmResolution - 1; i++)
            {
                sum += rpmCache[i];
            }

            if (curTicks < 20)
            {
                return 0.0;
            }
            else
            {
                return sum / rpmResolution;
            }
        }
        else
        {
            System.out.println("[GEARTOOTH_SENSOR] Accurate RPM not enabled, returning raw RPM instead.");
            return getRawRPM();
        }
    }

    /**
     * Converts an RPM of this specific wheel to a velocity in feet per second.
     *
     * @param RPM
     * @return
     */
    public double convertRPMtoVelocity(double RPM)
    {
        return (RPM * wheelCircumfrence) / 720;
    }
    /*
    public String toString() {
        String motorDisplay = "";
        
        switch(channel) {
            case RobotMap.FRONT_LEFT_GEAR_TOOTH_SENSOR_CHANNEL:
                motorDisplay = "FL";
                break;
            case RobotMap.FRONT_RIGHT_GEAR_TOOTH_SENSOR_CHANNEL:
                motorDisplay = "FR";
                break;
            case RobotMap.BACK_LEFT_GEAR_TOOTH_SENSOR_CHANNEL:
                motorDisplay = "BL";
                break;
            case RobotMap.BACK_RIGHT_GEAR_TOOTH_SENSOR_CHANNEL:
                motorDisplay = "BR";
                break;
        }
        
        return motorDisplay + ": " + getTicks();
    }
    */
}
