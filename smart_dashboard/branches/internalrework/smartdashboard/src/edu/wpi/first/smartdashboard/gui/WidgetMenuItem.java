/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.smartdashboard.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Mitchell Wills
 */
public class WidgetMenuItem {
    private final String label;
    private boolean enabled = true;
    public WidgetMenuItem(String label){
        this.label = label;
    }
    public String getLabel(){
        return label;
    }
    
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public final JMenuItem createMenuItem(){
        JMenuItem item = _createMenuItem();
        item.setEnabled(enabled);
        return item;
    }
    protected JMenuItem _createMenuItem(){
        return new JMenuItem(getLabel());
    }
    
    public static abstract class Action extends WidgetMenuItem{
        public Action(String label){
            super(label);
        }
        public abstract void execute();
        
        @Override
        protected JMenuItem _createMenuItem(){
            JMenuItem menuItem = new JMenuItem(getLabel());
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    execute();
                }
            });
            return menuItem;
        }
    }
    public static abstract class Boolean extends WidgetMenuItem{
        public Boolean(String label){
            super(label);
        }
        public abstract boolean getValue();
        public abstract void setValue(boolean value);
        
        @Override
        protected JMenuItem _createMenuItem(){
            final JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(getLabel(), getValue());
            menuItem.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    setValue(menuItem.isSelected());
                }
            });
            return menuItem;
        }
    }
    public static class Menu extends WidgetMenuItem{
        private final Iterable<WidgetMenuItem>subItems;
        public Menu(String label, Iterable<WidgetMenuItem> subItems){
            super(label);
            if(subItems!=null)
                this.subItems = subItems;
            else
                this.subItems = Collections.EMPTY_LIST;
        }
        
        @Override
        protected JMenuItem _createMenuItem(){
            final JMenu menuItem = new JMenu(getLabel());
            for(WidgetMenuItem item:subItems)
                menuItem.add(item.createMenuItem());
            return menuItem;
        }
    }
}
