package edu.wpi.first.smartdashboard;

import edu.wpi.first.smartdashboard.gui.DashboardPrefs;
import edu.wpi.first.smartdashboard.state.Record;
import edu.wpi.first.smartdashboard.util.IStateListener;
import edu.wpi.first.smartdashboard.util.IStateUpdatable;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import javax.swing.JOptionPane;

/**
 * Logs all information received to a CSV file.
 * 
 * @author pmalmsten
 */
public class LogToCSV implements IStateListener, IStateUpdatable, Serializable {
    private static final String s_lineSeparator = System.getProperty("line.separator");

    private long m_startTime;
    private transient FileWriter m_fw;
    private transient PreferenceChangeListener csvPreferenceChangeListener;

    /*
     * Prepares this LogToCSV object to begin writing to the specified file. The
     * specified file is opened in write mode (any existing content is blown away).
     * @param path The path of the CSV file to write to.
     */
    public void start(String path) {
        try {
            m_startTime = System.currentTimeMillis();
            m_fw = new FileWriter(path);
            m_fw.write("Time (ms),Name,Value" + s_lineSeparator);
            m_fw.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                                          "An error occurred when attempting to "
                                          + "open the output CSV file for writing. "
                                          + "Please check the file path preference.",
                                          "Unable to Open CSV File",
                                          JOptionPane.ERROR_MESSAGE);
            DashboardPrefs.getInstance().setLogToCSVEnabled(false);
        }
    }

    /*
     * If logging was previously enabled, this method flushes and releases
     * the file handle to the CSV file. Logging will no longer occur.
     */
    public void stop() {
        if(m_fw == null)
            return;

        try {
            m_fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        m_fw = null;
    }

    /*
     * Implements IStateListener. This LogToCSV object is always returned.
     */
    public IStateUpdatable newField(Record r) {
        return this;
    }

    /*
     * Implements IStateUpdatable. Any data passed to this method is logged to
     * this object's CSV file.
     */
    public void update(Record r) {
        if (m_fw != null) {
            try {
                long timeStamp = System.currentTimeMillis() - m_startTime;
                m_fw.write(timeStamp + ","
                        + "\"" + r.getName() + "\","
                        + "\"" + r.getValue() + "\"" + s_lineSeparator);
                m_fw.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /*
     * Implements part of Java's serialization API
     */
    private void readObject(ObjectInputStream is) throws IOException, ClassNotFoundException {
        is.defaultReadObject();

        main.registerAndInitLogger(this);
    }

    /*
     * Returns a PreferenceChangeListener associated with this object.
     * @return A PreferenceChangeListener which calls this object on relevant
     *  preference changes.
     */
    public PreferenceChangeListener getPreferenceChangeListener() {
        if(csvPreferenceChangeListener == null) {
            csvPreferenceChangeListener = new PreferenceChangeListener() {
                public void preferenceChange(PreferenceChangeEvent evt) {
                    if(evt.getKey() == DashboardPrefs.LOGTOCSV_ENABLE_KEY) {
                        if(DashboardPrefs.getInstance().getLogToCSVEnabled()) {
                            start(DashboardPrefs.getInstance().getLogToCSVFilePath());
                        } else {
                            stop();
                        }
                    }
                }
            };
        }

        return csvPreferenceChangeListener;
    }
}
