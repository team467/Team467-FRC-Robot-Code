/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package customdashboard;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author aidan
 */
public class CustomDashboard extends JFrame
{   
    //Graphs
    private TuningGraph angleGraph;
    
    //Image objects
    private TCPImageFetcher imageFetcher;
    private BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
    private JPanel imageCanvas = new JPanel()
    {
        public void paint(Graphics g)
        {
            g.drawImage(image, 0, 0, null);
        }
    };
    
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
        
        //Make image fetcher
        imageFetcher = new TCPImageFetcher(467);
        
        //Initialize JFrame settings
        int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 230;
        setSize(width, height);
        setVisible(true);
        addWindowListener(windowListener);

        //Make a new graph and add it to the frame
        //makeWheelAngleGraph();

        add(imageCanvas);
        
        //Repaint the frame
        repaint();
        
        //fixes not display problem
        validate();
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
            try
            {
                image = imageFetcher.fetch();
                imageCanvas.repaint();
            }
            catch (IOException ex)
            {
                Logger.getLogger(CustomDashboard.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //Sleep a little
            try
            {
                Thread.sleep(10);
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
        angleGraph = new TuningGraph("PID Tuning", "Time", "Motor Power", 0.0, 10.0, -1.0, 1.0);
        angleGraph.addSeries("set steering angle");
        angleGraph.addSeries("actual steering angle");
        add(angleGraph, BorderLayout.CENTER);
    }
    
    /**
     * Graphs the actual wheel angle against the set wheel angle
     */
    private void graphWheelAngles()
    {
        //Read value from network tables
        double actualAngle = Tables.wheelAngleTable.getNumber("actual angle", 0.0);
        double sentAngle = Tables.wheelAngleTable.getNumber("set angle", 0.0);
        
        //Graph the values and redraw the graph
        angleGraph.addValue("actual steering angle", time, actualAngle);
        angleGraph.addValue("set steering angle", time, sentAngle);
        angleGraph.repaint();
        
        //Increment the time
        time += 0.01;
    }
}
