package edu.wpi.first.smartdashboard.gui;

import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

/**
 *
 * @author Mitchell Wills
 */
public class TableWidgetPanel extends WidgetPanel implements ITableListener{
    private final ITable table;
    
    public TableWidgetPanel(ITable table, WidgetPanelLayout layout, WidgetRegistry widgetRegistry){
        super(layout, widgetRegistry);
        this.table = table;
        table.addTableListener(this, true);
        table.addSubTableListener(this);
    }
    
    public ITable getTable(){
        return table;
    }
    
    @Override
    public void valueChanged(final ITable source, final String key, final Object value, final boolean isNew) {
        if (isNew) {
            if (value instanceof ITable) {
                newTable(key, (ITable) value);
            } else {
                newTableValue(source, key);
            }
        }
    }
    
    protected void newTable(String key, ITable table) {
        TableWidgetContainer container = new TableWidgetContainer(key, table, getWidgetRegistry());
        newWidget(key, container);
    }

    protected void newTableValue(ITable parentTable, String key) {
        ValueWidgetContainer container = new ValueWidgetContainer(parentTable, key, getWidgetRegistry());
        newWidget(key, container);
    }

    
    
}
