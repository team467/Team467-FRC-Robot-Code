package edu.wpi.first.smartdashboard.gui.elements;

import edu.wpi.first.smartdashboard.state.Record;
import edu.wpi.first.smartdashboard.types.Types;
import edu.wpi.first.smartdashboard.util.StatefulDisplayElement;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author pmalmsten
 */
public class LinePlot extends StatefulDisplayElement {
    final String bufferSizeProperty = "Buffer Size (samples)";
    final String widthProperty = "Width (pixels)";
    final String heightProperty = "Height (pixels)";

    JPanel m_chartPanel;
    XYSeries m_data ;
    XYDataset m_dataset;
    JFreeChart m_chart;
    long m_timeUnit = 0;
    long m_maxBufferSize = 5000;

    @Override
    public void init() {
        final int defaultWidth = 400;
        final int defaultHeight = 300;
        
        setProperty(bufferSizeProperty, m_maxBufferSize);
        setProperty(widthProperty, defaultWidth);
        setProperty(heightProperty, defaultHeight);

        m_data = new XYSeries(m_name);
        m_dataset = new XYSeriesCollection(m_data);
        
        JFreeChart chart = ChartFactory.createXYLineChart(
                m_name,
                "Time (units)",
                "Data",
                m_dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
                );

        m_chartPanel = new ChartPanel(chart);
        m_chartPanel.setPreferredSize(new Dimension(defaultWidth, defaultHeight));
        
        add(m_chartPanel);
        revalidate();
        repaint();
    }

    public void update(final Record r) {
        final JPanel myself = this;
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                m_data.add(m_timeUnit++, (Number) r.getValue());
                
                if(m_data.getItemCount() > m_maxBufferSize)
                    m_data.remove(0);
                
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
        if(key == bufferSizeProperty) {
            m_maxBufferSize = Long.parseLong((String) value);

            while(m_data.getItemCount() > m_maxBufferSize)
                m_data.remove((int) 0);
        } else if(key == widthProperty) {
            Dimension size = m_chartPanel.getSize();
            size.width = Integer.parseInt((String) value);
            
            m_chartPanel.setPreferredSize(size);
            setSize(size);
        } else if (key == heightProperty) {
            Dimension size = m_chartPanel.getSize();
            size.height = Integer.parseInt((String) value);
            
            m_chartPanel.setPreferredSize(size);
            setSize(size);
        }
        return true;
    }

    @Override
    public Object getPropertyValue(String key) {
        if(key == bufferSizeProperty) {
            return m_maxBufferSize;
        } else if(key == widthProperty) {
            return getSize().width;
        } else if(key == heightProperty) {
            return getSize().height;
        }
        return null;
    }
}
