package edu.wpi.first.smartdashboard.gui.elements;

import edu.wpi.first.smartdashboard.gui.Widget;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.types.DataType;
import edu.wpi.first.smartdashboard.types.named.ButtonType;
import edu.wpi.first.smartdashboard.types.named.CommandType;
import edu.wpi.first.wpilibj.networking.NetworkTable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

/**
 *
 * @author Joe Grinstead
 */
public class Button extends Widget {

    public static final DataType[] TYPES = {CommandType.get(), ButtonType.get()};
    private NetworkTable table;

    @Override
    public void init() {
        JButton start = new JButton(getFieldName());

        start.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (getType() == CommandType.get()) {
                    table.putBoolean("running", true);
                } else { // Button
                    table.putBoolean("pressed", true);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (getType() == CommandType.get()) {
//                    table.putBoolean("running", false);
                } else { // Button
                    table.putBoolean("pressed", false);
                }
            }
        });

        start.setFocusable(false);

        setLayout(new BorderLayout());

        add(start, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    @Override
    public void setValue(Object table) {
        this.table = (NetworkTable) table;
    }

    @Override
    public void propertyChanged(Property property) {
    }
}
