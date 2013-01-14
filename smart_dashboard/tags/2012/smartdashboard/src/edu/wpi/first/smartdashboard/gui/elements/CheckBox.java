package edu.wpi.first.smartdashboard.gui.elements;

import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.robot.Robot;
import edu.wpi.first.smartdashboard.types.DataType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;

/**
 * Implements a simple text box UI element with a name label.
 * @author pmalmsten
 */
public class CheckBox extends EditableDisplayElement implements ActionListener {

    public static final DataType[] TYPES = {DataType.BOOLEAN};

    private JCheckBox valueField;
    private boolean value;

    public void init() {
        setResizable(false);

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        valueField = new JCheckBox(getFieldName());

        valueField.addActionListener(this);

        add(valueField);
    }

    @Override
    public void setValue(final Object value) {
        this.value = (Boolean) value;
        valueField.setSelected(this.value);
    }

    public void actionPerformed(ActionEvent e) {
        if (value ^ valueField.isSelected()) {
            value = !value;
            Robot.getTable().putBoolean(getFieldName(), value);
        }
    }

    @Override
    public void propertyChanged(Property property) {
        if (property == editable) {
            valueField.setEnabled(editable.getValue());
        }
    }
}
