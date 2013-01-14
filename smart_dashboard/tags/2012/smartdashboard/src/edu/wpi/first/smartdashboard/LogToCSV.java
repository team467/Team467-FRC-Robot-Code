package edu.wpi.first.smartdashboard;

import edu.wpi.first.smartdashboard.gui.DashboardPrefs;
import edu.wpi.first.smartdashboard.robot.Robot;
import edu.wpi.first.wpilibj.networking.NetworkAdditionListener;
import edu.wpi.first.wpilibj.networking.NetworkListener;
import edu.wpi.first.wpilibj.networking.NetworkTable;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 * Logs all information received to a CSV file.
 * 
 * @author pmalmsten
 */
public class LogToCSV implements NetworkListener, NetworkAdditionListener {

    private static final String s_lineSeparator = System.getProperty("line.separator");
    private long m_startTime;
    private FileWriter m_fw;
    private static LogToCSV instance;

    public static LogToCSV getInstance() {
        return instance == null ? instance = new LogToCSV() : instance;
    }

    /*
     * Prepares this LogToCSV object to begin writing to the specified file. The
     * specified file is opened in write mode (any existing content is blown away).
     * @param path The path of the CSV file to write to.
     */
    public void start(String path) {
        if (m_fw == null) {
            try {
                m_startTime = System.currentTimeMillis();
                m_fw = new FileWriter(path);
                m_fw.write("Time (ms),Name,Value" + s_lineSeparator);
                m_fw.flush();
                Robot.getTable().addListenerToAll(this);
                Robot.getTable().addAdditionListener(this, true);
                Robot.getTable().removeAdditionListener(this);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "An error occurred when attempting to "
                        + "open the output CSV file for writing. "
                        + "Please check the file path preference.",
                        "Unable to Open CSV File",
                        JOptionPane.ERROR_MESSAGE);
                DashboardPrefs.getInstance().logToCSV.setValue(false);
            }
        }
    }

    /*
     * If logging was previously enabled, this method flushes and releases
     * the file handle to the CSV file. Logging will no longer occur.
     */
    public void stop() {
        if (m_fw == null) {
            return;
        }

        try {
            m_fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Robot.getTable().removeListenerFromAll(this);
        m_fw = null;
    }

    public void valueChanged(String key, Object value) {
        if (!(value instanceof NetworkTable) && m_fw != null) {
            try {
                long timeStamp = System.currentTimeMillis() - m_startTime;
                m_fw.write(timeStamp + ","
                        + "\"" + key + "\","
                        + "\"" + value + "\"" + s_lineSeparator);
                m_fw.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void valueConfirmed(String key, Object value) {
        valueChanged(key, value);
    }

    public void fieldAdded(String name, Object value) {
        valueChanged(name, value);
    }
}
