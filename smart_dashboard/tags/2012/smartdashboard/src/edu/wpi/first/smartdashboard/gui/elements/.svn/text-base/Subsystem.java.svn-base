package edu.wpi.first.smartdashboard.gui.elements;

import edu.wpi.first.smartdashboard.gui.Widget;
import edu.wpi.first.smartdashboard.properties.ColorProperty;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.types.DataType;
import edu.wpi.first.smartdashboard.types.named.SubsystemType;
import edu.wpi.first.wpilibj.networking.NetworkListener;
import edu.wpi.first.wpilibj.networking.NetworkTable;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * @author Joe Grinstead
 */
public class Subsystem extends Widget implements NetworkListener {

    public static final DataType[] TYPES = {SubsystemType.get()};

    public final ColorProperty background = new ColorProperty(this, "Background");

    private JTextField valueField;
    private NetworkTable table;

    public void init() {
        JLabel nameLabel = new JLabel(getFieldName());
        valueField = new JTextField("---");

        update(background, valueField.getBackground());

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        valueField.setEditable(false);
        valueField.setColumns(10);

        add(nameLabel);
        add(valueField);
        revalidate();
        repaint();
    }

    @Override
    public void setValue(Object value) {
        if (table != null) {
            table.removeListener("command", this);
        }
        table = (NetworkTable) value;
        table.addListener("command", this);
        table.addListener("hasCommand", this);

        valueChanged("hasCommand", table.getBoolean("hasCommand"));

        repaint();
    }

    public void valueChanged(String key, Object value) {
        if (key.equals("hasCommand")) {
            if ((Boolean) value) {
                valueField.setText(table.getSubTable("command").getString("name"));
            } else {
                valueField.setText("---");
            }
        } else if (key.equals("command")) {
            valueField.setText(((NetworkTable) value).getString("name"));
        }
    }

    public void valueConfirmed(String key, Object value) {
    }

    @Override
    public void propertyChanged(Property property) {
        if (property == background) {
            valueField.setBackground(background.getValue());
        }
    }
}
