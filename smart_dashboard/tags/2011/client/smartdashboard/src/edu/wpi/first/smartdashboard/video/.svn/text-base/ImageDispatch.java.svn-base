package edu.wpi.first.smartdashboard.video;

import edu.wpi.first.smartdashboard.gui.DashboardPrefs;
import edu.wpi.first.smartdashboard.gui.MainWindow;
import edu.wpi.first.smartdashboard.net.TCPImageFetcher;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Fetches images from a robot and informs interested objects of their arrival.
 *
 * @author pmalmsten
 */
public class ImageDispatch extends Thread {
    boolean m_run = true;
    TCPImageFetcher m_fetcher = null;
    List<IImageReceiver> m_receivers = Collections.synchronizedList(new ArrayList<IImageReceiver>());
    static ImageDispatch instance = null;

    private ImageDispatch() {
    }

    public void setFetcher(TCPImageFetcher fetcher) {
        if(m_fetcher == null)
            m_fetcher = fetcher;
    }

    public static ImageDispatch getInstance() {
        if(instance == null || instance.getState() == Thread.State.TERMINATED) {
            instance = new ImageDispatch();
        }

        return instance;
    }

    @Override
    public synchronized void start() {
        if(m_fetcher == null)
            throw new IllegalStateException("An image fetcher must be provided "
                    + "by calling setFetcher before attempting to start the "
                    + "ImageDispatch thread");
        
        super.start();
    }

    @Override
    public void run() {
        while(m_run) {
            try {
                Image img = m_fetcher.fetch();

                if(img != null) {
                    for(IImageReceiver r : m_receivers) {
                        r.update(img);
                    }
                }

                img = null;
                yield();
            } catch (Exception ex) {
                ex.printStackTrace();
                if(DashboardPrefs.getInstance().getDebugVideoErrors()) {
                        JOptionPane.showMessageDialog(MainWindow.getInstance(),
                                                      "Unable to connect to the robot to receive video. "
                                                      + "Check your team number in the preferences dialog"
                                                      + " and disable/reenable video to try again. The exception "
                                                      + "was:\n\n" + ex,
                                                      "Network Problem",
                                                      JOptionPane.ERROR_MESSAGE);
                        stopThread();
                        VideoController.hideVideo();
                        DashboardPrefs.getInstance().setShowCameraVideo(false);
                }
            }
        }
    }

    public void stopThread() {
        m_run = false;
    }

    public void registerForImages(IImageReceiver i) {
        m_receivers.add(i);
    }

    public void unregisterForImages(IImageReceiver i) {
        m_receivers.remove(i);
    }
}
