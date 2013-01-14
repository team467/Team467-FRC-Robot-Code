package edu.wpi.first.smartdashboard.gui.elements;

import edu.wpi.first.smartdashboard.state.Record;
import edu.wpi.first.smartdashboard.types.Types;
import edu.wpi.first.smartdashboard.util.StatefulDisplayElement;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CompassPlot;
import org.jfree.data.general.DefaultValueDataset;

/**
 *
 * @author Paul
 */
public class Compass extends StatefulDisplayElement {
    final String revDistanceProperty = "Circumference";
    final String ringColorProperty = "Ring Color";

    JPanel chartPanel;
    DefaultValueDataset data = new DefaultValueDataset(0);
    CompassPlot m_compass;

    @Override
    public void init() {
        setProperty(revDistanceProperty, 360.0);
        setProperty(ringColorProperty, Color.YELLOW);

        m_compass = new CompassPlot(data);
        m_compass.setSeriesNeedle(7);
        m_compass.setSeriesPaint(0, Color.RED);
        m_compass.setSeriesOutlinePaint(0, Color.RED);

        JFreeChart chart = new JFreeChart(m_name, JFreeChart.DEFAULT_TITLE_FONT,
                                            m_compass, false);
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(250, 150));
        
        add(chartPanel);
        revalidate();
        repaint();
    }


    public void updateDisplay(final double value) {
        final JPanel myself = this;
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                data.setValue(value + m_compass.getRevolutionDistance() / 2 );
                myself.revalidate();
                myself.repaint();
            }
        });
    }

    public void update(final Record r) {
        updateDisplay(((Number) r.getValue()).doubleValue());
    }

    public static Types.Type[] getSupportedTypes() {
        return new Types.Type[] { Types.Type.BYTE,
                                  Types.Type.CHAR,
                                  Types.Type.DOUBLE,
                                  Types.Type.FLOAT,
                                  Types.Type.INT,
                                  Types.Type.LONG,
                                  Types.Type.SHORT
                                };
    }

    @Override
    public boolean propertyChange(String key, Object value) {
        if(key == revDistanceProperty) {
            double compassUncorrectedValue = ((Number) data.getValue()).doubleValue() - m_compass.getRevolutionDistance() / 2;
            m_compass.setRevolutionDistance(Double.parseDouble((String) value));
            updateDisplay(compassUncorrectedValue);

        } else if (key == ringColorProperty) {
            m_compass.setRosePaint((Color) value);
        }
        return true;
    }

    @Override
    public Object getPropertyValue(String key) {
        if(key == revDistanceProperty) {
            return m_compass.getRevolutionDistance();
        } else if(key == ringColorProperty) {
            return m_compass.getRosePaint();
        } else return null;
    }
}
