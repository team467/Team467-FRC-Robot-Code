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
        setSize(new Dimension(WheelSpeedCalibration.SCREEN_SIZE_X + 400, WheelSpeedCalibration.SCREEN_SIZE_Y+ 400));
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
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WheelSpeedCalibration.SCREEN_SIZE_X, WheelSpeedCalibration.SCREEN_SIZE_Y);
        g.setColor(Color.BLACK);
        //draw horizontal line
        g.drawLine(0, (WheelSpeedCalibration.SCREEN_SIZE_Y / 2), WheelSpeedCalibration.SCREEN_SIZE_X, (WheelSpeedCalibration.SCREEN_SIZE_Y / 2));
        //draw vertical line
        g.drawLine((WheelSpeedCalibration.SCREEN_SIZE_X / 2), 0, (WheelSpeedCalibration.SCREEN_SIZE_X / 2), WheelSpeedCalibration.SCREEN_SIZE_Y);

        g.drawString("X: Speed btw ~ -8.0 and 8.0 RPS", 10, 40);
        g.drawString("Y: Power btw -1.0 and 1.0 Pwr", 10, 55);
        for (Wheel w : wheels)
        {
            for (GraphPoint p : w.points)
            {
                if (!p.used)
                {
                    g.setColor(Color.BLUE);
                }
                else
                {
                    switch (wheels.indexOf(w))
                    {
                        case WheelSpeedCalibrationMap.FRONT_RIGHT:
                            g.setColor(Color.RED);
                            break;
                        case WheelSpeedCalibrationMap.FRONT_LEFT:
                            g.setColor(Color.ORANGE);
                            break;
                        case WheelSpeedCalibrationMap.BACK_RIGHT:
                            g.setColor(Color.GREEN);
                            break;
                        case WheelSpeedCalibrationMap.BACK_LEFT:
                            g.setColor(Color.MAGENTA);
                            break;
                    }
                }


                //X: Scale up to half the x, from -1.0 to 1.0 up to -256.0 to 256.0,
                //   then shift the x val over to center
                //Y: take half the size of the screen and subtract from that to 
                //   center the Y, and scale the size from ~(-8.0 to 8.0)or so based on Y

                //System.err.println(p.power * 256);

//                g.fillRect((int) (p.speed * WheelSpeedCalibration.SIZE_X_SCALING + (WheelSpeedCalibration.SCREEN_SIZE_X / 2)),
//                        (WheelSpeedCalibration.SCREEN_SIZE_Y / 2) - (int) (p.power * (WheelSpeedCalibration.SCREEN_SIZE_Y / 2)),
//                        WheelSpeedCalibration.GRID_SQUARE_SIZE,
//                        WheelSpeedCalibration.GRID_SQUARE_SIZE);
                g.fillRect((int) (scaleX(p.speed)),
                        (int) scaleY(p.power),
                        WheelSpeedCalibration.GRID_SQUARE_SIZE,
                        WheelSpeedCalibration.GRID_SQUARE_SIZE);
            }
            //System.out.println("p1x: " + w.posPoints.point1.x + " p1y: " + w.posPoints.point1.y + " p2x: " + w.posPoints.point2.x + " p2y: " + w.posPoints.point2.y);
            g.drawLine((int) (scaleX(w.posPoints.point1.x)),
                    (int) (scaleY(w.posPoints.point1.y)),
                    (int) (scaleX(w.posPoints.point2.x)),
                    (int) (scaleY(w.posPoints.point2.y)));
            g.drawLine((int) scaleX(0), (int) scaleY(0), (int) scaleX(-6), (int) scaleY(-0.5));
            g.drawLine((int) scaleX(0), (int) scaleY(0), (int) scaleX(-6), (int) scaleY(-0.25));
        }
    }

    private static double scaleX(double x)
    {
        return (x * WheelSpeedCalibration.SIZE_X_SCALING + (WheelSpeedCalibration.SCREEN_SIZE_X / 2));
    }

    private static double scaleY(double y)
    {
        return ((WheelSpeedCalibration.SCREEN_SIZE_Y / 2) - (y * (WheelSpeedCalibration.SCREEN_SIZE_Y / 2)));
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