/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelspeedcalibration;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author Kyle
 */
public class FrameClass extends JFrame
{
    public FrameClass()
    {
        setSize(new Dimension(WheelSpeedCalibration.SCREEN_SIZE_X, WheelSpeedCalibration.SCREEN_SIZE_Y));
        setVisible(true);
        setResizable(false);
        createBufferStrategy(2);
        setIconImage(null);
        setTitle("Team 467 Wheel Speed Calibration Utility");
        setFocusable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);        
    }
    
    public void run(ArrayList<Wheel> Wheels)
    {
        BufferStrategy buffer = this.getBufferStrategy();
        while (true)
        {
            draw(buffer.getDrawGraphics(), Wheels);
            buffer.show();
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(FrameClass.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private static void draw(Graphics g, ArrayList<Wheel> wheels)
    {        
        g.setColor(Color.white);
        g.fillRect(0, 0, WheelSpeedCalibration.SCREEN_SIZE_X, WheelSpeedCalibration.SCREEN_SIZE_Y);        
        for (Wheel w: wheels)
        {
            for (GraphPoint p: w.points)
            {                
                if (!p.used)
                {                  
                    g.setColor(Color.blue);
                }                
                else
                {
                    switch (wheels.indexOf(w))
                    {
                        case 0:
                            g.setColor(Color.RED);
                            break;
                        case 1:
                            g.setColor(Color.BLACK);
                            break;
                        case 2:
                            g.setColor(Color.GREEN);
                            break;
                        case 3:
                            g.setColor(Color.MAGENTA);
                            break;
                    }                                        
                }
                //System.err.println(p.power * 256);
                g.fillRect((int)(p.power * (WheelSpeedCalibration.SCREEN_SIZE_X / 2) + (WheelSpeedCalibration.SCREEN_SIZE_X / 2)), (WheelSpeedCalibration.SCREEN_SIZE_Y / 2) - (int)(p.speed * 32), 2, 2);
                //g.fillRect(p.index * 2, (WheelSpeedCalibration.SCREEN_SIZE_Y / 2) - (int)(p.speed * 32), 2, 2);
            }
        }
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
        throw new UnsupportedOperationException("Not supported yet.");
    }
}