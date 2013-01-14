
package edu.wpi.first.smartdashboard.gui.elements;

import edu.wpi.first.smartdashboard.state.Record;
import edu.wpi.first.smartdashboard.types.Types;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

/**
 * Implements a field that displays the time. The color of the text and
 * background after a certain amount of time. This field is designed to convert
 * an int representing seconds into a time in minutes and seconds and display it
 * properly.
 * i.e. `90' is displayed as `1:30'
 *
 * @author Alex Henning
 */
public class TimeField extends FormattedField {
    Color normForeground, normBackground,
	alertForeground, alertBackground;
    int alertTime = 15;

    @Override
    public void init() {
	super.init();
	propertyChange(foregroundProperty, Color.BLACK);
	propertyChange(backgroundProperty, Color.GREEN);
	propertyChange(heightProperty, "40");
	setPreferredSize(new Dimension(150, getHeight()));
	propertyChange(fontSizeProperty, "36");

	setProperty(alertTimeProperty, alertTime);

	alertBackground = Color.RED;
	setProperty(alertForegroundProperty, alertForeground);
	alertForeground = Color.BLACK;
	setProperty(alertBackgroundProperty, alertBackground);
    }

    @Override
    public void update(final Record r) {
        final TimeField myself = this;
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                int seconds = ((Number) r.getValue()).intValue();

                if (seconds <= alertTime) {
                    valueField.setForeground(alertForeground);
                    valueField.setBackground(alertBackground);
                } else {
                    valueField.setForeground(normForeground);
                    valueField.setBackground(normBackground);
                }

                int minutes = 0;
                while (seconds >= 60) {
                    minutes += 1;
                    seconds -= 60;
                }

                if (seconds >= 10) {
                    valueField.setValue(minutes + ":" + seconds);
                } else {
                    valueField.setValue(minutes + ":0" + seconds);
                }
                myself.revalidate();
                myself.repaint();
            }
        });
    }

    public static Types.Type[] getSupportedTypes() {
        return new Types.Type[]{ Types.Type.INT };
    }

    @Override
    public boolean propertyChange(String key, Object value) {
        super.propertyChange(key, value);
        if (key == foregroundProperty) {
            normForeground = (Color) value;
        } else if (key == backgroundProperty) {
            normBackground = (Color) value;
        } else if (key == alertTimeProperty) {
            alertTime = Integer.parseInt((String) value);
        } else if (key == alertForegroundProperty) {
            alertForeground = (Color) value;
        } else if (key == alertBackgroundProperty) {
            alertBackground = (Color) value;
        }
        return true;
    }

    @Override
    public Object getPropertyValue(String key) {
        Object ret = super.getPropertyValue(key);
        if (key == foregroundProperty) {
            return normForeground;
        } else if (key == backgroundProperty) {
            return normBackground;
        } else if (ret != null) {
            // Allow FormattedField to handle all of it's properties except for
            // color, because the time field changes color based off of the time.
            return ret;
        } else if (key == alertTimeProperty) {
            return alertTime;
        } else if (key == alertForegroundProperty) {
            return alertForeground;
        } else if (key == alertBackgroundProperty) {
            return alertBackground;
        } else {
            return null;
        }
    }

    protected final String alertTimeProperty = "Alert Time",
                           alertBackgroundProperty = "Alert Background Color",
                           alertForegroundProperty = "Alert Foreground Color";

}
