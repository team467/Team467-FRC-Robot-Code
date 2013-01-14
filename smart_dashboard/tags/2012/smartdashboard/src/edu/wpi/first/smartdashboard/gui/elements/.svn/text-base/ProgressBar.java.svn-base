/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.smartdashboard.gui.elements;

import edu.wpi.first.smartdashboard.gui.Widget;
import edu.wpi.first.smartdashboard.properties.ColorProperty;
import edu.wpi.first.smartdashboard.properties.IntegerProperty;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.types.DataType;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 *
 * @author brad (Heavily modified by Alex Henning)
 */
public class ProgressBar extends Widget {

    public static final DataType[] TYPES = {DataType.NUMBER};

    public final ColorProperty foreground = new ColorProperty(this, "Foreground");
    public final ColorProperty background = new ColorProperty(this, "Background");
    public final IntegerProperty max = new IntegerProperty(this, "Maximum", 100);
    public final IntegerProperty min = new IntegerProperty(this, "Minimum", 0);

    private JProgressBar progressBar;
    private double scale;

    @Override
    public void init() {
        scale = 1;
        progressBar = new JProgressBar();
        progressBar.setMaximum((int) (min.getValue() * scale));
        progressBar.setMaximum((int) (max.getValue() * scale));
        progressBar.setBorderPainted(false);
        progressBar.setBounds(progressBar.getX(), progressBar.getY(),
                progressBar.getX() + 200, progressBar.getY() + 40);

        setLayout(new BorderLayout());
        add(new JLabel(getFieldName()), BorderLayout.PAGE_START);
        add(progressBar, BorderLayout.CENTER);


        update(foreground, progressBar.getForeground());
        update(background, progressBar.getBackground());
        
        revalidate();
        repaint();
    }

    private void calcScale() {
        scale = (getHeight() + getWidth()) / (max.getValue() - min.getValue());
        progressBar.setMinimum((int) (min.getValue() * scale));
        progressBar.setMaximum((int) (max.getValue() * scale));
    }

    @Override
    public void setValue(Object value) {
        progressBar.setValue((int) (((Number) value).doubleValue() * scale));
        revalidate();
        repaint();
    }

    @Override
    public void propertyChanged(Property property) {
        if (property == foreground) {
            progressBar.setForeground(foreground.getValue());
        } else if (property == background) {
            progressBar.setBackground(background.getValue());
        } else if (property == max || property == min) {
            calcScale();
        }
    }
}
