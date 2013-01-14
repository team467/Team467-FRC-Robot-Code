package edu.wpi.first.smartdashboard.video;

import edu.wpi.first.smartdashboard.gui.DashboardPrefs;
import edu.wpi.first.smartdashboard.gui.MainWindow;
import edu.wpi.first.smartdashboard.gui.elements.VideoBox;
import edu.wpi.first.smartdashboard.net.TCPImageFetcher;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

/**
 *
 * @author pmalmsten
 */
public class VideoController {
    public static PreferenceChangeListener videoPreferencesChangeListener = new PreferenceChangeListener() {
       public void preferenceChange(PreferenceChangeEvent evt) {
            if(evt.getKey() == DashboardPrefs.SHOWCAMERAVIDEO_KEY) {
                if(DashboardPrefs.getInstance().getShowCameraVideo()) {
                    showVideo();
                    startReceivingVideo(DashboardPrefs.getInstance().getCameraVideoTeamNumber());
                } else {
                    hideVideo();
                }
           }
        }
    };

    private static VideoBox s_videoBox;

    public static void showVideo() {
        if(s_videoBox == null) {
            s_videoBox = new VideoBox();
            s_videoBox.connect();
            MainWindow.getInstance().addField(s_videoBox);
        }
    }

    public static void hideVideo() {
        if(s_videoBox != null) {
            s_videoBox.disconnect();

            if(s_videoBox.getLayoutAllocation() != null) {
                s_videoBox.getLayoutAllocation().deallocate();
                s_videoBox.getParent().remove(s_videoBox);
            }
            s_videoBox = null;
            MainWindow.getInstance().redrawDisplayElements();
        }
    }

    public static void startReceivingVideo(int teamNumber) {
        if(teamNumber > 0 && ImageDispatch.getInstance().getState() == Thread.State.NEW) {
            ImageDispatch.getInstance().setFetcher(new TCPImageFetcher(teamNumber));
            ImageDispatch.getInstance().start();
        }
    }
}
