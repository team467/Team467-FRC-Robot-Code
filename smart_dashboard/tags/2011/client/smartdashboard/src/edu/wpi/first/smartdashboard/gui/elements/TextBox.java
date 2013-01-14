package edu.wpi.first.smartdashboard.gui.elements;

import edu.wpi.first.smartdashboard.util.StatefulDisplayElement;
import edu.wpi.first.smartdashboard.state.Record;
import edu.wpi.first.smartdashboard.types.Types;
import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * Implements a simple text box UI element with a name label.
 * @author pmalmsten
 */
public class TextBox extends StatefulDisplayElement {
    private JTextField valueField;

    public void init() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        JLabel nameLabel = new JLabel(m_name);
        valueField = new JTextField();
        valueField.setEditable(false);
        valueField.setColumns(10);

        add(nameLabel);
        add(valueField);
        revalidate();
        repaint();

	setProperty(widthProperty, valueField.getColumns());
	setProperty(backgroundProperty, valueField.getBackground());
    }

    public void update(final Record r) {
        final TextBox myself = this;
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                Object realValue = r.getValue();
                valueField.setText(realValue.toString());
                myself.revalidate();
                myself.repaint();
            }
        });
    }

    public static Types.Type[] getSupportedTypes() {
        return Types.Type.values();
    }

    @Override
    public boolean propertyChange(String key, Object value) {
	if (key == widthProperty) valueField.setColumns(Integer.parseInt((String)value));
	else if (key == backgroundProperty) valueField.setBackground((Color) value);
	return true;
    }

    @Override
    public Object getPropertyValue(String key) {
	if (key == widthProperty) return valueField.getColumns();
	else if (key == backgroundProperty) return valueField.getBackground();
	else return null;
    }

    private final String widthProperty = "Width";
    private final String backgroundProperty = "Background color";
}
