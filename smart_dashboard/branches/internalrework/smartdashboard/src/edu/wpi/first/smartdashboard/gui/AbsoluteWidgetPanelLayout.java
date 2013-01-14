package edu.wpi.first.smartdashboard.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import javax.swing.JMenuItem;

/**
 *
 * @author Mitchell Wills
 */
public class AbsoluteWidgetPanelLayout extends WidgetPanelLayout{
    private final Map<WidgetContainer, Rectangle> widgetConstraints = new HashMap<WidgetContainer, Rectangle>();
    
    @Override
    public void addWidget(WidgetContainer widgetContainer) {
    }

    @Override
    public void removeWidget(WidgetContainer widgetContainer) {
    }
    
    @Override
    public boolean supportsResize(){
        return true;
    }

    @Override
    public void moveTo(WidgetContainer widgetContainer, int x, int y) {
        Rectangle savedBounds = widgetConstraints.get(widgetContainer);
        Point newLocation = new Point(x, y);
        if(useGrid && !newLocation.equals(savedBounds.getLocation())){
            newLocation.x = (newLocation.x+getGridCellWidth()/2)/getGridCellWidth()*getGridCellWidth();
            newLocation.y = (newLocation.y+getGridCellHeight()/2)/getGridCellHeight()*getGridCellHeight();
        }
        if(savedBounds==null)
            widgetConstraints.put(widgetContainer, new Rectangle(newLocation, widgetContainer.getPreferredSize()));
        else
            widgetConstraints.put(widgetContainer, new Rectangle(newLocation, savedBounds.getSize()));
        layoutWidget(widgetContainer);
    }

    @Override
    public void resizeTo(WidgetContainer widgetContainer, int width, int height) {
        Rectangle savedBounds = widgetConstraints.get(widgetContainer);
        Dimension newSize = new Dimension(width, height);
        if(useGrid && !newSize.equals(savedBounds.getSize())){
            newSize.width = (newSize.width+getGridCellWidth()/2)/getGridCellWidth()*getGridCellWidth();
            newSize.height = (newSize.height+getGridCellHeight()/2)/getGridCellHeight()*getGridCellHeight();
        }
        if(savedBounds==null)
            widgetConstraints.put(widgetContainer, new Rectangle(findSpace(widgetContainer), newSize));
        else
            widgetConstraints.put(widgetContainer, new Rectangle(savedBounds.getLocation(), newSize));
        layoutWidget(widgetContainer);
    }
    
