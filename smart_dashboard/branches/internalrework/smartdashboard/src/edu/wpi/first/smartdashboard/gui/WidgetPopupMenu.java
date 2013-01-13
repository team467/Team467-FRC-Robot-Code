package edu.wpi.first.smartdashboard.gui;

import java.util.Iterator;
import javax.swing.JPopupMenu;

/**
 *
 * @author Mitchell Wills
 */
public class WidgetPopupMenu extends JPopupMenu{
    public void show(WidgetPanelGlassPanel glassPane, WidgetContainer widget, WidgetPanelLayout layout, int x, int y){
        removeAll();
        for(WidgetMenuItem item:layout.getMenuProperties(widget)){
            add(item.createMenuItem());
        }
        if(widget!=null){
            addSeparator();
            for (Iterator<WidgetMenuItem> it = widget.getMenuProperties().iterator(); it.hasNext();) {//TODO figure out why this won't compile as enhanced for loop
                WidgetMenuItem item = it.next();
                add(item.createMenuItem());
            }
        }
            
        show(glassPane, x, y);
    }
}
