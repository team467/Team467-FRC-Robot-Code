package edu.wpi.first.smartdashboard.gui;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author Mitchell Wills
 */
public abstract class WidgetPanelLayout {
    private WidgetPanel panel;
    void init(WidgetPanel panel){
        if(this.panel!=null)
            throw new RuntimeException("A WidgetPanelLayout can only be used on one panel");
        this.panel = panel;
    }
    protected WidgetPanel getPanel(){
        return panel;
    }
    
    public abstract boolean supportsResize();
    
    public abstract void addWidget(WidgetContainer widgetContainer);

    public abstract void removeWidget(WidgetContainer widgetContainer);

    public abstract void layoutWidget(WidgetContainer widgetContainer);

    public abstract void paintOverlay(Graphics g, WidgetContainer selectedElement);

    public abstract void moveTo(WidgetContainer widgetContainer, int x, int y);

    public abstract void resizeTo(WidgetContainer widgetContainer, int width, int height);

    public void keyPressed(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {}

    public Collection<WidgetMenuItem> getMenuProperties(WidgetContainer widget) {
        return Collections.EMPTY_LIST;
    }


}
