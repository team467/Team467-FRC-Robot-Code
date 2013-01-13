package edu.wpi.first.smartdashboard.gui;

import edu.wpi.first.smartdashboard.properties.PropertyHolder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JComponent;

/**
 *
 * @author Mitchell Wills
 */
public abstract class WidgetContainer<T extends NewWidget> extends JComponent{
    private T widget = null;
    public WidgetContainer(){
        setLayout(new BorderLayout());
    }
    
    protected void setWidget(T widget){
        if(this.widget!=null){
            remove(this.widget);
            this.widget.cleanupWidget();
        }
        this.widget = widget;
        if(widget!=null)
            add(widget, BorderLayout.CENTER);
        revalidate();
    }
    
    @Override
    public Dimension getMaximumSize(){
        if(widget!=null)
            return widget.getMaximumSize();
        return super.getMaximumSize();
    }
    
    void cleanup(){
        setWidget(null);
    }
    
    protected T getWidget(){
        return widget;
    }
    
    public boolean isObstruction() {
        if(widget!=null)
            return widget.isObstruction();
        return false;
    }
    public boolean isResizable() {
        if(widget!=null)
            return widget.isResizable();
        return false;
    }

    public abstract Collection<WidgetMenuItem> getChangeToMenuItems();
    
    public Collection<WidgetMenuItem> getMenuProperties() {
        final NewWidget widget = getWidget();
        List<WidgetMenuItem> menuItems = new ArrayList<WidgetMenuItem>();
        
        Collection<WidgetMenuItem> changeToItems = getChangeToMenuItems();
        WidgetMenuItem.Menu menu = new WidgetMenuItem.Menu("Change to...", changeToItems);
        if(changeToItems==null || changeToItems.size()==0){
            menu.setEnabled(false);
        }
        menuItems.add(menu);
        
        if(widget instanceof PropertyHolder && !((PropertyHolder)widget).getProperties().isEmpty()){
            menuItems.add(new WidgetMenuItem.Action("Properties...") {//only add if the widget actualy has properties
                @Override
                public void execute() {
                    PropertyEditor editor = PropertyEditor.getPropertyEditor();
                    editor.setPropertyHolder((PropertyHolder)widget);
                    editor.setVisible(true);
                }
            });
        }
        return menuItems;
    }


}
