/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.smartdashboard.gui;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Mitchell Wills
 */
public class AbstractWidgetRegistry implements WidgetRegistry{
    private Collection<ValueWidgetFactory> valueWidgets = new LinkedList<ValueWidgetFactory>();
    public AbstractWidgetRegistry() {
    }
    
    public void addValueWidget(ValueWidgetFactory factory){
        valueWidgets.add(factory);
    }

    public List<WidgetFactory> getWidgets(NewDataType type) {
        List<WidgetFactory> factories = new LinkedList<WidgetFactory>();
        if(type instanceof ValueType){
            for(ValueWidgetFactory factory:valueWidgets){
                if(factory.supports(((ValueType)type).getType()))
                    factories.add(factory);
            }
        }
        return factories;
    }
    
}
