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
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

/**
 * Class displays frame and decides to write to file and if online mode push to
 * the cRIO or quit
 *
 * @author Kyle
 */
public class Frame extends javax.swing.JFrame
{

    boolean completePush = true;
    //bool used to decide to draw a line each iteration through the wheels array
    private static boolean drawLine = true;
    
    GridBagConstraints c;
    
    public static Checkbox FrontLeftCheck;
    public static Checkbox FrontRightCheck;
    public static Checkbox BackLeftCheck;
    public static Checkbox BackRightCheck;
    public static Button sendButton;
    public static Button pullButton;
    public static JTextArea outputConsole = new JTextArea();
    public static JScrollPane outputConsoleScrollBar = new JScrollPane();

    /**
     * Constructor to setup frame
     */
    public Frame()
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
     *
     * @param Wheels Arraylist of wheels to use the data stored in each wheel
     */
    public void run(ArrayList<Wheel> Wheels)
    {

        c = new GridBagConstraints();
        setupGridBag(c, Wheels);
        BufferStrategy buffer = this.getBufferStrategy();

        //update loop        
        while (true)
        {
            //will not update the draw loop while recomputing data as it causes conflicts
            if (!WheelSpeedCalibrationMap.regraphing)
            {
                //draws and shows the graph
                draw(buffer.getDrawGraphics(), Wheels);
                buffer.show();
            }
            try
            {
                Thread.sleep(1000);//WheelSpeedCalibrationMap.FRAME_SLEEP);
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * sets up checkboxes and buttons
     *
     * @param c Gridbag Constants
     */
    private void setupGridBag(GridBagConstraints c, ArrayList<Wheel> Wheels)
    {
        int FrontLeftNum = 0;
        int FrontRightNum = 0;
        int BackLeftNum = 0;
        int BackRightNum = 0;
        

        for (Wheel w : Wheels)
        {
            FrontRightNum = numValidValues("FrontRight", w, FrontRightNum);
            FrontLeftNum = numValidValues("FrontLeft", w, FrontLeftNum);
            BackRightNum = numValidValues("BackRight", w, BackRightNum);
            BackLeftNum = numValidValues("BackLeft", w, BackLeftNum);
        }

        FrontLeftCheck = new Checkbox("Front Left  # of Used Values: [" + FrontLeftNum + "]");
        FrontLeftCheck.setForeground(WheelSpeedCalibrationMap.FRONT_LEFT_COLOR);
        FrontRightCheck = new Checkbox("Front Right   # of Used Values: [" + FrontRightNum + "]");
        FrontRightCheck.setForeground(WheelSpeedCalibrationMap.FRONT_RIGHT_COLOR);
        BackLeftCheck = new Checkbox("Back Left  # of Used Values: [" + BackLeftNum + "]");
        BackLeftCheck.setForeground(WheelSpeedCalibrationMap.BACK_LEFT_COLOR);
        BackRightCheck = new Checkbox("Back Right   # of Used Values: [" + BackRightNum + "]");
        BackRightCheck.setForeground(WheelSpeedCalibrationMap.BACK_RIGHT_COLOR);

        sendButton = new Button("Send Values");
        pullButton = new Button("Refresh Graph ");

        Label offlineLabel = new Label("Set to Connect to Robot: " + WheelSpeedCalibrationMap.pullFromRobot);
        FrontLeftCheck.setState(true);
        FrontRightCheck.setState(true);
        BackLeftCheck.setState(true);
        BackRightCheck.setState(true);

        outputConsole.setRows(5);
        outputConsole.setColumns(20);
        outputConsoleScrollBar.setViewportView(outputConsole);
        
        addCheckboxToGridBag(BackLeftCheck, 0);                
        addCheckboxToGridBag(BackRightCheck, 1);        
        addCheckboxToGridBag(FrontLeftCheck, 2);
        addCheckboxToGridBag(FrontRightCheck, 3);        
        addButtonToGridBag(sendButton, 4);        
        addButtonToGridBag(pullButton, 5);        
        addLabelToGridBag(offlineLabel, 6);        
        addTextAreaToGridBag(outputConsole, 7);
        addActionListeners(sendButton, pullButton);
    }

    /**
     * * Lists the number of valid values for each wheel, output can be seen in
     * brackets next to the checkboxes
     *
     * @param wheelKey     Key to indicate each value
     * @param w            Wheel for the GraphPoints to be read from
     * @param incrementInt Int used to list number of used values on each wheel
     * @return
     */
    private static int numValidValues(String wheelKey, Wheel w, int incrementInt)
    {
        if (wheelKey.equals(w.name))
        {
            for (GraphPoint p : w.points)
            {
                if (p.used)
                {
                    incrementInt++;
                }
            }
        }
        return incrementInt;
    }

    /**
     * Adds Listeners to each checkbox and button for the GUI to check each
     * wheel
     *
     * @param sendButton Button Send
     * @param quitButton Button Quit
     */
    private void addActionListeners(Button sendButton, Button pullButton)
    {

        sendButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                if (completePush)
                {
                    WriteToFile.addToFile();

                    if (WheelSpeedCalibrationMap.pullFromRobot)
                    {
                        FTPUtilities.transmitPreferences(ServerOperationEnum.PUSH);
                    }
                    completePush = false;
                    System.exit(0);
                }
            }
        });

        pullButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                WheelSpeedCalibrationMap.regraphing = true;
                //resets previous vals so that it will read the file again w/ previous values still being there
                for (Wheel w : WheelSpeedCalibration.wheels)
                {
                    w.points.clear();
                }

                if (WheelSpeedCalibrationMap.pullFromRobot)
                {
                    FTPUtilities.transmitPreferences(ServerOperationEnum.PULL);
                }

                //reads through the file and write the values to the wheels ArrayList
                ParseFile.readAndParseFile();

                for (Wheel w : WheelSpeedCalibration.wheels)
                {
                    //filter data to remove all "NaN" and "0.0" values
                    w.points = Utilities.removeZeros(w.points);

                    //normalize data points to have a speed value between -1.0 and 1.0
                    w.doubleArrayList = Utilities.normalizeValues(w.points);

                    //runs line fit on data both forward (POS) and backward (NEG), then filters the data that is more than a 
                    // certian distance away from the line to be unused.
                    w.negPoints = Utilities.LeastSquaredRegression(w.doubleArrayList.negArrayList, WheelSpeedCalibrationMap.BACKWARD);
                    w.posPoints = Utilities.LeastSquaredRegression(w.doubleArrayList.posArrayList, WheelSpeedCalibrationMap.FORWARD);

                    //runs fit on data again to make the computed least squared regression line not use the outliers filtered out 
                    //by the previous run
                    w.negPoints = Utilities.LeastSquaredRegression(w.doubleArrayList.negArrayList, WheelSpeedCalibrationMap.BACKWARD);
                    w.posPoints = Utilities.LeastSquaredRegression(w.doubleArrayList.posArrayList, WheelSpeedCalibrationMap.FORWARD);

                    //prints out slope and y int vals
                    if (WheelSpeedCalibrationMap.DEBUG_MODE)
                    {
                        System.out.println("=== Point 1 Pos ===");
                        System.out.println(w.posPoints.point1.x);
                        System.out.println(w.posPoints.point1.y);
                        System.out.println("=== Point 2 Pos ===");
                        System.out.println(w.posPoints.point2.x);
                        System.out.println(w.posPoints.point2.y);
                        System.out.println("=== Point 1 Neg ===");
                        System.out.println(w.negPoints.point1.x);
                        System.out.println(w.negPoints.point1.y);
                        System.out.println("=== Point 2 Neg ===");
                        System.out.println(w.negPoints.point2.x);
                        System.out.println(w.negPoints.point2.y);
                    }
                }
                System.out.println("Refresh of Perferences complete!");
                WheelSpeedCalibrationMap.regraphing = false;
            }
        });

    }

    /**
     * Adds the JTextArea to GridBag
     *
     * @param c        GridbagConstants
     * @param checkbox checkbox object to add
     * @param gridy    sets the y position of each object in the GridBag
     */
    private void addTextAreaToGridBag(JTextArea textArea, int gridy)
    {
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;
        c.weightx = 0.0;
        c.gridwidth = 1;
        c.insets = new Insets(0, WheelSpeedCalibrationMap.GRAPH_SIZE_X + 20, 0, 0);  //top padding
        c.gridx = 0;
        c.gridy = gridy;
        this.add(textArea, c);
    }

    /**
     * Adds the checkboxs to GridBag
     *
     * @param c        GridbagConstants
     * @param checkbox checkbox object to add
     * @param gridy    sets the y position of each object in the GridBag
     */
    private void addCheckboxToGridBag(Checkbox checkbox, int gridy)
    {
        c = new GridBagConstraints();
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
     *
     * @param c      GridBag Constants
     * @param button Button to add to GridBag
     * @param gridy  y position to add to grid
     */
    private void addButtonToGridBag(Button button, int gridy)
    {
        c = new GridBagConstraints();
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
     *
     * @param c     GridBag Constants
     * @param label Label to add to GridBag
     * @param gridy y position to add to grid
     */
    private void addLabelToGridBag(Label label, int gridy)
    {
        c = new GridBagConstraints();
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
     *
     * @param g      Graphics from the buffer
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
                        if (FrontRightCheck.getState())
                        {
                            g.setColor((!p.used) ? WheelSpeedCalibrationMap.UNUSED_COLOR : WheelSpeedCalibrationMap.FRONT_RIGHT_COLOR);
                            drawLine = true;
                        }
                        else
                        {
                            drawLine = false;
                        }
                        break;

                    case WheelSpeedCalibrationMap.FRONT_LEFT:
                        if (FrontLeftCheck.getState())
                        {
                            g.setColor((!p.used) ? WheelSpeedCalibrationMap.UNUSED_COLOR : WheelSpeedCalibrationMap.FRONT_LEFT_COLOR);
                            drawLine = true;
                        }
                        else
                        {
                            drawLine = false;
                        }
                        break;
                    case WheelSpeedCalibrationMap.BACK_RIGHT:
                        if (BackRightCheck.getState())
                        {
                            g.setColor((!p.used) ? WheelSpeedCalibrationMap.UNUSED_COLOR : WheelSpeedCalibrationMap.BACK_RIGHT_COLOR);
                            drawLine = true;
                        }
                        else
                        {
                            drawLine = false;
                        }
                        break;
                    case WheelSpeedCalibrationMap.BACK_LEFT:
                        if (BackLeftCheck.getState())
                        {
                            g.setColor((!p.used) ? WheelSpeedCalibrationMap.UNUSED_COLOR : WheelSpeedCalibrationMap.BACK_LEFT_COLOR);
                            drawLine = true;
                        }
                        else
                        {
                            drawLine = false;
                        }
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
    private static double scaleX(double x)
    {
        return (x * WheelSpeedCalibrationMap.SIZE_X_SCALING + (WheelSpeedCalibrationMap.GRAPH_SIZE_X / 2));
    }

    private static double scaleY(double y)
    {
        return ((WheelSpeedCalibrationMap.GRAPH_SIZE_Y / 2) - (y * (WheelSpeedCalibrationMap.GRAPH_SIZE_Y / 2)));


    }
}

/**
 * Thread setup called by WheelSpeedCalibration to run the function
 *
 * @author Kyle
 */
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
        Frame f = new Frame();
        f.run(this.wheels);
        runner.start();
    }        
}
