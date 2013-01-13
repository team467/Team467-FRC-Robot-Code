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
    
    private static void draw(Graphics g, ArrayList<Wheel> Wheels)
    {        
        g.setColor(Color.white);
        g.fillRect(0, 0, WheelSpeedCalibration.SCREEN_SIZE_X, WheelSpeedCalibration.SCREEN_SIZE_Y);        
        for (Wheel w: Wheels)
        {
            for (Point p: w.Points)
            {                
                if (p.used)
                {
                    g.setColor(Color.red);
                }                
                else
                {
                    g.setColor(Color.blue);
                }
                //System.err.println(p.power * 256);
                g.fillRect((int)(p.power * (WheelSpeedCalibration.SCREEN_SIZE_X / 2) + (WheelSpeedCalibration.SCREEN_SIZE_X / 2)), (WheelSpeedCalibration.SCREEN_SIZE_Y / 2) - (int)(p.speed * 32), 2, 2);
                //g.fillRect(p.index * 2, (WheelSpeedCalibration.SCREEN_SIZE_Y / 2) - (int)(p.speed * 32), 2, 2);
            }
        }
    }
}
