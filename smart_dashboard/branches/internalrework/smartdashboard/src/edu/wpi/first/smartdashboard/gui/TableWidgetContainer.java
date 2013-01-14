/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.smartdashboard.gui;

import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Mitchell Wills
 */
public class TableWidgetContainer extends WidgetContainer<TableWidget> implements ITableListener{
    private final String key;
    private final ITable table;
    private final WidgetRegistry widgetRegistry;
    private TableTableType type = null;
    
    public TableWidgetContainer(String key, ITable table, WidgetRegistry widgetRegistry){
        this.widgetRegistry = widgetRegistry;
        this.table = table;
        this.key = key;
        table.addTableListener("~TYPE~", this, true);
    }

    public void valueChanged(ITable source, String key, Object value, boolean isNew) {
        if(key.equals("~TYPE~") && value instanceof String){
            type = TableTableType.getType((String)value);
            List<WidgetFactory> factories = widgetRegistry.getWidgets(type);
            for(int i = 0; i<factories.size(); ++i){
                WidgetFactory factory = factories.get(i);
                if(factory instanceof TableWidgetFactory){
                    TableWidget widget = ((TableWidgetFactory)factory).createWidget(table);
                    setWidget(widget);
                    return;
                }
            }
        }
        setWidget(null);
        //TODO print cannot handle type
    }
    
    public Collection<WidgetMenuItem> getChangeToMenuItems(){
        List<WidgetMenuItem> changeItems = new ArrayList<WidgetMenuItem>();
            List<WidgetFactory> factories = widgetRegistry.getWidgets(type);
            for(int i = 0; i<factories.size(); ++i){
                final WidgetFactory factory = factories.get(i);
                if(factory instanceof TableWidgetFactory){
                    changeItems.add(new WidgetMenuItem.Action(factory.getName()) {
                        @Override
                        public void execute() {
                            TableWidget widget = ((TableWidgetFactory)factory).createWidget(table);
                            setWidget(widget);
                        }
                    });
                }
            }
        return changeItems;
    }
}
