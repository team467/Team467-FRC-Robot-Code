package edu.wpi.first.smartdashboard.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Mitchell Wills
 */
public class WidgetPanel extends JLayeredPane{
    private final WidgetRegistry widgetRegistry;
    private final WidgetPanelLayout layout;
    private final Map<String, WidgetContainer> widgets = new HashMap<String, WidgetContainer>();
    private final WidgetPanelGlassPanel glassPanel;
    private final JComponent widgetPanel;
    
    public WidgetPanel(WidgetPanelLayout layout, WidgetRegistry widgetRegistry){
        this.layout = layout;
        this.widgetRegistry = widgetRegistry;
        layout.init(this);
        
        setLayout(new Layout());
        add(glassPanel = new WidgetPanelGlassPanel(this, layout));
        add(widgetPanel = new JLayeredPane());
        widgetPanel.setLayout(new ContainerLayout(layout));
    }
    
    
    protected WidgetRegistry getWidgetRegistry(){
        return widgetRegistry;
    }
    
    protected void newWidget(final String name, final WidgetContainer container){
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    widgets.put(name, container);
                    layout.addWidget(container);
                    widgetPanel.add(container);
                    revalidate();
                    repaint();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void removeWidget(final String name){
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    WidgetContainer container = widgets.remove(name);
                    layout.removeWidget(container);
                    container.cleanup();
                    widgetPanel.remove(container);
                    revalidate();
                    repaint();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void newWidget(String name, NewWidget widget){
        newWidget(name, new StaticWidgetContainer(widget));
    }

    public WidgetContainer findWidgetAt(Point point) {
        Component c = widgetPanel.findComponentAt(point);
        while(c!=null && c.getParent()!=widgetPanel)
            c = c.getParent();
        if(c instanceof WidgetContainer)
            return (WidgetContainer)c;
        return null;
    }
    
    
    private class ContainerLayout implements LayoutManager {
        private final WidgetPanelLayout layout;
        public ContainerLayout(WidgetPanelLayout layout){
            this.layout = layout;
        }

        public void addLayoutComponent(String name, Component comp) {
        }

        public void removeLayoutComponent(Component comp) {
        }

        public Dimension preferredLayoutSize(Container parent) {
            return new Dimension(640, 480);
        }

        public Dimension minimumLayoutSize(Container parent) {
            return new Dimension(0, 0);
        }

        public void layoutContainer(Container parent) {
            for (WidgetContainer widgetContainer : widgets.values())
                layout.layoutWidget(widgetContainer);
        }
    }
    private class Layout implements LayoutManager {
        public Layout(){}

        public void addLayoutComponent(String name, Component comp) {
        }

        public void removeLayoutComponent(Component comp) {
        }

        public Dimension preferredLayoutSize(Container parent) {
            return new Dimension(640, 480);
        }

        public Dimension minimumLayoutSize(Container parent) {
            return new Dimension(0, 0);
        }

        public void layoutContainer(Container parent) {
            widgetPanel.setBounds(getBounds());
            glassPanel.setBounds(getBounds());
        }
    }
    
}