    @Override
    public void layoutWidget(WidgetContainer widgetContainer){
        Rectangle savedBounds = widgetConstraints.containsKey(widgetContainer)?new Rectangle(widgetConstraints.get(widgetContainer)):null;
        Dimension minSize = widgetContainer.getMinimumSize();
        Dimension preferredSize = widgetContainer.getPreferredSize();
        Dimension maxSize = widgetContainer.getMaximumSize();
        if(savedBounds==null){
            savedBounds = new Rectangle(findSpace(widgetContainer), widgetContainer.getPreferredSize());
        }
        else{
            Dimension savedSize = savedBounds.getSize();
            Dimension size = new Dimension(preferredSize);
            if (savedSize != null && savedSize.width != -1) {
                size.width = savedSize.width;
            }
            if (savedSize != null && savedSize.height != -1) {
                size.height = savedSize.height;
            }
            savedBounds = new Rectangle(savedBounds.getLocation(), size);
        }
        if(savedBounds.width<minSize.width)
            savedBounds.width = minSize.width;
        if(savedBounds.width>maxSize.width)
            savedBounds.width = maxSize.width;
        
        if(savedBounds.height<minSize.height)
            savedBounds.height = minSize.height;
        if(savedBounds.height>maxSize.height)
            savedBounds.height = maxSize.height;

        widgetContainer.setLocation(savedBounds.getLocation());
        widgetContainer.setSize(savedBounds.getSize());
        widgetConstraints.put(widgetContainer, savedBounds);
        
        widgetContainer.revalidate();//TODO maybe only do this on resize
    }
    
    
    private static final Color GRID_COLOR = new Color(0, 0, 0, 40);
    private boolean useGrid = false;
    private int getGridCellWidth(){
        return 16;
    }
    private int getGridCellHeight(){
        return 16;
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            useGrid = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            useGrid = false;
        }
    }

    @Override
    public void paintOverlay(Graphics g, WidgetContainer selectedWidget) {
        Rectangle bounds = getPanel().getBounds();

       if (selectedWidget != null) {
            Rectangle widgetBounds = selectedWidget.getBounds();

            g.setColor(Color.GRAY);
            g.drawRoundRect(widgetBounds.x - 1, widgetBounds.y - 1, widgetBounds.width + 1, widgetBounds.height + 1, 8, 8);
        }

        if (useGrid) {
            g.setColor(GRID_COLOR);

            //DashboardPrefs pref = frame.getPrefs();
            int[] w = {getGridCellWidth()};//pref.grid_widths.getValue();
            int[] h = {getGridCellHeight()};//pref.grid_heights.getValue();

            int cell = -1;
            for (int i = 0; i < bounds.width; i += w[cell = (cell + 1) % w.length]) {
                    g.drawLine(i, 0, i, bounds.height);
            }

            cell = -1;
            for (int i = 0; i < bounds.height; i += h[cell = (cell + 1) % h.length]) {
                    g.drawLine(0, i, bounds.width, i);
            }
        }
    }
    @Override
    public Collection<WidgetMenuItem> getMenuProperties(final WidgetContainer widget) {
        List<WidgetMenuItem> menuItems = new ArrayList<WidgetMenuItem>();
        if(widget!=null){
            menuItems.add(new WidgetMenuItem.Action("Send to Back") {
                @Override
                public void execute() {
                    //TODO send Widget to Back
                }
            });
            menuItems.add(new WidgetMenuItem.Action("Reset Size") {
                @Override
                public void execute() {
                    Rectangle newBounds = new Rectangle(widgetConstraints.get(widget).getLocation(), widget.getPreferredSize());
                    widgetConstraints.put(widget, newBounds);
                    layoutWidget(widget);
                }
            });
            menuItems.add(new WidgetMenuItem.Action("Remove") {
                @Override
                public void execute() {
                    //TODO remove widget
                }
            });
        }
        else{
            //TODO add layout config
        }
        return menuItems;
    }
    
    
    private static final Random random = new Random();
    /**
     * Finds a space to put the newest element, using its preferred size
     * @param toPlace the element to place
     * @return the place where it should go
     */
    private Point findSpace(WidgetContainer toPlace) {
        Stack<Point> positions = new Stack<Point>();
        positions.add(new Point(0, 0));

        Dimension size = toPlace.getSize();
        Dimension panelBounds = getPanel().getSize();

        PositionLoop:
        while (!positions.isEmpty()) {
            Point position = positions.pop();
            Rectangle area = new Rectangle(position, size);

            if (area.x < 0 || area.y < 0
                           || area.x + area.width > panelBounds.width
                           || area.y + area.height > panelBounds.height) {
                continue;
            }

            for (WidgetContainer element : widgetConstraints.keySet()) {
                if (element != toPlace && element.isObstruction()) {
                    Rectangle bounds = element.getBounds();
                    // Test Intersection
                    if (!(bounds.x > area.x + area.width
                       || bounds.x + bounds.width < area.x
                       || bounds.y > area.y + area.height
                       || bounds.y + bounds.height < area.y)) {
                        Point right = new Point(bounds.x + bounds.width + 1, position.y);
                        if (positions.isEmpty()) {
                            positions.add(right);
                            right = null;
                        }
                        positions.add(new Point(position.x, bounds.y + bounds.height + 1));
                        if (right != null && Math.abs(right.x - area.x) < area.width / 3) {
                            positions.add(right);
                        }
                        continue PositionLoop;
                    }
                }
            }

            return position;
        }

        // If no space was found, jumble them at the beginning
        return new Point(random.nextInt(32), random.nextInt(32));
    }

    
}
