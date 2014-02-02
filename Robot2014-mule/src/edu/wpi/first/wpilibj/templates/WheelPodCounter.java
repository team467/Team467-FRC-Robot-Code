/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Counter;

/**
 *
 * @author kyle
 */
public class WheelPodCounter
{

    int ticksPerWheel;
    double wheelDiameter;
    long prevTime = System.currentTimeMillis();
    int prevCounterTicks = 0;
    Counter counter;

    /**
     * Object for use on reading the wheel sensors.
     *
     * @param channel - channel on the analog card.
     * @param numberOfTicksPerWheel - Number of tabs to detect per revolution of
     * the wheel.
     * @param wheelDiameter - diameter in inches. Supports decimals.
     */
    public WheelPodCounter(int channel, int numberOfTicksPerWheel, double wheelDiameter)
    {
        counter = new Counter(channel);
        counter.reset();
        prevCounterTicks = 0;
        ticksPerWheel = numberOfTicksPerWheel;
        this.wheelDiameter = wheelDiameter;
    }

    /**
     * Starts the counter.
     */
    public void start()
    {
        prevCounterTicks = counter.get();
        prevTime = System.currentTimeMillis();
        counter.start();
    }

    /**
     * Resets the counter.
     */
    public void reset()
    {
        prevCounterTicks = 0;
        prevTime = System.currentTimeMillis();
        counter.reset();
    }

    int currentCounterTicks;
    long currentTime;

    /**
     * Gets the speed in rev/sec based on the number of ticks since this
     * function was last called and the time since this function was last
     * called.
     *
     * <br/>
     * <br/>
     * <b>Note: only call this OR <i>getTravelRate()</i> once per cRIO tick and
     * save the value or there will not be enough time to get an accurate
     * reading.</b>
     *
     * @return - speed in rev/sec
     */
    public double getSpeed()
    {
        //saves the current vals to check only once so time and wheel ticks are not missed.
        currentCounterTicks = counter.get();
        currentTime = System.currentTimeMillis();
        //delta ticks / number of ticks per wheel (which equals # revs) / delta time (which equals rev/milis) * 1000 (which equals rev/sec)
        double result = (currentCounterTicks - prevCounterTicks) / ticksPerWheel / (currentTime - prevTime) * 1000;
        //update prev vals with current ones
        prevTime = currentTime;
        prevCounterTicks = currentCounterTicks;
        //return the rev/sec
        return result;

    }

    /**
     * Gets the speed in feet/sec based on the number of ticks since this
     * function was last called and the time since this function was last
     * called.
     *
     * <br/>
     * <br/>
     * <b>Note: only call this OR <i>getSpeed()</i> once per cRIO tick and save
     * the value or there will not be enough time to get an accurate
     * reading.</b>
     *
     * @return rate in feet/sec
     */
    public double getTravelRate()
    {
        //returns speed in rps * wheel circumfirence in feet
        return getSpeed() * wheelDiameter * Math.PI / 12;
    }

    /**
     * Gets the raw number of counter tick since the robot has started.
     *
     * @return
     */
    public int getRawCounterTicks()
    {
        return counter.get();
    }

}
