package edu.wpi.first.smartdashboard.gui.elements;

import edu.wpi.first.smartdashboard.gui.Widget;
import edu.wpi.first.smartdashboard.properties.DoubleProperty;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.types.DataType;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.MeterPlot;
import org.jfree.data.Range;
import org.jfree.data.general.DefaultValueDataset;

/**
 *
 * @author Paul
 */
public class SimpleDial extends Widget {
    public static final DataType[] TYPES = { DataType.NUMBER };

    public final DoubleProperty max = new DoubleProperty(this, "Upper Limit", 100);
    public final DoubleProperty min = new DoubleProperty(this, "Lower Limit", 0);
    public final DoubleProperty tickInterval = new DoubleProperty(this, "Tick Interval", 10);

    JPanel chartPanel;
    DefaultValueDataset data = new DefaultValueDataset(0);
    MeterPlot m_meter;
    Range m_plotRange;

    @Override
    public void init() {
        setLayout(new BorderLayout());

        m_meter = new MeterPlot(data);
        m_plotRange = new Range(min.getValue(), max.getValue());
        m_meter.setRange(m_plotRange);
        //plot.addInterval(new MeterInterval("High", new Range(80.0, 100.0)));
        JFreeChart chart = new JFreeChart(getFieldName(), JFreeChart.DEFAULT_TITLE_FONT,
                                            m_meter, false);
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(250, 150));

        propertyChanged(tickInterval);

        add(chartPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    @Override
    public void setValue(Object value) {
        data.setValue((Number) value);
        revalidate();
        repaint();
    }

    @Override
    public void propertyChanged(Property property) {
        if (property == min || property == max) {
            m_plotRange = new Range(min.getValue(), max.getValue());
            m_meter.setRange(m_plotRange);
        } else if (property == tickInterval) {
            m_meter.setTickSize(tickInterval.getValue());
        }
    }
}
