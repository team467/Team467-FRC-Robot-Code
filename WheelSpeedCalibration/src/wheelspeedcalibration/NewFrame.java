/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelspeedcalibration;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Kyle
 */
public class NewFrame extends JFrame
{

    public JPanel graphPanel;
    public JPanel buttonPanel;
    public static JCheckBox FrontLeftCheck;
    public static JCheckBox FrontRightCheck;
    public static JCheckBox BackLeftCheck;
    public static JCheckBox BackRightCheck;
    public static JButton sendButton;
    public static JButton pullButton;
    public static JTextArea outputConsole;
    public static JScrollPane outputConsoleScrollBar;
    //bool used to decide to draw a line each iteration through the wheels array
    private static boolean drawLine = true;
    BufferStrategy buffer;

    public NewFrame()
    {
        setSize(new Dimension(WheelSpeedCalibrationMap.SCREEN_SIZE_X, WheelSpeedCalibrationMap.SCREEN_SIZE_Y));
        setVisible(true);
        setResizable(true);
        //setIconImage(null);
        setTitle("Team 467 Wheel Speed Calibration Utility");
        setFocusable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        //height,width
        setLayout(new GridLayout(0, 2));

        //GraphDrawingPanel is special type of JPanel that draw the graph
        graphPanel = new GraphDrawingPanel();
        add(graphPanel);
        
        //adds button panel which holds all of the UI Buttons
        buttonPanel = new JPanel();
        //setupButtonPanel();
        
        add(buttonPanel);        
    }

    /**
     *
     */
    private void setupButtonPanel()
    {
        //height,width
        buttonPanel.setLayout(new GridLayout(WheelSpeedCalibrationMap.NUM_BUTTONS_GUI, 0));

        FrontLeftCheck = new JCheckBox("Front Left  # of Used Values: [" + returnNumUsedVals("FrontLeft") + "]");
        FrontLeftCheck.setForeground(WheelSpeedCalibrationMap.FRONT_LEFT_COLOR);
        FrontRightCheck = new JCheckBox("Front Right   # of Used Values: [" + returnNumUsedVals("FrontRight") + "]");
        FrontRightCheck.setForeground(WheelSpeedCalibrationMap.FRONT_RIGHT_COLOR);
        BackLeftCheck = new JCheckBox("Back Left  # of Used Values: [" + returnNumUsedVals("BackLeft") + "]");
        BackLeftCheck.setForeground(WheelSpeedCalibrationMap.BACK_LEFT_COLOR);
        BackRightCheck = new JCheckBox("Back Right   # of Used Values: [" + returnNumUsedVals("BackRight") + "]");
        BackRightCheck.setForeground(WheelSpeedCalibrationMap.BACK_RIGHT_COLOR);

        sendButton = new JButton("Send Values");
        pullButton = new JButton("Refresh Graph ");

        buttonPanel.add(FrontLeftCheck);
        buttonPanel.add(FrontRightCheck);
        buttonPanel.add(BackLeftCheck);
        buttonPanel.add(BackRightCheck);
        buttonPanel.add(sendButton);
        buttonPanel.add(pullButton);
    }
    
    private int returnNumUsedVals(String wheelKey)
    {
        int numUsedPoints = 0;
        for (Wheel w : WheelSpeedCalibration.wheels)
        {
            if (w.key.equals(wheelKey))
            {
                numUsedPoints = w.numUsedPoints;
            }
        }
        return numUsedPoints;
    }

