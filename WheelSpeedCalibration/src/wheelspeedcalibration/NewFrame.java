/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelspeedcalibration;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.axis.AxisSpace;
import org.jfree.chart.axis.TickUnitSource;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Kyle
 */
public class NewFrame extends JFrame
{

    boolean FrontLeft = true;
    boolean FrontRight = true;
    boolean BackLeft = true;
    boolean BackRight = true;
    XYPlot plot;
    ActionListener redrawGraph;
    ActionListener sendValues;
    ActionListener pullFile;
    ActionListener checkboxUpdated;
    ActionListener connectToRobotClick;
    KeyListener showHelp;
    XYSeriesCollection result;
    XYSeries FrontLeftSeries;
    XYSeries FrontRightSeries;
    XYSeries BackLeftSeries;
    XYSeries BackRightSeries;
    JFreeChart chart;
//    Label onlineLight;
    JButton connectToRobot;
    public ChartPanel chartPanel;
    public JPanel graphPanel;
    public JPanel graphPanelContainer;
    public JPanel controlPanel;
    public JPanel checkboxPanelContainer;
    public JPanel onlineCheckboxPanel;
    public JPanel usedCheckboxPanel;
    public JPanel wheelCheckboxPanel;
    public JPanel buttonPanel;
    public JPanel titlePanel;
    public JPanel outputPanel;
    public JPanel userInterfaceContainer;
    public static JCheckBox FrontLeftCheck;
    public static JCheckBox FrontRightCheck;
    public static JCheckBox BackLeftCheck;
    public static JCheckBox BackRightCheck;
    public static JCheckBox UsedCheck;
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
        setFocusable(true);
        setTitle("Team 467 Wheel Speed Calibration Utility");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        //height,width
        setLayout(new GridLayout(0, 2));

        //adds action listeners to each button and checkbox
        setupActionListeners();

        //wrapper for the graph panel
        graphPanelContainer = new JPanel();
        //graph panel for custom made graph with the customized JPanel
        graphPanel = new GraphDrawingPanel();
        //chart panel for holding the JFreeChart
        chartPanel = createPanel();
        //wrapper for all user interface components (Right Half of Screen)        
        userInterfaceContainer = new JPanel();
        //holds checkboxes and buttons
        controlPanel = new JPanel();
        //holds output text area
        outputPanel = new JPanel();
        //holds all checkboxes
        checkboxPanelContainer = new JPanel();
        //holds wheek checkboxes
        wheelCheckboxPanel = new JPanel();
        //holds checkbox for used values
        usedCheckboxPanel = new JPanel();
        //holds all checkboxes
        onlineCheckboxPanel = new JPanel();
        //holds all buttons
        buttonPanel = new JPanel();
        //title seen at top of UIContainer
        titlePanel = new JPanel();

        setupGraphPanel();
        setupUserInterfaceContainer();
        setupControlPanel();
        setupConsolePanel();
        setupCheckboxPanelContainer();
        setupButtonPanel();
        setupTitlePanel();


        //prints to the output console all values that had been stored during startup of frame in the
        //WheelSpeedCalibrationMap.outputText
        printOutputConsole();

        //"Validates this container and all of its subcomponents."
        //See JavaDoc for more detail, but this is the function that ensures that the buttons will show on startup
        validate();

