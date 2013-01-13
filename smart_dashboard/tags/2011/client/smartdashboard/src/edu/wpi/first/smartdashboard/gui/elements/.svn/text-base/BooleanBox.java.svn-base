package edu.wpi.first.smartdashboard.gui.elements;

import edu.wpi.first.smartdashboard.util.StatefulDisplayElement;
import edu.wpi.first.smartdashboard.state.Record;
import edu.wpi.first.smartdashboard.types.Types;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Implements a simple text box UI element with a name label.
 * @author pmalmsten
 */
public class BooleanBox extends StatefulDisplayElement {
    private JPanel valueField;
    private Color colorOnTrue = Color.GREEN;
    private Color colorOnFalse = Color.RED;

    public void init() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        JLabel nameLabel = new JLabel(m_name);
        valueField = new JPanel();
        valueField.setPreferredSize(new Dimension(10,10));

        add(nameLabel);
        add(valueField);
        revalidate();
        repaint();
        
	setProperty(statusBackgroundColorTrue, colorOnTrue);
        setProperty(statusBackgroundColorFalse, colorOnFalse);
    }

    public void update(final Record r) {
        final BooleanBox myself = this;
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                boolean realValue = (Boolean) r.getValue();
                if(realValue)
                    valueField.setBackground(colorOnTrue);
                else
                    valueField.setBackground(colorOnFalse);
                
                myself.revalidate();
                myself.repaint();
            }
        });
    }

    public static Types.Type[] getSupportedTypes() {
        return new Types.Type[] {Types.Type.BOOLEAN};
    }

    @Override
    public boolean propertyChange(String key, Object value) {
	if (key == statusBackgroundColorTrue) {
            if(valueField.getBackground().equals(colorOnTrue))
                valueField.setBackground((Color) value);
            colorOnTrue = (Color) value;
        }
	else if (key == statusBackgroundColorFalse) {
            if(valueField.getBackground().equals(colorOnFalse))
                valueField.setBackground((Color) value);
            colorOnFalse = (Color) value;
        }
	return true;
    }

    @Override
    public Object getPropertyValue(String key) {
	if (key == statusBackgroundColorTrue) return colorOnTrue;
	else if (key == statusBackgroundColorFalse) return colorOnFalse;
	return null;
    }

    private final String statusBackgroundColorTrue = "Color to show when true";
    private final String statusBackgroundColorFalse = "Color to show when false";
}
