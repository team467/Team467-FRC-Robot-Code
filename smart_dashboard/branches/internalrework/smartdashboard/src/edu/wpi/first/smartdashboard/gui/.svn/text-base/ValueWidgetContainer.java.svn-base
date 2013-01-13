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
public class ValueWidgetContainer extends WidgetContainer<ValueWidget> implements ITableListener{
    private final ITable parentTable;
    private final String key;
    private final WidgetRegistry widgetRegistry;
    private ValueType type = null;
    private Object value = null;
    
    public ValueWidgetContainer(ITable parentTable, String key, WidgetRegistry widgetRegistry){
        this.widgetRegistry = widgetRegistry;
        this.parentTable = parentTable;
        this.key = key;
        parentTable.addTableListener(key, this, true);
    }

    public void valueChanged(ITable _source, String _key, Object value, boolean _isNew) {
        ValueWidget widget = getWidget();
        type = ValueType.getTypeOf(value);
        this.value = value;
        if(widget==null){//TODO handle value type changes
            List<WidgetFactory> factories = widgetRegistry.getWidgets(type);
            for(int i = 0; i<factories.size(); ++i){
                WidgetFactory factory = factories.get(i);
                if(factory instanceof ValueWidgetFactory){
                    widget = ((ValueWidgetFactory)factory).createWidget(key);
                    setWidget(widget);
                    break;
                }
            }
            if(factories.size()==0){
                setWidget(null);
                //TODO print cannot handle type
            }
        }
        updateValue();
    }
    
    protected void setWidget(ValueWidget widget){
        super.setWidget(widget);
        updateValue();
    }
    protected void updateValue(){
        ValueWidget widget = getWidget();
        if(widget!=null && value!=null)
            widget.setValue(value);
    }
    
    public Collection<WidgetMenuItem> getChangeToMenuItems(){
        List<WidgetMenuItem> changeItems = new ArrayList<WidgetMenuItem>();
            List<WidgetFactory> factories = widgetRegistry.getWidgets(type);
            for(int i = 0; i<factories.size(); ++i){
                final WidgetFactory factory = factories.get(i);
                if(factory instanceof ValueWidgetFactory){
                    changeItems.add(new WidgetMenuItem.Action(factory.getName()) {
                        @Override
                        public void execute() {
                            ValueWidget widget = ((ValueWidgetFactory)factory).createWidget(key);
                            setWidget(widget);
                        }
                    });
                }
            }
        return changeItems;
    }
}
