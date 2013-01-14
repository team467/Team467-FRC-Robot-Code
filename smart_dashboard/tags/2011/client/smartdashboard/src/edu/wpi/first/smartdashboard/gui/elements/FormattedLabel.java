package edu.wpi.first.smartdashboard.gui.elements;

import edu.wpi.first.smartdashboard.util.DisplayElement;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JFormattedTextField;

/**
 * Implements a simple text label that can be customized to display whatever
 * text the user decides.
 * @author Alex Henning
 */
public class FormattedLabel extends DisplayElement {
    protected JFormattedTextField label;

    public void init() {
        setLayout(new BorderLayout());

        label = new JFormattedTextField();
        label.setEditable(false);
        label.setColumns(10);
        label.setValue("Label");

        add(label, BorderLayout.CENTER);
        revalidate();
        repaint();

        setProperty(foregroundProperty, label.getForeground());
        setProperty(backgroundProperty, label.getBackground());
        setProperty(fontSizeProperty, label.getFont().getSize());
        setProperty(widthProperty, getWidth());
        setProperty(heightProperty, getHeight());
        setProperty(textProperty, "Label");
    }

    @Override
    public boolean propertyChange(String key, Object value) {
        if (key == foregroundProperty) {
            label.setForeground((Color) value);
        } else if (key == backgroundProperty) {
            label.setBackground((Color) value);
        } else if (key == fontSizeProperty) {
            label.setFont(new Font(label.getFont().getFontName(),
                    label.getFont().getStyle(),
                    (int) Integer.parseInt((String) value)));
        } else if (key == widthProperty) {
            setSize(new Dimension(Integer.parseInt((String) value),
                    getHeight()));
        } else if (key == heightProperty) {
            setSize(getWidth(),
                    Integer.parseInt((String) value));
        } else if (key == textProperty) {
            label.setValue((String) value);
        }
        return true;
    }

    @Override
    public Object getPropertyValue(String key) {
        if (key == foregroundProperty) {
            return label.getForeground();
        } else if (key == backgroundProperty) {
            return label.getBackground();
        } else if (key == fontSizeProperty) {
            return label.getFont().getSize();
        } else if (key == widthProperty) {
            return getWidth();
        } else if (key == heightProperty) {
            return getHeight();
        } else if (key == textProperty) {
            return label.getValue();
        } else
            return null;
    }

    protected final String widthProperty = "Width",
                           foregroundProperty = "Foreground Color",
	                       backgroundProperty = "Background Color",
	                       fontSizeProperty = "Font size",
	                       heightProperty = "Height",
	                       textProperty = "Label";

}
