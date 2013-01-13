package edu.wpi.first.smartdashboard.test;

import edu.wpi.first.smartdashboard.gui.NewDataType;
import edu.wpi.first.smartdashboard.gui.ValueType;
import edu.wpi.first.smartdashboard.gui.ValueWidget;
import edu.wpi.first.smartdashboard.properties.BooleanProperty;
import edu.wpi.first.smartdashboard.properties.Property;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;


/**
 * Implements a simple text box UI element with a name label.
 * @author pmalmsten
 */
public class BooleanBox extends ValueWidget {
    
    public static final ValueType[] TYPES = {ValueType.BOOLEAN};
    
    public final BooleanProperty editable = new BooleanProperty(this, "Editable", false);
    private JCheckBox valueField;

    public BooleanBox(String name) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        JLabel nameLabel = new JLabel(name);
        valueField = new JCheckBox();
        valueField.setEnabled(false);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        add(valueField);
        add(nameLabel);
    }

    @Override
    public void setValue(Object value) {
        if(value instanceof Boolean)
            valueField.setSelected((Boolean)value);
    }
    
    @Override
    public Dimension getMaximumSize(){
        return new Dimension(500, 200);
    }

    public void propertyChanged(Property property) {
        if (property == editable) {
            valueField.setEnabled(editable.getValue());
        }
    }
}
