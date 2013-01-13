package edu.wpi.first.wpijavacv;

import com.googlecode.javacv.FFmpegFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import static com.googlecode.javacv.cpp.opencv_core.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class used to gather images from the robot's camera.
 * @author Joe Grinstead and Greg Granito
 */
public class WPICamera extends WPIDisposable {

    private static final int DEFAULT_ENDING_IP = 20;
    private FFmpegFrameGrabber grabber;
    private IplImage image;
    private boolean readImage = true;
    private boolean badConnection = false;
    private final Object imageLock = new Object();
    private final Object grabberLock = new Object();

    public WPICamera(String loginName, String password, int team) {
        this(loginName + ":" + password + "@10." + (team / 100) + "." + (team % 100) + "." + DEFAULT_ENDING_IP);
    }

    public WPICamera(int team) {
        this("10." + (team / 100) + "." + (team % 100) + "." + DEFAULT_ENDING_IP);
    }

    public WPICamera(String loginName, String password, String ip) {
        this(loginName + ":" + password + "@" + ip);
    }

    public WPICamera(String ip) {
        final String path = "http://" + ip + "/mjpg/video.mjpg";

        new Thread() {

            @Override
            public void run() {
                grabber = new FFmpegFrameGrabber(path);
                try {
                    grabber.start();

                    while (!isDisposed()) {
                        try {
                            IplImage newest;
                            synchronized (grabberLock) {
                                if (isDisposed()) {
                                    return;
                                }
                                newest = grabber.grab();
                            }
                            if (isNull(newest)) {
                                synchronized (imageLock) {
                                    badConnection = true;
                                    imageLock.notify();
                                }
                                return;
                            } else {
                                synchronized (imageLock) {
                                    if (image == null) {
                                        image = cvCreateImage(newest.cvSize(), newest.depth(), newest.nChannels());
                                    }
                                    cvCopy(newest, image);
                                    readImage = false;
                                    badConnection = false;
                                    imageLock.notify();
                                }
                            }
                        } catch (Exception ex) {
                            synchronized (imageLock) {
                                badConnection = true;
                                imageLock.notify();
                            }
                            ex.printStackTrace();
                            return;
                        }
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException ex) {
                        }
                    }
                } catch (Exception ex) {
                    synchronized (imageLock) {
                        badConnection = true;
                        imageLock.notify();
                    }
                    ex.printStackTrace();
                }
            }
        }.start();
    }

    public WPIImage getImage() throws BadConnectionException {
        validateDisposed();

        synchronized (imageLock) {
            if (badConnection) {
                throw new BadConnectionException();
            } else if (image == null) {
                return null;
            } else if (image.nChannels() == 1) {
                return new WPIGrayscaleImage(image.clone());
            } else {
                assert image.nChannels() == 3;
                return new WPIColorImage(image.clone());
            }
        }
    }

    public WPIImage getNewImage(double timeout) throws BadConnectionException {
        validateDisposed();

        synchronized (imageLock) {
            readImage = true;
            while (readImage && !badConnection) {
                try {
                    badConnection = true;
                    imageLock.wait((long) (timeout * 1000));
                } catch (InterruptedException ex) {
                }
            }
            readImage = true;

            if (badConnection) {
                throw new BadConnectionException();
            } else if (image.nChannels() == 1) {
                return new WPIGrayscaleImage(image.clone());
            } else {
                assert image.nChannels() == 3;
                return new WPIColorImage(image.clone());
            }
        }
    }

    public WPIImage getNewImage() throws BadConnectionException {
        return getNewImage(0);
    }

    @Override
    protected void disposed() {
        try {
            synchronized (imageLock) {
                if (!isNull(image)) {
                    image.release();
                }
            }
        } catch (Exception ex) {
        }
    }

    /**
     * An exception that occurs when the camera can not be reached.
     * @author Greg Granito
     */
    public static class BadConnectionException extends Exception {
    }

    @Override
    protected void finalize() throws Throwable {
        grabber.stop();
        super.finalize();
    }
}
