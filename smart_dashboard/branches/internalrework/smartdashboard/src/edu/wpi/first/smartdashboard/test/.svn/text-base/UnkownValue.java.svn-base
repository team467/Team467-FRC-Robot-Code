package edu.wpi.first.smartdashboard.test;

import edu.wpi.first.smartdashboard.gui.ValueWidget;
import edu.wpi.first.smartdashboard.gui.ValueWidgetFactory;
import edu.wpi.first.smartdashboard.properties.Property;
import javax.swing.*;


/**
 * Implements a simple text box UI element with a name label.
 * @author pmalmsten
 */
public class UnkownValue extends ValueWidget {
    public static final ValueWidgetFactory FACTORY = new ValueWidgetFactory() {

        public ValueWidget createWidget(String name) {
            return new UnkownValue(name);
        }

        public boolean supports(Class<?> type) {
            return true;
        }

        public String getName() {
            return "Unkown Value Widget";
        }
    };
            
    private JTextField valueField;

    public UnkownValue(String name) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel nameLabel = new JLabel(name);
        valueField = new JTextField(20);
        valueField.setEnabled(false);

        add(valueField);
        add(nameLabel);
    }

    @Override
    public void setValue(Object value) {
        valueField.setText(value==null?"null":value.toString());
    }

    public void propertyChanged(Property property) {
    }
}
