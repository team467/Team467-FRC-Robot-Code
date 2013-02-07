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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.event.ItemEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 * Class displays frame and decides to write to file and if online mode push
 * to the cRIO or quit
 *
 * @author Kyle
 */
public class FrameClass extends JFrame
{

    boolean completePush = true;
    
    /* Bools for drawing each line*/
    public static boolean FRONT_RIGHT = true;
    public static boolean FRONT_LEFT = true;
    public static boolean BACK_RIGHT = true;
    public static boolean BACK_LEFT = true;
    public static boolean OK = false;
    
    //bool used to decide to draw a line each iteration through the wheels array
    private static boolean drawLine = true;

    /**
     * constructor sets up frame
     */
    public FrameClass()
    {
        setSize(new Dimension(WheelSpeedCalibrationMap.SCREEN_SIZE_X, WheelSpeedCalibrationMap.SCREEN_SIZE_Y));
        setVisible(true);
        setResizable(false);
        createBufferStrategy(2);
        setIconImage(null);
        setTitle("Team 467 Wheel Speed Calibration Utility");
        setFocusable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
    }

    /**
     * displays frame and runs an update loop for graphics and selection of
     * choice in writing
     * @param Wheels Arraylist of wheels to use the data stored in each wheel
     */
    public void run(ArrayList<Wheel> Wheels)
    {
        GridBagConstraints c = new GridBagConstraints();
        BufferStrategy buffer = this.getBufferStrategy();
        setupGridBag(c);

        //update loop        
        while (true)
        {

            draw(buffer.getDrawGraphics(), Wheels);
            buffer.show();
            try
            {
                Thread.sleep(WheelSpeedCalibrationMap.FRAME_SLEEP);
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(FrameClass.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * sets up checkboxes and buttons     
     * @param c Gridbag Constants
     */
    private void setupGridBag(GridBagConstraints c)
    {

        Checkbox FrontLeftCheck = new Checkbox("Front Left");
        Checkbox FrontRightCheck = new Checkbox("Front Right");
        Checkbox BackLeftCheck = new Checkbox("Back Left");
        Checkbox BackRightCheck = new Checkbox("Back Right");

        Button okButton = new Button("Send");
        Button cancelButton = new Button("Quit");

        Label offlineLabel = new Label("Push to Robot: " + !WheelSpeedCalibrationMap.OFF_LINE_MODE);
        FrontLeftCheck.setState(FRONT_LEFT);
        FrontRightCheck.setState(FRONT_RIGHT);
        BackLeftCheck.setState(BACK_LEFT);
        BackRightCheck.setState(BACK_RIGHT);

        addCheckboxToGridBag(c, BackLeftCheck, 0);
        addCheckboxToGridBag(c, BackRightCheck, 1);
        addCheckboxToGridBag(c, FrontLeftCheck, 2);
        addCheckboxToGridBag(c, FrontRightCheck, 3);
        addButtonToGridBag(c, okButton, 4);
        addButtonToGridBag(c, cancelButton, 5);
        addLabelToGridBag(c, offlineLabel, 6);

        addActionListeners(FrontLeftCheck, FrontRightCheck, BackLeftCheck, BackRightCheck, okButton, cancelButton);        
    }

    private void addActionListeners(Checkbox FrontLeft, Checkbox FrontRight, Checkbox BackLeft, Checkbox BackRight, Button okButton, Button cancelButton)
    {

        okButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                if (completePush)
                {
                    WriteToFile.addToFile();

                    if (!WheelSpeedCalibrationMap.OFF_LINE_MODE)
                    {
                        FTPUtilities.transmitPreferences(ServerOperationEnum.PUSH);
                    }
                    completePush = false;
                    System.exit(0);
                }
            }
        });

        cancelButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                System.exit(0);
            }
        });
        FrontLeft.addItemListener(new java.awt.event.ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                FRONT_LEFT = !FRONT_LEFT;
                //System.out.println("Front Left " + FRONT_LEFT);
            }
        });
        FrontRight.addItemListener(new java.awt.event.ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                FRONT_RIGHT = !FRONT_RIGHT;
                //System.out.println("Front Right " + FRONT_RIGHT);
            }
        });
        BackLeft.addItemListener(new java.awt.event.ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                BACK_LEFT = !BACK_LEFT;
                //System.out.println("Back Left " + BACK_LEFT);
            }
        });
        BackRight.addItemListener(new java.awt.event.ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                BACK_RIGHT = !BACK_RIGHT;
                //System.out.println("Back Right " + BACK_RIGHT);
            }
        });
    }

    /**
     * Adds the checkboxs to GridBag
     * @param c GridbagConstants
     * @param checkbox checkbox object to add
     * @param gridy sets the y pos of each object
     */
    private void addCheckboxToGridBag(GridBagConstraints c, Checkbox checkbox, int gridy)
    {
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;
        c.weightx = 0.0;
        c.gridwidth = 1;
        c.insets = new Insets(0, WheelSpeedCalibrationMap.GRAPH_SIZE_X + 20, 0, 0);  //top padding
        c.gridx = 0;
        c.gridy = gridy;
        this.add(checkbox, c);
    }

    /**
     * Adds the buttons to GridBag
     * @param c GridBag Constants
     * @param button Button to add to GridBag
     * @param gridy y position to add to grid
     */
    private void addButtonToGridBag(GridBagConstraints c, Button button, int gridy)
    {
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;
        c.weightx = 0.0;
        c.gridwidth = 1;
        c.insets = new Insets(0, WheelSpeedCalibrationMap.GRAPH_SIZE_X + 20, 0, 0);  //top padding
        c.gridx = 0;
        c.gridy = gridy;
        this.add(button, c);
    }

    /**
     * Adds the label to GridBag
     * @param c GridBag Constants
     * @param label Label to add to GridBag
     * @param gridy y position to add to grid
     */
    private void addLabelToGridBag(GridBagConstraints c, Label label, int gridy)
    {
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;
        c.weightx = 0.0;
        c.gridwidth = 1;
        c.insets = new Insets(0, WheelSpeedCalibrationMap.GRAPH_SIZE_X + 20, 0, 0);  //top padding
        c.gridx = 0;
        c.gridy = gridy;
        this.add(label, c);
    }

    /**
     * Function to update the graphing of each wheel
     * @param g Graphics from the buffer
     * @param wheels wheels ArrayList to get values from
     */
    private static void draw(Graphics g, ArrayList<Wheel> wheels)
    {
        //draw white background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WheelSpeedCalibrationMap.GRAPH_SIZE_X, WheelSpeedCalibrationMap.GRAPH_SIZE_Y);
        
        //set draw color to black axies
        g.setColor(Color.BLACK);
        
        //draw horizontal axis line
        g.drawLine(0, (WheelSpeedCalibrationMap.GRAPH_SIZE_Y / 2), WheelSpeedCalibrationMap.GRAPH_SIZE_X, (WheelSpeedCalibrationMap.GRAPH_SIZE_Y / 2));
        //draw vertical axis line
        g.drawLine((WheelSpeedCalibrationMap.GRAPH_SIZE_X / 2), 0, (WheelSpeedCalibrationMap.GRAPH_SIZE_X / 2), WheelSpeedCalibrationMap.GRAPH_SIZE_Y);

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
                        if (FRONT_RIGHT)
                        {
                            if (!p.used)
                            {
                                g.setColor(Color.BLUE);
                            }
                            else
                            {
                                g.setColor(Color.RED);
                            }
                            drawLine = true;
                        }
                        else
                        {
                            drawLine = false;        
                        }
                        break;
                        
                    case WheelSpeedCalibrationMap.FRONT_LEFT:
                        if (FRONT_LEFT)
                        {
                            if (!p.used)
                            {
                                g.setColor(Color.BLUE);
                            }
                            else
                            {
                                g.setColor(Color.ORANGE);
                            }
                            drawLine = true;
                        }
                        else
                        {
                            drawLine = false;                            
                        }
                        break;
                    case WheelSpeedCalibrationMap.BACK_RIGHT:
                        if (BACK_RIGHT)
                        {
                            if (!p.used)
                            {
                                g.setColor(Color.BLUE);
                            }
                            else
                            {
                                g.setColor(Color.MAGENTA);
                            }
                            drawLine = true;
                        }
                        else
                        {
                            drawLine = false;
                        }
                        break;
                    case WheelSpeedCalibrationMap.BACK_LEFT:
                        if (BACK_LEFT)
                        {
                            if (!p.used)
                            {
                                g.setColor(Color.BLUE);
                            }
                            else
                            {
                                g.setColor(Color.GREEN);
                            }
                            drawLine = true;
                        }
                        else
                        {
                            drawLine = false;
                        }
                        break;
                }
                if (drawLine)
                {
                    g.fillRect((int) (scaleX(p.speed)),
                            (int) scaleY(p.power),
                            WheelSpeedCalibrationMap.GRID_SQUARE_SIZE,
                            WheelSpeedCalibrationMap.GRID_SQUARE_SIZE);
                }
            }
            if (drawLine)
            {
                g.drawLine((int) (scaleX(w.posPoints.point1.x)),
                        (int) (scaleY(w.posPoints.point1.y)),
                        (int) (scaleX(w.posPoints.point2.x)),
                        (int) (scaleY(w.posPoints.point2.y)));
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
    private static double scaleX(double x)
    {
        return (x * WheelSpeedCalibrationMap.SIZE_X_SCALING + (WheelSpeedCalibrationMap.GRAPH_SIZE_X / 2));
    }

    private static double scaleY(double y)
    {
        return ((WheelSpeedCalibrationMap.GRAPH_SIZE_Y / 2) - (y * (WheelSpeedCalibrationMap.GRAPH_SIZE_Y / 2)));
    }
}

class RunnableThread implements Runnable
{

    Thread runner;
    protected ArrayList<Wheel> wheels;

    public RunnableThread(String threadName, ArrayList<Wheel> wheels)
    {
        runner = new Thread(this, threadName); // (1) Create a new thread.
        System.out.println("Starting " + runner.getName());
        this.wheels = wheels;
        //ServerGUI.printToOutput("Starting " + runner.getName());
        //this.run(wheels);
    }

    @Override
    public void run()
    {
        FrameClass f = new FrameClass();
        f.run(this.wheels);
        runner.start();
    }
}