/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelspeedcalibration;

import java.awt.BasicStroke;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.DateTitle;
import org.jfree.data.xy.XYDataset;
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
    ActionListener redrawGraph;
    ActionListener sendValues;
    ActionListener pullFile;
    ActionListener checkboxUpdated;
    public ChartPanel chartPanel;
    public JPanel graphPanel;
    public JPanel graphPanelContainter;
    public JPanel controlPanel;
    public JPanel checkboxPanel;
    public JPanel buttonPanel;
    public JPanel titlePanel;
    public JPanel outputPanel;
    public JPanel userInterfaceContainter;
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
        setTitle("Team 467 Wheel Speed Calibration Utility");
        setFocusable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        //height,width
        setLayout(new GridLayout(0, 2));

        setupActionListeners();

        graphPanelContainter = new JPanel();
        graphPanelContainter.setLayout(new GridLayout());
        add(graphPanelContainter);

        graphPanel = new GraphDrawingPanel();
        chartPanel = createPanel();
        graphPanelContainter.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        //graphPanelContainter.add(graphPanel);
        graphPanelContainter.add(chartPanel);
        marginBorder(graphPanelContainter, 10, 10, 10, 5);


        userInterfaceContainter = new JPanel();
        userInterfaceContainter.setLayout(new BoxLayout(userInterfaceContainter, WIDTH));
