/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.shs.first.team467;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SmartDashboard;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 *
 * @author USFIRST
 */
public class Lifter2011 {

    //Single class instance
    private static Lifter2011 instance;

    //Motor channels
    private final int SLIDER_CHANNEL = 8;
    private final int STAGE1_MOTOR1_CHANNEL = 1;
    private final int STAGE1_MOTOR2_CHANNEL = 2;
    private final int STAGE2_MOTOR_CHANNEL = 3;

    //Sensor channels
    private final int SLIDER_LIMIT_FRONT_CHANNEL = 7;
    private final int LIFT_STAGE1_LIMIT_TOP_CHANNEL = 10;//TBD
    private final int LIFT_STAGE1_LIMIT_BOTTOM_CHANNEL = 11;//TBD
    private final int LIFT_STAGE2_LIMIT_TOP_CHANNEL = 8;
    private final int LIFT_STAGE2_LIMIT_BOTTOM_CHANNEL = 9;

    //Motor objects
    private CANJaguar slider;
    private Relay stage1Motor1;
    private Relay stage1Motor2;
    private Relay stage2Motor;

    //Sensor objects
    private DigitalInput sliderSensorFront;
    private DigitalInput liftStage1SensorTop;
    private DigitalInput liftStage1SensorBottom;
    private DigitalInput liftStage2SensorTop;
    private DigitalInput liftStage2SensorBottom;

    //Private constructor so instances can't be created outside this class
    private Lifter2011()
    {
        try
        {
            slider = new CANJaguar(SLIDER_CHANNEL);
        }
        catch (CANTimeoutException ex)
        {
            ex.printStackTrace();
        }
        //Create lifter motors
        stage1Motor1 = new Relay(STAGE1_MOTOR1_CHANNEL);
        stage1Motor2 = new Relay(STAGE1_MOTOR2_CHANNEL);
        stage2Motor = new Relay(STAGE2_MOTOR_CHANNEL);

        //Create limit sensors
        sliderSensorFront = new DigitalInput(SLIDER_LIMIT_FRONT_CHANNEL);
        liftStage1SensorTop = new DigitalInput(LIFT_STAGE1_LIMIT_TOP_CHANNEL);
        liftStage1SensorBottom = new DigitalInput(LIFT_STAGE1_LIMIT_BOTTOM_CHANNEL);
        liftStage2SensorTop = new DigitalInput(LIFT_STAGE2_LIMIT_TOP_CHANNEL);
        liftStage2SensorBottom = new DigitalInput(LIFT_STAGE2_LIMIT_BOTTOM_CHANNEL);
    }

    public void logLimits()
    {
        SmartDashboard.log(liftStage1SensorTop.get(), "Stage1 Top Sensor");
        SmartDashboard.log(liftStage2SensorTop.get(), "Stage2 Top Sensor");
        SmartDashboard.log(liftStage1SensorBottom.get(), "Stage1 Bottom Sensor");
        SmartDashboard.log(liftStage2SensorBottom.get(), "Stage2 Bottom Sensor");
        SmartDashboard.log(sliderSensorFront.get(), "Slider Sensor");
    }

    /**
     * Returns the single instance of this class
     * @return See above
     */
    public static Lifter2011 getInstance()
    {
        if (instance == null)
        {
            instance = new Lifter2011();
        }
        return instance;
    }

    /**
     * Sets the speed to move the rack and pinion slider to move the lifter
     * forward.
     * @param magnitude The speed to move at. Positive values move forward,
     * negative values move back.
     */
    public void setSlider(double magnitude)
    {
        try
        {
            if ((sliderSensorFront.get() && magnitude > 0.0) || magnitude < 0.0)
            {
                slider.setX(magnitude);
            }
            else
            {
                slider.setX(0.0);
            }
        }
        catch (CANTimeoutException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Move the lifter first stage up
     */
    public void liftStage1()
    {
        if (liftStage1SensorTop.get())
        {
            stage1Motor1.set(Relay.Value.kForward);
            stage1Motor2.set(Relay.Value.kReverse);
        }
        else
        {
            stopStage1();
        }
    }

    /**
     * Move the lifter first stage down
     */
    public void lowerStage1()
    {
        if (liftStage1SensorBottom.get())
        {
            stage1Motor1.set(Relay.Value.kReverse);
            stage1Motor2.set(Relay.Value.kForward);
        }
        else
        {
            stopStage1();
        }
    }

    /**
     * Stops the first stage of the lifter
     */
    public void stopStage1()
    {
        stage1Motor1.set(Relay.Value.kOff);
        stage1Motor2.set(Relay.Value.kOff);
    }

    /**
     * Lifts the lifter second stage
     */
    public void liftStage2()
    {
        if (liftStage2SensorTop.get())
        {
            stage2Motor.set(Relay.Value.kReverse);
        }
        else
        {
            stopStage2();
        }
    }

    /**
     * Lowers the lifter second stage
     */
    public void lowerStage2()
    {
        if (liftStage2SensorBottom.get())
        {
            stage2Motor.set(Relay.Value.kForward);
        }
        else
        {
            stopStage2();
        }
    }

    /**
     * Stops the lifter second stage
     */
    public void stopStage2()
    {
        stage2Motor.set(Relay.Value.kOff);
    }

}
