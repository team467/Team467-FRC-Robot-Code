package edu.wpi.first.smartdashboard.gui.elements;

import edu.wpi.first.smartdashboard.gui.Widget;
import edu.wpi.first.smartdashboard.properties.ColorProperty;
import edu.wpi.first.smartdashboard.properties.DoubleProperty;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.types.DataType;
import edu.wpi.first.smartdashboard.types.named.GyroType;
import edu.wpi.first.wpilibj.networking.NetworkListener;
import edu.wpi.first.wpilibj.networking.NetworkTable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CompassPlot;
import org.jfree.data.general.DefaultValueDataset;

/**
 *
 * @author Paul
 */
public class Compass extends Widget implements NetworkListener {

    public static final DataType[] TYPES = {DataType.NUMBER, GyroType.get()};
    public final DoubleProperty circumference = new DoubleProperty(this, "Circumference", 360.0);
    public final ColorProperty ringColor = new ColorProperty(this, "Ring Color", Color.YELLOW);
    private JPanel chartPanel;
    private DefaultValueDataset data = new DefaultValueDataset(0);
    private CompassPlot m_compass;
    private NetworkTable gyroTable;
    private Runnable updater = new Runnable() {
        public void run() {
            if (gyroTable != null) {
                data.setValue(gyroTable.getInt("angle") + m_compass.getRevolutionDistance() / 2);
            }
            
            repaint();
        }
    };

    @Override
    public void init() {
        setLayout(new BorderLayout());

        m_compass = new CompassPlot(data);
        m_compass.setSeriesNeedle(7);
        m_compass.setSeriesPaint(0, Color.RED);
        m_compass.setSeriesOutlinePaint(0, Color.RED);

        JFreeChart chart = new JFreeChart(getFieldName(), JFreeChart.DEFAULT_TITLE_FONT,
                m_compass, false);
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(250, 150));

        add(chartPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void updateDisplay(final double value) {
        setValue(value);
    }

    @Override
    public void setValue(Object value) {
        if (gyroTable != null) {
            gyroTable.removeListener("angle", this);
            gyroTable = null;
        }
        if (getType().isChildOf(DataType.NUMBER)) {
            data.setValue(((Number) value).doubleValue() + m_compass.getRevolutionDistance() / 2);
        } else if (getType().isChildOf(GyroType.get())) {
            gyroTable = (NetworkTable) value;
            gyroTable.addListener("angle", this);
            data.setValue(gyroTable.getInt("angle") + m_compass.getRevolutionDistance() / 2);
        }

        repaint();
    }

    @Override
    public void propertyChanged(Property property) {
        if (property == circumference) {
            double compassUncorrectedValue = ((Number) data.getValue()).doubleValue() - m_compass.getRevolutionDistance() / 2;
            m_compass.setRevolutionDistance(circumference.getValue());
            updateDisplay(compassUncorrectedValue);
        } else if (property == ringColor) {
            m_compass.setRosePaint(ringColor.getValue());
        }
    }

    public void valueChanged(String key, Object value) {
        SwingUtilities.invokeLater(updater);
    }

    public void valueConfirmed(String key, Object value) {
    }
}