//        userInterfaceContainter.setLayout(new GridLayout(0, 1));

        controlPanel = new JPanel();
        outputPanel = new JPanel();
        checkboxPanel = new JPanel();
        buttonPanel = new JPanel();
        titlePanel = new JPanel();

        setupControlPanel();
        setupCheckboxPanel();
        setupOutputPanel();
        setupButtonPanel();
        setupTitlePanel();

        add(userInterfaceContainter);
        userInterfaceContainter.add(controlPanel);
        userInterfaceContainter.add(outputPanel);
        marginBorder(userInterfaceContainter, 10, 10, 5, 10);

        //"Validates this container and all of its subcomponents."
        //See JavaDoc for more detail, but this is the function that ensures that the buttons will show on startup
        validate();
        printOutputConsole();
    }

    private void setupActionListeners()
    {
        redrawGraph = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Check Clicked");
                
                chartPanel.repaint();
                repaint();

            }
        };

        sendValues = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                WriteToFile.addToFile();

                if (WheelSpeedCalibrationMap.PULL_FROM_ROBOT)
                {
                    FTPUtilities.transmitPreferences(ServerOperationEnum.PUSH);
                    Utilities.appendOutputWindow("");
                    Utilities.appendOutputWindow("File Sent to Robot!");
                    printOutputConsole();
                }
                Utilities.appendOutputWindow("");
                Utilities.appendOutputWindow("File not sent as the program is in offline mode!");
                printOutputConsole();
                System.out.println("File sent to robot!");
            }
        };

        pullFile = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (WheelSpeedCalibrationMap.PULL_FROM_ROBOT)
                {
                    FTPUtilities.transmitPreferences(ServerOperationEnum.PULL);
                }
                WheelSpeedCalibration.updateGraph();
                Utilities.appendOutputWindow("");
                Utilities.appendOutputWindow("Graph Updated!");
                printOutputConsole();
            }
        };
    }

    private void setupOutputPanel()
    {
        outputConsole = new JTextArea();
        outputConsole.setEditable(false);
        outputConsoleScrollBar = new JScrollPane(outputConsole);
        outputConsoleScrollBar.getViewport().add(outputConsole);

        outputPanel.setLayout(new GridLayout());
        outputPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        outputPanel.add(outputConsoleScrollBar);
    }

    private void setupControlPanel()
    {
        controlPanel.setLayout(new BoxLayout(controlPanel, WIDTH));
//        controlPanel.setLayout(new GridLayout(0, 1));
        controlPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        controlPanel.add(titlePanel);
        controlPanel.add(checkboxPanel);
        controlPanel.add(buttonPanel);
    }

    private void setupButtonPanel()
    {
        buttonPanel.setLayout(new GridLayout(0, 1));
        buttonPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));


        sendButton = new JButton("Send Values");
        sendButton.setPreferredSize(new Dimension(0, 70));
        sendButton.addActionListener(sendValues);

        pullButton = new JButton("Refresh Graph ");
        pullButton.setPreferredSize(new Dimension(0, 70));
        pullButton.addActionListener(pullFile);

        buttonPanel.add(sendButton);
        buttonPanel.add(pullButton);
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Update Buttons"));
        sendButton.setToolTipText("This button writes the values to the code and sends to the robot");
        pullButton.setToolTipText("This button redraws the values on the graph");
        
    }

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

    //  Fonts/Triforce.ttf
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
    private void setupCheckboxPanel()
    {
        //height,width
        checkboxPanel.setLayout(new GridLayout(0, 1));
        checkboxPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

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

        checkboxPanel.add(FrontLeftCheck);
        checkboxPanel.add(FrontRightCheck);
        checkboxPanel.add(BackLeftCheck);
        checkboxPanel.add(BackRightCheck);
        checkboxPanel.setBorder(BorderFactory.createTitledBorder("Wheels"));
    }

    public void printOutputConsole()
    {
        //outputConsole.setText("");
        outputConsole.setText(WheelSpeedCalibrationMap.outputText);
    }

    private void draw(Graphics g, ArrayList<Wheel> wheels)
    {
//        System.out.println("WIDTH: " + graphPanel.getBounds().width);
//        System.out.println("HEIGHT: " + graphPanel.getBounds().height);
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

    private ChartPanel createPanel()
    {        
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Wheel Calibration Values", // chart title
                "Speed Values", // x axis label
                "Power Values", // y axis label
                createDataset(), //data
                PlotOrientation.VERTICAL,
                true, // include legend
                true, // tooltips
                false // urls
                );
        XYPlot plot = chart.getXYPlot();

        setupPlotLines(plot);

        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseZoomable(true);
        panel.setDisplayToolTips(true);
        return panel;
    }

    private void setupPlotLines(XYPlot plot)
    {
        Marker m = new ValueMarker(0.0);
        m.setStroke(new BasicStroke(1));
        m.setPaint(Color.BLACK);
        plot.addRangeMarker(m);
        plot.addDomainMarker(m);

        XYLineAnnotation posLineAnnotation = null;
        XYLineAnnotation negLineAnnotation = null;

        for (Wheel w : WheelSpeedCalibration.wheels)
        {
            posLineAnnotation = new XYLineAnnotation(
                    w.posPoints.point1.x,
                    w.posPoints.point1.y,
                    w.posPoints.point2.x,
                    w.posPoints.point2.y);
            negLineAnnotation = new XYLineAnnotation(
                    w.negPoints.point1.x,
                    w.negPoints.point1.y,
                    w.negPoints.point2.x,
                    w.negPoints.point2.y);
            plot.addAnnotation(posLineAnnotation);
            plot.addAnnotation(negLineAnnotation);
        }
    }

    private XYDataset createDataset()
    {
        XYSeriesCollection result = new XYSeriesCollection();
        XYSeries FrontLeftSeries = new XYSeries("Front Left");
        XYSeries FrontRightSeries = new XYSeries("Front Right");
        XYSeries BackLeftSeries = new XYSeries("Back Left");
        XYSeries BackRightSeries = new XYSeries("Back Right");
        for (Wheel w : WheelSpeedCalibration.wheels)
        {
            if (w.name == "FrontLeft")
            {
                drawLine = (FrontLeftCheck != null) ? drawLine = FrontLeftCheck.isSelected() : true;
                if (drawLine)
                {
                    for (GraphPoint p : w.points)
                    {
                        FrontLeftSeries.add(p.speed, p.power);
                    }
                }
                else
                {
                    FrontLeftSeries.clear();
                }
                result.addSeries(FrontLeftSeries);
            }
            else if (w.name == "FrontRight")
            {
                drawLine = (FrontRightCheck != null) ? drawLine = FrontRightCheck.isSelected() : true;
                if (drawLine)
                {
                    for (GraphPoint p : w.points)
                    {
                        FrontRightSeries.add(p.speed, p.power);
                    }                    
                }
                else
                {
                    FrontRightSeries.clear();
                }
                result.addSeries(FrontRightSeries);
            }
            else if (w.name == "BackLeft")
            {
                drawLine = (BackLeftCheck != null) ? drawLine = BackLeftCheck.isSelected() : true;
                if (drawLine)
                {
                    for (GraphPoint p : w.points)
                    {
                        BackLeftSeries.add(p.speed, p.power);
                    }
                }
                else
                {
                    BackLeftSeries.clear();
                }
                result.addSeries(BackLeftSeries);
            }
            else if (w.name == "BackRight")
            {
                drawLine = (BackRightCheck != null) ? drawLine = BackRightCheck.isSelected() : true;
                if (drawLine)
                {
                    for (GraphPoint p : w.points)
                    {
                        BackRightSeries.add(p.speed, p.power);
                    }
                }
                else
                {
                    BackRightSeries.clear();
                }
                result.addSeries(BackRightSeries);
            }
        }
        return result;
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

    /**
     * Apply margin to specified component
     *
     * @param component
     * @param top
     * @param bottom
     * @param left
     * @param right
     */
    public void marginBorder(JComponent component, int top, int bottom, int left, int right)
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

    private class GraphDrawingPanel extends JPanel
    {

        public void paint(Graphics g)
        {
            draw(g, WheelSpeedCalibration.wheels);
        }
    }
}
