package edu.wpi.first.smartdashboard.gui.elements;

import edu.wpi.first.smartdashboard.properties.ColorProperty;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.robot.Robot;
import edu.wpi.first.smartdashboard.types.DataType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * Implements a simple text box UI element with a name label.
 * @author pmalmsten
 * @author Joe Grinstead
 */
public class TextBox extends EditableDisplayElement implements ActionListener {

    public static final DataType[] TYPES = {DataType.BASIC};
    public static final String NAME = "Text Box";

    public final ColorProperty background = new ColorProperty(this, "Background");

    private JTextField valueField;
    private String oldText;

    public void init() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        JLabel nameLabel = new JLabel(getFieldName());
        valueField = new JTextField();

        update(background, valueField.getBackground());

        valueField.setEditable(editable.getValue());
        valueField.setColumns(10);

        valueField.addActionListener(this);

        add(nameLabel);
        add(valueField);
        revalidate();
        repaint();
    }

    @Override
    public void setValue(Object value) {
        valueField.setText(oldText = value.toString());
        revalidate();
        repaint();
    }

    @Override
    public void propertyChanged(Property property) {
        if (property == background) {
            valueField.setBackground(background.getValue());
        } else if (property == editable) {
            valueField.setEditable(editable.getValue());
        }
    }

    public void actionPerformed(ActionEvent event) {
        String newText = valueField.getText();
        if (!newText.equals(oldText)) {
            if (getType().isChildOf(DataType.BOOLEAN)) {
                if (newText.equalsIgnoreCase("true")) {
                    setValue("true");
                    Robot.getTable().putBoolean(getFieldName(), true);
                } else if (newText.equalsIgnoreCase("false")) {
                    setValue("false");
                    Robot.getTable().putBoolean(getFieldName(), false);
                } else {
                    setValue(oldText);
                }
            } else if (getType().isChildOf(DataType.DOUBLE)) {
                try {
                    double value = Double.parseDouble(newText);
                    setValue(value);
                    Robot.getTable().putDouble(getFieldName(), value);
                } catch (NumberFormatException e) {
                    setValue(oldText);
                }
            } else if (getType().isChildOf(DataType.INTEGER)) {
                try {
                    int value = Integer.parseInt(newText);
                    setValue(value);
                    Robot.getTable().putInt(getFieldName(), value);
                } catch (NumberFormatException e) {
                    setValue(oldText);
                }
            } else if (getType().isChildOf(DataType.STRING)) {
                setValue(newText);
                Robot.getTable().putString(getFieldName(), newText);
            }
        }

        // Take the caret off the textbox
        requestFocus();
    }
}
