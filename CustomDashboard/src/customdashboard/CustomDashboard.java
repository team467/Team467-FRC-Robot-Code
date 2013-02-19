/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package customdashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author aidan
 */
public class CustomDashboard extends JFrame
{   
    //Dashboard swing elements
    private JButton exitButton;
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
    
    private JPanel mainPanel;
    
    //Listeners
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
    private ActionListener exitButtonListener = new ActionListener() 
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            System.exit(0);
        }
    };
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        setNimbusLook();
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
        //setUndecorated(true);
        int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 240;
        setSize(width, height);
        setVisible(true);
        addWindowListener(windowListener);
        
        mainPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));

        //Make dashboard elements
        exitButton = new JButton("Exit");    
        //makeWheelAngleGraph();
        
        //Initialize element properties
        imageCanvas.setPreferredSize(new Dimension(640, 480));
        
        //Add listeners
        exitButton.addActionListener(exitButtonListener);

        //Add dashboard elements
        add(mainPanel);
        mainPanel.add(imageCanvas);
        mainPanel.add(exitButton);
        
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
        
        repaint();
        
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
    
    /**
     * Sets the nimbus look and feel for the window
     */
    private static void setNimbusLook()
    {
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(CustomDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(CustomDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(CustomDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(CustomDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
}
