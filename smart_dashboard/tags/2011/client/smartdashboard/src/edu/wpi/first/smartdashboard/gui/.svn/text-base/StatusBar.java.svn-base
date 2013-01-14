package edu.wpi.first.smartdashboard.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

/**
 * Represents a status bar which shows the current state of the dashboard.
 * @author pmalmsten
 */
public class StatusBar extends JPanel implements Observer {
    /**
     * Represents a transition which should be applied to a status bar when
     * a state change occurs.
     */
    public static enum StateTransition {
        NO_DATA("No data received; check your network connection.", Color.RED),
        GOOD_DATA("", Color.GREEN),
        BAD_DATA("Out of sync, please wait a moment...", Color.YELLOW);

        private String m_message;
        private Color m_color;

        /**
         * Creates a state transition with an appropriate string message and
         * color.
         * @param m The message to show.
         * @param c The color to show.
         */
        private StateTransition(String m, Color c) {
            m_message = m;
            m_color = c;
        }

        /**
         * Puts the given status bar into the proper state.
         * @param sb The status bar to update.
         */
        public void apply(StatusBar sb) {
            sb.setStatusText(m_message);
            sb.setStatusColor(m_color);
        }
    }

    private JTextField m_status;
    private JPanel m_color;
    private StateTransition m_lastState;

    /**
     * Creates and initializes a status bar. This must be called from within
     * an appropriate GUI thread!
     */
    public StatusBar() {
        setBorder(new BevelBorder(BevelBorder.LOWERED));

        m_status = new JTextField();
        m_status.setEditable(false);
        m_status.setText("No data.");
        m_status.setBorder(BorderFactory.createEmptyBorder());

        m_color = new JPanel();
        m_color.setPreferredSize(new Dimension(20, 10));
        m_color.setBackground(Color.red);

        setLayout(new BorderLayout());
        add(m_status, BorderLayout.WEST);
        add(m_color, BorderLayout.EAST);

        StateTransition.NO_DATA.apply(this);
    }

    /**
     * Sets the text message displayed by this status bar.
     * @param message
     */
    protected void setStatusText(final String message) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                m_status.setText(message);
            }
        });
    }

    /**
     * Sets the color displayed by this status bar's color area.
     * @param c
     */
    protected void setStatusColor(final Color c) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                m_color.setBackground(c);
            }
        });
    }

    public void update(Observable o, Object arg) {
        if(arg instanceof StateTransition) {
            if((StateTransition) arg != m_lastState){
                ((StateTransition) arg).apply(this);
                m_lastState = (StateTransition) arg;
            }
        }
    }
}
