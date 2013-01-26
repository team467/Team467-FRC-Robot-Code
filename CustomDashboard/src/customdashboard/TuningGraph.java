/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package customdashboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.LinkedList;
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
 * @author aidan
 */
public class TuningGraph extends ChartPanel
{
    //Chart objects
    private JFreeChart chart;
    private XYSeriesCollection dataset;
    
    /**
     * Constructor to create a new tuning graph
     * @param name The name of the graph
     * @param xAxis The name of the x axis
     * @param yAxis The name of the y axis
     */
    public TuningGraph(String name, String xAxis, String yAxis, double xMin, 
                       double xMax, double yMin, double yMax)
    {
        super(null);
        
        //Create an empty dataset
        dataset = new XYSeriesCollection();
        
        //Create a new chart
        chart = ChartFactory.createXYLineChart(
                name,
                xAxis,
                yAxis,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);
        
        //Add the chart to this chart panel
        setChart(chart);
        
        //Initialize graph settings
        setSize(new Dimension(800, 600));
        setBackground(getBackground());
        chart.getXYPlot().getDomainAxis().setRange(xMin, xMax);
        chart.getXYPlot().getRangeAxis().setRange(yMin, yMax);
        
        //Repaint the component
        repaint();
    }
    
    /**
     * Adds a new XY Series to the chart
     * @param name The key name of the XY Series
     */
    public void addSeries(String name)
    {
        XYSeries data = new XYSeries(name);
        dataset.addSeries(data);
    }
    
    /**
     * Adds an point to the XYSeries with the given name
     * @param name The XY Series key
     * @param xVal The x value of the point
     * @param yVal Th y value of the point
     */
    public void addValue(String name, double xVal, double yVal)
    {
        dataset.getSeries(name).add(xVal, yVal);
    }
}
