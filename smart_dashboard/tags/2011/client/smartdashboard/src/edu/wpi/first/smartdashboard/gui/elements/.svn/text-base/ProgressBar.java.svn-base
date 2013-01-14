/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.smartdashboard.gui.elements;

import edu.wpi.first.smartdashboard.state.Record;
import edu.wpi.first.smartdashboard.types.Types;
import edu.wpi.first.smartdashboard.util.StatefulDisplayElement;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

/**
 *
 * @author brad (Heavily modified by Alex Henning)
 */
public class ProgressBar extends StatefulDisplayElement {

    private JProgressBar progressBar;
    private double min, max, scale;

    @Override
    public void init() {
        min = 0; max = 100; scale = 1;
        progressBar = new JProgressBar();
//        progressBar.setOrientation(SwingConstants.VERTICAL);
        progressBar.setMaximum((int) (min * scale));
        progressBar.setMaximum((int) (max * scale));
        progressBar.setBorderPainted(false);
        progressBar.setBounds(progressBar.getX(), progressBar.getY(),
                progressBar.getX() + 200, progressBar.getY() + 40);

        setLayout(new BorderLayout());
        add(new JLabel(m_name), BorderLayout.PAGE_START);
        add(progressBar, BorderLayout.CENTER);
        revalidate();
        repaint();

        setProperty(foregroundProperty, progressBar.getForeground());
        setProperty(backgroundProperty, progressBar.getBackground());
        setProperty(maximumProperty, progressBar.getMaximum());
        setProperty(minimumProperty, progressBar.getMinimum());
    	setProperty(widthProperty, getWidth());
        setProperty(heightProperty, getHeight());
        setProperty(orientationProperty, "horizontal");
    }

    private void calcScale() {
        scale = (getHeight() + getWidth()) / (max - min);
        progressBar.setMinimum((int) (min * scale));
        progressBar.setMaximum((int) (max * scale));
    }

    @Override
    public boolean propertyChange(String key, Object value) {
        if (key == foregroundProperty) {
            progressBar.setForeground((Color) value);
        } else if (key == backgroundProperty) {
            progressBar.setBackground((Color) value);
        } else if (key == maximumProperty) {
            max = Double.parseDouble((String) value);
            calcScale();
        } else if (key == minimumProperty) {
            min = Double.parseDouble((String) value);
            calcScale();
        } else if (key == widthProperty) {
            setSize(new Dimension(Integer.parseInt((String) value),
                    getHeight()));
        } else if (key == heightProperty) {
            setSize(getWidth(),
                    Integer.parseInt((String) value));
        } else if (key == orientationProperty) {
            if (value == "vertical") {
                progressBar.setOrientation(SwingConstants.VERTICAL);
            } else if (value == "horizontal") {
                progressBar.setOrientation(SwingConstants.HORIZONTAL);
            }
        }
        return true;
    }

    public static Types.Type[] getSupportedTypes() {
        return new Types.Type[]{
                    Types.Type.BYTE,
                    Types.Type.INT,
                    Types.Type.SHORT,
                    Types.Type.CHAR,
                    Types.Type.FLOAT,
                    Types.Type.DOUBLE,
                    Types.Type.LONG
                };
    }

    @Override
    public Object getPropertyValue(String key) {
        if (key == foregroundProperty) {
            return progressBar.getForeground();
        }
        if (key == backgroundProperty) {
            return progressBar.getBackground();
        }
        if (key == maximumProperty) {
            return max;
        }
        if (key == minimumProperty) {
            return min;
        } else if (key == widthProperty) {
            return getWidth();
        } else if (key == heightProperty) {
            return getHeight();
        } else if (key == orientationProperty) {
            if (SwingConstants.VERTICAL == progressBar.getOrientation()) {
                return "vertical";
            } else {
                return "horizontal";
            }
        } else {
            return 0;
        }
    }

    public void update(final Record r) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                Number value = (Number) (((Number) r.getValue()).doubleValue() * scale);
                progressBar.setValue(value.intValue());
                ProgressBar.this.revalidate();
                ProgressBar.this.repaint();
            }
        });
    }
    
    private final String foregroundProperty = "Foreground",
                         backgroundProperty = "Background",
	                     maximumProperty = "Maximum",
	                     minimumProperty = "Minimum",
	                     widthProperty = "Width",
	                     heightProperty = "Height",
                         orientationProperty = "Orientation";
}
