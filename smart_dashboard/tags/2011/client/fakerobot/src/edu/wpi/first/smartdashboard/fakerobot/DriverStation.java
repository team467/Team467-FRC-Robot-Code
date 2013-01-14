package edu.wpi.first.smartdashboard.fakerobot;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pmalmsten
 */
public class DriverStation {
    private static DriverStation instance;
    private Object m_monitor = new Object();
    private int m_updateNumber;
    private IDashboard m_dash;

    public static DriverStation getInstance() {
        if(instance == null)
            instance = new DriverStation();
        return instance;
    }

    public void setDashboardPackerToUseHigh(IDashboard d) {
        m_dash = d;
    }

    public Object getStatusDataMonitor() {
        return m_monitor;
    }

    public void incrementUpdateNumber() {
        m_updateNumber++;
    }

    public void writePacket(OutputStream os) {
        if(m_dash == null)
            return;
        
        try {
            os.write(new byte[26]);
            os.write(m_updateNumber);

            byte[] data = m_dash.getBytes();

            os.write((data.length & 0xFF000000) >> 24);
            os.write((data.length & 0x00FF0000) >> 16);
            os.write((data.length & 0x0000FF00) >> 8);
            os.write(data.length & 0x000000FF);
            os.write(data);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        m_dash.flush();
    }
}
