package edu.wpi.first.smartdashboard.gui.elements;

import edu.wpi.first.smartdashboard.types.Types;
import edu.wpi.first.smartdashboard.util.DisplayElement;
import edu.wpi.first.smartdashboard.video.IImageReceiver;
import edu.wpi.first.smartdashboard.video.ImageDispatch;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JLabel;

/**
 * Implements a panel which shows images received from a robot.
 * 
 * @author pmalmsten
 */
public class VideoBox extends DisplayElement implements IImageReceiver {
    transient Image m_img = null;
    JLabel statusLabel = new JLabel("Waiting for video...");
    Dimension m_oldSize = null;
    boolean waitingForVideo = true;

    public VideoBox() {
        setPreferredSize(new Dimension(320, 240));
    }
    
    public void init() {
        setLayout(new BorderLayout());
        setBackground(Color.black);
        add(statusLabel, BorderLayout.NORTH);
        statusLabel.setForeground(Color.white);
        revalidate();
        repaint();
    }

    public void connect()
    {
        ImageDispatch.getInstance().registerForImages(this);
    }
    
    @Override
    public void disconnect() {
        ImageDispatch.getInstance().unregisterForImages(this);
    }

    public static Types.Type[] getSupportedTypes() {
        Types.Type[] nothing = new Types.Type[0];
        return nothing;
    }

    public void update(Image img) {
        m_img = img;
        final Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                if(waitingForVideo) {
                    remove(statusLabel);
                    waitingForVideo = false;
                }

                if(m_oldSize == null || !size.equals(m_oldSize)) {
                    setPreferredSize(size);
                    setMaximumSize(size);
                    setMaximumSize(size);
                    setSize(size);
                    m_oldSize = size;
                }
                
                revalidate();
                repaint();
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        if(m_img != null)
            g.drawImage(m_img, 0, 0, null);
        else
            super.paintComponent(g);
    }

    @Override
    public boolean propertyChange(String key, Object value) {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getPropertyValue(String key) {
	throw new UnsupportedOperationException("Not supported yet.");
    }
}
