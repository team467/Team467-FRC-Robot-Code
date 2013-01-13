package edu.wpi.first.smartdashboard.gui.elements;

import edu.wpi.first.smartdashboard.state.Record;
import edu.wpi.first.smartdashboard.types.Types;
import edu.wpi.first.smartdashboard.util.StatefulDisplayElement;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.MeterInterval;
import org.jfree.chart.plot.MeterPlot;
import org.jfree.data.Range;
import org.jfree.data.general.DefaultValueDataset;

/**
 *
 * @author Paul
 */
public class SimpleDial extends StatefulDisplayElement {

    final String upperLimitProperty = "Upper limit";
    final String lowerLimitProperty = "Lower limit";
    final String tickIntervalProperty = "Tick interval";

    JPanel chartPanel;
    DefaultValueDataset data = new DefaultValueDataset(0);
    MeterPlot m_meter;
    Range m_plotRange;

    @Override
    public void init() {
	setProperty(upperLimitProperty, new Double(100));
	setProperty(lowerLimitProperty, new Double(0));
        setProperty(tickIntervalProperty, new Double(10));

        m_meter = new MeterPlot(data);
        m_plotRange = new Range(0, 100);
        m_meter.setRange(m_plotRange);
        //plot.addInterval(new MeterInterval("High", new Range(80.0, 100.0)));
        JFreeChart chart = new JFreeChart(m_name, JFreeChart.DEFAULT_TITLE_FONT,
                                            m_meter, false);
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(250, 150));
        
        add(chartPanel);
        revalidate();
        repaint();
    }

    public void update(final Record r) {
        final JPanel myself = this;
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                data.setValue((Number) r.getValue());
                myself.revalidate();
                myself.repaint();
            }
        });
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
	if (key == lowerLimitProperty) {
	    m_plotRange = new Range(new Double((String) value), m_plotRange.getUpperBound());
	}
	else if (key == upperLimitProperty) {
	    m_plotRange = new Range(m_plotRange.getLowerBound(), new Double((String) value));
	}
        else if (key == tickIntervalProperty) {
            m_meter.setTickSize(new Double((String) value));
        }

        if(key == lowerLimitProperty || key == upperLimitProperty)
            m_meter.setRange(m_plotRange);
	return true;
    }

    @Override
    public Object getPropertyValue(String key) {
	if (key == lowerLimitProperty) {
	    return m_plotRange.getLowerBound();
	}
	else if (key == upperLimitProperty) {
	    return m_plotRange.getUpperBound();
	}
        else if (key == tickIntervalProperty) {
            return m_meter.getTickSize();
        }
	else return null;
    }
}