        if (WheelSpeedCalibrationMap.preferencesNotExistFlag)
        {
            Utilities.showWarningBox("File wpilib-preferences.ini does not exist. \nPlease pull Preferences file from the cRIO.");
        }

    }

    /**
     * Sets up functionality for each action listener that is added to
     * components.
     */
    private void setupActionListeners()
    {
        /**
         * Listener applied to each checkbox that will update graph of lines and
         * data set
         */
        redrawGraph = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                refreshDataset();
                refreshPlotLines();
                repaint();

            }
        };



        /**
         * Listens to connect to robot button to set pullFromRobot
         */
        connectToRobotClick = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (FTPUtilities.transmitPreferences(ServerOperationEnum.NO_ACTION) && !WheelSpeedCalibrationMap.pullFromRobot)
                {
                    Utilities.appendOutputWindow("\nConnected to Robot!");
                    printOutputConsole();
                    WheelSpeedCalibrationMap.pullFromRobot = true;
                    connectToRobot.setText("Connected!");
                    pullButton.setText("Refresh Graph from Robot");
                    sendButton.setEnabled(true);
                }
                else
                {
                    Utilities.appendOutputWindow("\nNot connected to Robot");
                    printOutputConsole();
                    WheelSpeedCalibrationMap.pullFromRobot = false;
                    connectToRobot.setText("Connect");
                    pullButton.setText("Refresh Graph from Local File");
                    sendButton.setEnabled(false);
                }
                connectToRobot.setForeground(onlineLightColor());
            }
        };

        /**
         * Listener for button on ButtonPanel that writes and pushes files to
         * the robot
         */
        sendValues = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                WriteToFile.addToFile();

                if (WheelSpeedCalibrationMap.pullFromRobot)
                {
                    boolean successSend = FTPUtilities.transmitPreferences(ServerOperationEnum.PUSH);
                    if (successSend)
                    {
                        Utilities.appendOutputWindow("");
                        Utilities.appendOutputWindow("File Sent to Robot!");
                        printOutputConsole();
                    }
                    else
                    {
                        Utilities.showErrorBox("Sending of File Failed!");
                    }
                }
                else
                {
                    Utilities.appendOutputWindow("");
                    Utilities.appendOutputWindow("File not sent as the program is in offline mode!");
                }
                printOutputConsole();
            }
        };

        /**
         * Listener for Pull button on ButtonPanel that pulls and reads file
         * from the robot
         */
        pullFile = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (WheelSpeedCalibrationMap.pullFromRobot)
                {
                    FTPUtilities.transmitPreferences(ServerOperationEnum.PULL);
                }
                WheelSpeedCalibration.updateGraph();
                result.removeAllSeries();
                plot.clearAnnotations();
                FrontLeftSeries.clear();
                FrontRightSeries.clear();
                BackLeftSeries.clear();
                BackRightSeries.clear();
                createDataset();
                refreshDataset();
                refreshPlotLines();
                repaint();
                Utilities.appendOutputWindow("");
                Utilities.appendOutputWindow("Graph Updated!");
                printOutputConsole();
            }
        };

    }

    /**
     * Sets up all of the components on the right half of the screen that
     * contains the interactive pieces.
     */
    private void setupUserInterfaceContainer()
    {
        add(userInterfaceContainer);
        userInterfaceContainer.setLayout(new BoxLayout(userInterfaceContainer, WIDTH));
        userInterfaceContainer.add(controlPanel);
        userInterfaceContainer.add(outputPanel);
        marginBorder(userInterfaceContainer, 10, 10, 5, 10);
    }

    /**
     * Sets up layout for the graph and its parents, including layout.
     */
    private void setupGraphPanel()
    {
        graphPanelContainer.setLayout(new GridLayout());
        add(graphPanelContainer);

        graphPanelContainer.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        //graphPanelContainer.add(graphPanel);
        graphPanelContainer.add(chartPanel);
        marginBorder(graphPanelContainer, 10, 10, 10, 5);
    }

    /**
     * Sets up outputConsole with JTextArea and a scroll bar for the panel
     */
    private void setupConsolePanel()
    {
        outputConsole = new JTextArea();
        outputConsole.setEditable(false);
        outputConsoleScrollBar = new JScrollPane(outputConsole);
        outputConsoleScrollBar.getViewport().add(outputConsole);

        outputPanel.setLayout(new GridLayout());
        outputPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        outputPanel.add(outputConsoleScrollBar);
    }

    /**
     * Sets up panel on right half of screen holding all manipulation components
     */
    private void setupControlPanel()
    {
        controlPanel.setLayout(new BoxLayout(controlPanel, WIDTH));
        controlPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        controlPanel.add(titlePanel);
        controlPanel.add(checkboxPanelContainer);
        controlPanel.add(buttonPanel);
    }

    /**
     * Sets up buttons to pull, read, and push file to and from the robot
     */
    private void setupButtonPanel()
    {
        buttonPanel.setLayout(new GridLayout(0, 1));
        buttonPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));


        sendButton = new JButton("Write Values and Send to Robot");
        sendButton.setPreferredSize(new Dimension(0, 70));
        sendButton.addActionListener(sendValues);
        sendButton.setEnabled(false);

        pullButton = new JButton("Refresh Graph from Local File ");
        pullButton.setPreferredSize(new Dimension(0, 70));
        pullButton.addActionListener(pullFile);

        buttonPanel.add(sendButton);
        buttonPanel.add(pullButton);
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Update Buttons"));
        sendButton.setToolTipText("This button writes the values to the preferences file and sends it to the robot");
        pullButton.setToolTipText("This button pulls the preferences file from the robot and recomputes the values on the graph");

    }

    /**
     * Sets up and creates title panel on controlPanel of Frame
     */
    private void setupTitlePanel()
    {
        Font triforce = loadFont("Fonts/Triforce.ttf");
        triforce = triforce.deriveFont(Font.BOLD);
        Label titleLabel = new Label("Team 467 Wheel Speed Calibrator");
        titlePanel.setLayout(new GridLayout(0, 1));
        titlePanel.setBorder(BorderFactory.createEtchedBorder());
        marginBorder(titlePanel, 4, 4, 4, 4);
        titleLabel.setFont(triforce);
//        titleLabel.setFont(new Font("Comic Sans", Font.BOLD, 32));
        titlePanel.add(titleLabel);
    }

    /**
     * Loads custom .ttf file and returns a Font to be used by other functions
     *
     * @param name Filepath to .ttf file to load
     * @return Font loaded from said .ttf file
     */
    public Font loadFont(String name)
    {
        InputStream is = NewFrame.class.getResourceAsStream(name);
        Font font = null;
        try
        {
            font = Font.createFont(Font.TRUETYPE_FONT, is);
            font = font.deriveFont(32f);
            System.out.println("Loaded Font from: " + NewFrame.class.getResource(name).getPath());
        }
        catch (FontFormatException ex)
        {
            Utilities.showErrorBox("Font Format exception: " + ex.getLocalizedMessage());
        }
        catch (IOException ex)
        {
        }
        return font;
    }

    /**
     * Runs setup code for each button on the GUI
     */
    private void setupCheckboxPanelContainer()
    {
        //height, width
        checkboxPanelContainer.setLayout(new GridLayout(1, 0));

        //height,width
        wheelCheckboxPanel.setLayout(new GridLayout(0, 1));

        FrontLeftCheck = new JCheckBox("Front Left");
        FrontLeftCheck.addActionListener(redrawGraph);
        FrontLeftCheck.setSelected(true);
        FrontLeftCheck.setForeground(WheelSpeedCalibrationMap.FRONT_LEFT_COLOR);
        FrontLeftCheck.setToolTipText("This displays the values for the Front Left Wheel");

        FrontRightCheck = new JCheckBox("Front Right");
        FrontRightCheck.addActionListener(redrawGraph);
        FrontRightCheck.setSelected(true);
        FrontRightCheck.setForeground(WheelSpeedCalibrationMap.FRONT_RIGHT_COLOR);
        FrontRightCheck.setToolTipText("This displays the values for the Front Right Wheel");

        BackLeftCheck = new JCheckBox("Back Left");
        BackLeftCheck.addActionListener(redrawGraph);
        BackLeftCheck.setSelected(true);
        BackLeftCheck.setForeground(WheelSpeedCalibrationMap.BACK_LEFT_COLOR);
        BackLeftCheck.setToolTipText("This displays the values for the Back Left Wheel");

        BackRightCheck = new JCheckBox("Back Right");
        BackRightCheck.addActionListener(redrawGraph);
        BackRightCheck.setSelected(true);
        BackRightCheck.setForeground(WheelSpeedCalibrationMap.BACK_RIGHT_COLOR);
        BackRightCheck.setToolTipText("This displays the values for the Back Right Wheel");

        UsedCheck = new JCheckBox("Toggle Used Values");
        
        wheelCheckboxPanel.add(FrontRightCheck);
        wheelCheckboxPanel.add(FrontLeftCheck);
        wheelCheckboxPanel.add(BackRightCheck);
        wheelCheckboxPanel.add(BackLeftCheck);
        wheelCheckboxPanel.setBorder(BorderFactory.createTitledBorder("Wheels"));

        onlineCheckboxPanel.setBorder(BorderFactory.createTitledBorder("Connection to Robot"));
        onlineCheckboxPanel.setLayout(new GridLayout(0, 1));

        //.onlineLight.setFont(new Font("Arial", Font.PLAIN, 32));        

        connectToRobot = new JButton("Connect");
        connectToRobot.addActionListener(connectToRobotClick);
        connectToRobot.setForeground(onlineLightColor());

        //onlineCheckboxPanel.add(onlineLight);
        onlineCheckboxPanel.add(connectToRobot);

        checkboxPanelContainer.add(wheelCheckboxPanel);
        checkboxPanelContainer.add(usedCheckboxPanel);
        checkboxPanelContainer.add(onlineCheckboxPanel);
    }

    /**
     * Returns the correct color for the online light.
     *
     * @return Color for indicator to be set to.
     */
    private Color onlineLightColor()
    {
        if (WheelSpeedCalibrationMap.pullFromRobot)
        {
            return Color.GREEN;
        }
        else
        {
            return Color.RED;
        }
    }

    private void printOutputConsole()
    {
        outputConsole.setText(WheelSpeedCalibrationMap.outputText);
    }

    /**
     * Draws custom graph, should be called ONLY by paint function in modified
     * JPanel GraphDrawingPanel.
     *
     * @param g      Graphics to draw on JPanel, passed by JPanel's paint
     *               function
     * @param wheels ArrayList of Wheel objects to draw each line with
     */
    private void draw(Graphics g, ArrayList<Wheel> wheels)
    {
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
                        //set color to blue if unused, else it sets to proper color
                        g.setColor((!p.used) ? WheelSpeedCalibrationMap.UNUSED_COLOR : WheelSpeedCalibrationMap.FRONT_RIGHT_COLOR);
                        //draws line if chechbox is null, else, polls the checkbox
                        drawLine = (FrontRightCheck != null) ? drawLine = FrontRightCheck.isSelected() : true;
                        break;

                    case WheelSpeedCalibrationMap.FRONT_LEFT:
                        //set color to blue if unused, else it sets to proper color
                        g.setColor((!p.used) ? WheelSpeedCalibrationMap.UNUSED_COLOR : WheelSpeedCalibrationMap.FRONT_LEFT_COLOR);
                        //draws line if chechbox is null, else, polls the checkbox
                        drawLine = (FrontLeftCheck != null) ? drawLine = FrontLeftCheck.isSelected() : true;
                        break;
                    case WheelSpeedCalibrationMap.BACK_RIGHT:
                        g.setColor((!p.used) ? WheelSpeedCalibrationMap.UNUSED_COLOR : WheelSpeedCalibrationMap.BACK_RIGHT_COLOR);
                        drawLine = (BackRightCheck != null) ? drawLine = BackRightCheck.isSelected() : true;
                        break;
                    case WheelSpeedCalibrationMap.BACK_LEFT:
                        g.setColor((!p.used) ? WheelSpeedCalibrationMap.UNUSED_COLOR : WheelSpeedCalibrationMap.BACK_LEFT_COLOR);
                        drawLine = (FrontRightCheck != null) ? drawLine = BackLeftCheck.isSelected() : true;
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

    /**
     * Creates a panel to place in the frame layout. Should be called once in
     * constructor, never in code body.
     *
     * @return ChartPanel with all values and lines showing unless non-null
     *         checkboxes are set otherwise.
     */
    private ChartPanel createPanel()
    {
        result = new XYSeriesCollection();
        FrontLeftSeries = new XYSeries("Front Left");
        FrontRightSeries = new XYSeries("Front Right");
        BackLeftSeries = new XYSeries("Back Left");
        BackRightSeries = new XYSeries("Back Right");
        chart = ChartFactory.createScatterPlot(
                "Wheel Calibration Values", // chart title
                "Speed Values", // x axis label
                "Power Values", // y axis label
                result, //data
                PlotOrientation.VERTICAL,
                true, // include legend
                true, // tooltips
                false // urls
                );
        plot = chart.getXYPlot();
        //sets up data to draw
        createDataset();
        plot.setBackgroundPaint(WheelSpeedCalibrationMap.BACKGROUND_COLOR);

        //chart.getXYPlot().getRangeAxis().setRange(lowestLow*0.95, highestHigh*1.05);

        //draw Axis on the graph
        drawAxis();

        //called to create the plot lines for each wheel
        refreshPlotLines();


        Rectangle shape = new Rectangle(2, 2);
        plot.getRenderer().setSeriesPaint(result.indexOf(FrontRightSeries), WheelSpeedCalibrationMap.FRONT_RIGHT_COLOR);
        plot.getRenderer().setSeriesShape(result.indexOf(FrontRightSeries), shape);
        plot.getRenderer().setSeriesPaint(result.indexOf(FrontLeftSeries), WheelSpeedCalibrationMap.FRONT_LEFT_COLOR);
        plot.getRenderer().setSeriesShape(result.indexOf(FrontLeftSeries), shape);
        plot.getRenderer().setSeriesPaint(result.indexOf(BackRightSeries), WheelSpeedCalibrationMap.BACK_RIGHT_COLOR);
        plot.getRenderer().setSeriesShape(result.indexOf(BackRightSeries), shape);
        plot.getRenderer().setSeriesPaint(result.indexOf(BackLeftSeries), WheelSpeedCalibrationMap.BACK_LEFT_COLOR);
        plot.getRenderer().setSeriesShape(result.indexOf(BackLeftSeries), shape);

        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseZoomable(true);
        panel.setDisplayToolTips(true);
        return panel;
    }

    /**
     * Draws axis on the graph
     */
    private void drawAxis()
    {
        Marker m = new ValueMarker(0.0);
        m.setStroke(new BasicStroke(1));
        m.setPaint(Color.BLACK);
        plot.addRangeMarker(m);
        plot.addDomainMarker(m);
    }

    /**
     * Creates and refreshes the drawn lines on the graph corresponding to each
     * wheel
     */
    private void refreshPlotLines()
    {
        XYLineAnnotation posLineAnnotation = null;
        XYLineAnnotation negLineAnnotation = null;

        for (Wheel w : WheelSpeedCalibration.wheels)
        {
            if ("FrontLeft".equals(w.name))
            {
                drawLine = (FrontLeftCheck != null) ? drawLine = FrontLeftCheck.isSelected() : true;
                drawAnnotations(w, plot, posLineAnnotation, negLineAnnotation, WheelSpeedCalibrationMap.FRONT_LEFT_COLOR);
            }
            if ("FrontRight".equals(w.name))
            {
                drawLine = (FrontRightCheck != null) ? drawLine = FrontRightCheck.isSelected() : true;
                drawAnnotations(w, plot, posLineAnnotation, negLineAnnotation, WheelSpeedCalibrationMap.FRONT_RIGHT_COLOR);
            }
            if ("BackLeft".equals(w.name))
            {
                drawLine = (BackLeftCheck != null) ? drawLine = BackLeftCheck.isSelected() : true;
                drawAnnotations(w, plot, posLineAnnotation, negLineAnnotation, WheelSpeedCalibrationMap.BACK_LEFT_COLOR);
            }
            if ("BackRight".equals(w.name))
            {
                drawLine = (BackRightCheck != null) ? drawLine = BackRightCheck.isSelected() : true;
                drawAnnotations(w, plot, posLineAnnotation, negLineAnnotation, WheelSpeedCalibrationMap.BACK_RIGHT_COLOR);
            }
        }
    }

    /**
     * Draws lines for each wheel data set.
     *
     * @param w                 Wheel to draw lines.
     * @param plot              plot to add annotations.
     * @param posLineAnnotation annotation for forward data
     * @param negLineAnnotation annotation for backward data
     */
    private void drawAnnotations(Wheel w, XYPlot plot, XYLineAnnotation posLineAnnotation, XYLineAnnotation negLineAnnotation, Paint paint)
    {
        BasicStroke stroke = new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL);
        posLineAnnotation = new XYLineAnnotation(
                w.posPoints.point1.x,
                w.posPoints.point1.y,
                w.posPoints.point2.x,
                w.posPoints.point2.y,
                stroke, //stroke
                paint //paint
                );
        negLineAnnotation = new XYLineAnnotation(
                w.negPoints.point1.x,
                w.negPoints.point1.y,
                w.negPoints.point2.x,
                w.negPoints.point2.y,
                stroke, //stroke
                paint //paint
                );
        plot.removeAnnotation(negLineAnnotation);
        plot.removeAnnotation(posLineAnnotation);
        if (drawLine)
        {
            plot.addAnnotation(posLineAnnotation);
            plot.addAnnotation(negLineAnnotation);
        }
    }

    /**
     * Should only be called to initialize values, never hiding/showing lines.
     */
    private void createDataset()
    {
        for (Wheel w : WheelSpeedCalibration.wheels)
        {
            if ("FrontLeft".equals(w.name))
            {
                for (GraphPoint p : w.points)
                {
                    FrontLeftSeries.add(p.speed, p.power);
                }
                result.addSeries(FrontLeftSeries);
            }
            else if ("FrontRight".equals(w.name))
            {
                for (GraphPoint p : w.points)
                {
                    FrontRightSeries.add(p.speed, p.power);
                }
                result.addSeries(FrontRightSeries);
            }
            else if ("BackLeft".equals(w.name))
            {
                for (GraphPoint p : w.points)
                {
                    BackLeftSeries.add(p.speed, p.power);
                }
                result.addSeries(BackLeftSeries);
            }
            else if ("BackRight".equals(w.name))
            {
                for (GraphPoint p : w.points)
                {
                    BackRightSeries.add(p.speed, p.power);
                }
                result.addSeries(BackRightSeries);
            }
        }
    }

    /**
     * Refreshes dataset for graph. Can be called to update the data drawn on
     * the graph. Polls each checkbox for its value. Will work even if the
     * checkbox is null, as it will default to displaying the dataset
     *
     */
    private void refreshDataset()
    {
        for (Wheel w : WheelSpeedCalibration.wheels)
        {
            if ("FrontLeft".equals(w.name))
            {
                drawLine = (FrontLeftCheck != null) ? drawLine = FrontLeftCheck.isSelected() : true;
                plot.getRenderer().setSeriesVisible(result.indexOf(FrontLeftSeries), drawLine);
            }
            else if ("FrontRight".equals(w.name))
            {
                drawLine = (FrontRightCheck != null) ? drawLine = FrontRightCheck.isSelected() : true;
                plot.getRenderer().setSeriesVisible(result.indexOf(FrontRightSeries), drawLine);
            }
            else if ("BackLeft".equals(w.name))
            {
                drawLine = (BackLeftCheck != null) ? drawLine = BackLeftCheck.isSelected() : true;
                plot.getRenderer().setSeriesVisible(result.indexOf(BackLeftSeries), drawLine);
            }
            else if ("BackRight".equals(w.name))
            {
                drawLine = (BackRightCheck != null) ? drawLine = BackRightCheck.isSelected() : true;
                plot.getRenderer().setSeriesVisible(result.indexOf(BackRightSeries), drawLine);
            }
        }
    }

    /**
     * Scale up to half the x, from -1.0 to 1.0 up to -256.0 to 256.0, then
     * shift the x val over to center
     *
     * @param x value to scale for graph
     * @return scaled x value
     */
    private double scaleX(double x)
    {
        return (x * (graphPanel.getBounds().width / WheelSpeedCalibrationMap.SCREEN_X_RANGE) + (graphPanel.getBounds().width / 2));
    }

    /**
     * Takes half the size of the screen and subtract from that to center the Y,
     * and scale the size from ~(-8.0 to 8.0)or so based on Y
     *
     * @param y value to scale for graph
     * @return scaled y value
     */
    private double scaleY(double y)
    {
        return ((graphPanel.getBounds().height / 2) - (y * (graphPanel.getBounds().height / 2)));


    }

    /**
     * Apply margin to specified component.
     *
     * @param component
     * @param top
     * @param bottom
     * @param left
     * @param right
     */
    private void marginBorder(JComponent component, int top, int bottom, int left, int right)
    {
        Border current = component.getBorder();
        Border empty = new EmptyBorder(top, left, bottom, right);
        if (current == null)
        {
            component.setBorder(empty);
        }
        else
        {
            component.setBorder(new CompoundBorder(empty, current));
        }
    }

    /**
     * Creates custom panel that has the draw function overridden so that it
     * will draw custom made graph.
     */
    private class GraphDrawingPanel extends JPanel
    {

        @Override
        public void paint(Graphics g)
        {
            draw(g, WheelSpeedCalibration.wheels);
        }
    }
}
