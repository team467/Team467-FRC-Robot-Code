/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scouting2013;

import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * This is an extension of the panel class which serves to allow the drawing of
 * a faded watermark image in the panel background.
 */
public class WatermarkPanel extends JPanel
{
    //Watermark image

    private Image image;

    /**
     * Creates a new watermark panel
     */
    public WatermarkPanel(String fileName)
    {
        super();

        //Get the image file
        try
        {
            image = ImageIO.read(MainFrame.class.getResourceAsStream(fileName));
        }
        catch (IOException ex)
        {
            Logger.getLogger(MatchForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Paints the regular panel and then paints the watermark image into the
     * background
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        //Lower alpha value (for watermark)
        ((Graphics2D) g).setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 0.2f));

        //Draw image
        g.drawImage(image, 300, 0,null);

        //Return the alpha value to full
        ((Graphics2D) g).setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 1.0f));
    }
}
