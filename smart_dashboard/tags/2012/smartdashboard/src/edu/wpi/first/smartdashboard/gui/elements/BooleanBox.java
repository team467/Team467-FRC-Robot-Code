package edu.wpi.first.smartdashboard.gui.elements;

import edu.wpi.first.smartdashboard.gui.Widget;
import edu.wpi.first.smartdashboard.properties.ColorProperty;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.types.DataType;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Implements a simple text box UI element with a name label.
 * @author pmalmsten
 */
public class BooleanBox extends Widget {

    public static final DataType[] TYPES = {DataType.BOOLEAN};
    public final ColorProperty colorOnTrue = new ColorProperty(this, "Color to show when true", Color.GREEN);
    public final ColorProperty colorOnFalse = new ColorProperty(this, "Color to show when false", Color.RED);
    private JPanel valueField;
    private boolean value;

    public void init() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        JLabel nameLabel = new JLabel(getFieldName());
        valueField = new JPanel();
        valueField.setPreferredSize(new Dimension(10, 10));

        add(valueField);
        add(nameLabel);
        revalidate();
        repaint();
    }

    @Override
    public void setValue(final Object value) {
        this.value = (Boolean) value;
        valueField.setBackground(this.value ? colorOnTrue.getValue() : colorOnFalse.getValue());
        repaint();
    }

    @Override
    public void propertyChanged(Property property) {
        if (property == colorOnTrue && value) {
            valueField.setBackground(colorOnTrue.getValue());
        } else if (property == colorOnFalse && !value) {
            valueField.setBackground(colorOnFalse.getValue());
        }
    }
}
