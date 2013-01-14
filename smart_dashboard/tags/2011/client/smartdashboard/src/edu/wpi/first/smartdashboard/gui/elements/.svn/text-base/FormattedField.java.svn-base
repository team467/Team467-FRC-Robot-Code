package edu.wpi.first.smartdashboard.gui.elements;

import edu.wpi.first.smartdashboard.util.StatefulDisplayElement;
import edu.wpi.first.smartdashboard.state.Record;
import edu.wpi.first.smartdashboard.types.Types;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;

/**
 * Implements a simple text box UI element with a name label.
 * @author pmalmsten
 */
public class FormattedField extends StatefulDisplayElement {
    protected JFormattedTextField valueField;

    public void init() {
        setLayout(new BorderLayout());

        JLabel nameLabel = new JLabel(m_name);
        valueField = new JFormattedTextField();
        valueField.setEditable(false);
        valueField.setColumns(10);

        add(nameLabel, BorderLayout.LINE_START);
        add(valueField, BorderLayout.CENTER);
        revalidate();
        repaint();

        setProperty(foregroundProperty, valueField.getForeground());
        setProperty(backgroundProperty, valueField.getBackground());
        setProperty(fontSizeProperty, valueField.getFont().getSize());
        setProperty(widthProperty, getWidth());
        setProperty(heightProperty, getHeight());
    }

    public void update(final Record r) {
        final FormattedField myself = this;
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                Object realValue = r.getValue();
		valueField.setValue(realValue);
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
        if (key == foregroundProperty) {
            valueField.setForeground((Color) value);
        } else if (key == backgroundProperty) {
            valueField.setBackground((Color) value);
        } else if (key == fontSizeProperty) {
            valueField.setFont(new Font(valueField.getFont().getFontName(),
                    valueField.getFont().getStyle(),
                    (int) Integer.parseInt((String) value)));
        } else if (key == widthProperty) {
            setSize(Integer.parseInt((String) value),
                    getHeight());
        } else if (key == heightProperty) {
            setSize(getWidth(),
                    Integer.parseInt((String) value));
        }
        return true;
    }

    @Override
    public Object getPropertyValue(String key) {
        if (key == foregroundProperty) {
            return valueField.getForeground();
        } else if (key == backgroundProperty) {
            return valueField.getBackground();
        } else if (key == fontSizeProperty) {
            return valueField.getFont().getSize();
        } else if (key == widthProperty) {
            return getWidth();
        } else if (key == heightProperty) {
            return getHeight();
        } else return null;
    }

    protected final String widthProperty = "Width",
                           foregroundProperty = "Foreground",
                           backgroundProperty = "Background color",
                           fontSizeProperty = "Font size",
                           heightProperty = "Height";
}
