/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package customdashboard;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author aidan
 */
public class CustomDashboard extends JFrame
{   
    private TuningGraph graph;
    
    //Add frame window listener (to end program when frame is closed)
    private WindowListener windowListener = new WindowListener()
    {
        /**
         * Triggered when window is closed
         */
        public void windowClosing(WindowEvent e)
        {
            System.exit(0);
        }

        //Unused listener methods
        public void windowOpened(WindowEvent e){}
        public void windowClosed(WindowEvent e){}
        public void windowIconified(WindowEvent e){}
        public void windowDeiconified(WindowEvent e){}
        public void windowActivated(WindowEvent e){}
        public void windowDeactivated(WindowEvent e){}
        
    };
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        new CustomDashboard().run();
    }
    
    /**
     * Constructor for new custom dashboard (called once when frame is created)
     */
    public CustomDashboard()
    {
        super("Custom Dashboard");
        
        //Initialize JFrame settings
        setSize(1024, 720);
        setVisible(true);
        addWindowListener(windowListener);
        
        //Make a new graph and add it to the frame
        makeWheelAngleGraph();
        
        //Repaint the frame
        repaint();
    }
    
    /**
     * Runs the custom dashboard
     */
    public void run()
    {   
        //Initialize network tables
        Tables.initialize();
        
        //Main loop
        while (true)
        {
            graphWheelAngles();
            
            //Sleep a little
            try
            {
                Thread.sleep(2);
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(CustomDashboard.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    double time = 0.0;
    
    /**
     * Makes a new graph to graph wheel angles and adds it to the frame
     */
    private void makeWheelAngleGraph()
    {
        graph = new TuningGraph("PID Tuning", "Time", "Motor Power", 0.0, 10.0, -1.0, 1.0);
        graph.addSeries("set steering angle");
        graph.addSeries("actual steering angle");
        add(graph, BorderLayout.CENTER);
    }
    
    /**
     * Graphs the actual wheel angle against the set wheel angle
     */
    private void graphWheelAngles()
    {
            double actualAngle = Tables.wheelAngleTable.getNumber("actual angle", 0.0);
            double sentAngle = Tables.wheelAngleTable.getNumber("set angle", 0.0);
            
            graph.addValue("actual steering angle", time, actualAngle);
            graph.addValue("set steering angle", time, sentAngle);
            graph.repaint();
            
            time += 0.002;
    }
}