    private void draw(Graphics g, ArrayList<Wheel> wheels)
    {
        System.out.println("WIDTH: " + graphPanel.getBounds().width);
        System.out.println("HEIGHT: " + graphPanel.getBounds().height);
        //draw white background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, graphPanel.getBounds().width, graphPanel.getBounds().height);

        //set draw color to black axies
        g.setColor(Color.BLACK);

        //draw horizontal axis line
        g.drawLine(0, (graphPanel.getBounds().height / 2), graphPanel.getBounds().width, (graphPanel.getBounds().height / 2));
        //draw vertical axis line
        g.drawLine((graphPanel.getBounds().width / 2), 0, (graphPanel.getBounds().width / 2), graphPanel.getBounds().height);

        //writes down what the X and Y values mean
        g.drawString("X: Speed btw ~ -" + WheelSpeedCalibrationMap.SCREEN_X_RANGE / 2 + ".0 and " + WheelSpeedCalibrationMap.SCREEN_X_RANGE / 2 + ".0 RPS", 10, 40);
        g.drawString("Y: Power btw -" + WheelSpeedCalibrationMap.SCREEN_Y_RANGE / 2 + ".0 and " + WheelSpeedCalibrationMap.SCREEN_Y_RANGE / 2 + ".0 Pwr", 10, 55);

        for (Wheel w : wheels)
        {
            for (GraphPoint p : w.points)
            {
                switch (wheels.indexOf(w))
                {
                    //draws tht front right to ne 
                    case WheelSpeedCalibrationMap.FRONT_RIGHT:

                        g.setColor((!p.used) ? WheelSpeedCalibrationMap.UNUSED_COLOR : WheelSpeedCalibrationMap.FRONT_RIGHT_COLOR);
                        drawLine = true;
                        break;

                    case WheelSpeedCalibrationMap.FRONT_LEFT:

                        g.setColor((!p.used) ? WheelSpeedCalibrationMap.UNUSED_COLOR : WheelSpeedCalibrationMap.FRONT_LEFT_COLOR);
                        drawLine = true;
                        break;
                    case WheelSpeedCalibrationMap.BACK_RIGHT:
                        g.setColor((!p.used) ? WheelSpeedCalibrationMap.UNUSED_COLOR : WheelSpeedCalibrationMap.BACK_RIGHT_COLOR);
                        drawLine = true;
                        break;
                    case WheelSpeedCalibrationMap.BACK_LEFT:
                        g.setColor((!p.used) ? WheelSpeedCalibrationMap.UNUSED_COLOR : WheelSpeedCalibrationMap.BACK_LEFT_COLOR);
                        drawLine = true;
                        break;
                }
                //if drawline has not been turned off, draw each point
                if (drawLine)
                {
                    g.fillRect((int) (scaleX(p.speed)),
                            (int) scaleY(p.power),
                            WheelSpeedCalibrationMap.GRID_SQUARE_SIZE,
                            WheelSpeedCalibrationMap.GRID_SQUARE_SIZE);
                }
            }
            //if drawline has not been turned off, draw each line
            if (drawLine)
            {
                //forward line
                g.drawLine((int) (scaleX(w.posPoints.point1.x)),
                        (int) (scaleY(w.posPoints.point1.y)),
                        (int) (scaleX(w.posPoints.point2.x)),
                        (int) (scaleY(w.posPoints.point2.y)));
                //backward line
                g.drawLine((int) (scaleX(w.negPoints.point1.x)),
                        (int) (scaleY(w.negPoints.point1.y)),
                        (int) (scaleX(w.negPoints.point2.x)),
                        (int) (scaleY(w.negPoints.point2.y)));
            }
        }
    }
    //X: Scale up to half the x, from -1.0 to 1.0 up to -256.0 to 256.0,
    //   then shift the x val over to center
    //Y: take half the size of the screen and subtract from that to 
    //   center the Y, and scale the size from ~(-8.0 to 8.0)or so based on Y

    private double scaleX(double x)
    {
        return (x * (graphPanel.getBounds().width / WheelSpeedCalibrationMap.SCREEN_X_RANGE) + (graphPanel.getBounds().width / 2));
    }

    private double scaleY(double y)
    {
        return ((graphPanel.getBounds().height / 2) - (y * (graphPanel.getBounds().height / 2)));


    }

    private class GraphDrawingPanel extends JPanel
    {

        public void paint(Graphics g)
        {
            draw(g, WheelSpeedCalibration.wheels);
        }
    }
}
