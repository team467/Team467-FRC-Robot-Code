/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.smartdashboard.gui.elements;

import edu.wpi.first.smartdashboard.gui.Widget;
import edu.wpi.first.smartdashboard.properties.ColorProperty;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.types.DataType;
import edu.wpi.first.smartdashboard.types.named.CommandType;
import edu.wpi.first.wpilibj.networking.NetworkListener;
import edu.wpi.first.wpilibj.networking.NetworkTable;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Jeff Copeland
 */
public class Command extends Widget {

    public static final DataType[] TYPES = {CommandType.get()};
    private static final String START_CARD = "Start";
    private static final String CANCEL_CARD = "Cancel";
    public final ColorProperty foreground = new ColorProperty(this, "Font Color", Color.WHITE);
    public final ColorProperty startBackground = new ColorProperty(this, "Start Button Background", new Color(32, 234, 32));
    public final ColorProperty cancelBackground = new ColorProperty(this, "Cancel Button Background", new Color(243, 32, 32));
    private JLabel name;
    private JPanel buttonPanel;
    private CardLayout layout;
    private JButton start;
    private JButton cancel;
    private NetworkTable table;
    private NetworkListener listener = new NetworkListener() {

        public void valueChanged(String key, final Object value) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    if ((Boolean) value) {
                        layout.show(buttonPanel, CANCEL_CARD);
                    } else {
                        layout.show(buttonPanel, START_CARD);
                    }
                }
            });
        }

        public void valueConfirmed(String key, Object value) {
            valueChanged(key, value);
        }
    };

    @Override
    public void setValue(Object value) {
        if (table != null) {
            table.removeListener("running", listener);
        }
        table = (NetworkTable) value;
        table.addListener("running", listener);

        listener.valueChanged("running", table.getBoolean("running"));
        revalidate();
        repaint();
    }

    @Override
    public void init() {
        setResizable(false);

        buttonPanel = new JPanel(layout = new CardLayout());
        buttonPanel.setOpaque(false);

        start = new JButton("start");
        start.setOpaque(false);
        start.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                table.putBoolean("running", true);
            }
        });
        start.setForeground(foreground.getValue());
        start.setBackground(startBackground.getValue());

        buttonPanel.add(start, START_CARD);

        cancel = new JButton("cancel");
        cancel.setOpaque(false);
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                table.putBoolean("running", false);
            }
        });
        cancel.setForeground(foreground.getValue());
        cancel.setBackground(cancelBackground.getValue());

        buttonPanel.add(cancel, CANCEL_CARD);

        name = new JLabel(getFieldName());
        add(name);
        add(buttonPanel);
    }

    @Override
    public void propertyChanged(Property property) {
        if (property == foreground) {
            start.setForeground(foreground.getValue());
            cancel.setForeground(foreground.getValue());
        } else if (property == startBackground) {
            start.setBackground(startBackground.getValue());
        } else if (property == cancelBackground) {
            cancel.setBackground(cancelBackground.getValue());
        }
    }
}
