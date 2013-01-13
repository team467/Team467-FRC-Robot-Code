package edu.wpi.first.smartdashboard.gui;

import java.util.Collection;

/**
 *
 * @author Mitchell Wills
 */
public class StaticWidgetContainer extends WidgetContainer{
    public StaticWidgetContainer(NewWidget widget){
        setWidget(widget);
    }
    public Collection<WidgetMenuItem> getChangeToMenuItems(){
        return null;
    }
}
