package edu.wpi.first.smartdashboard.gui;

import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;
import javax.swing.JComponent;

/**
 *
 * @author Mitchell Wills
 */
public abstract class TableListeningPanel extends JComponent implements ITableListener{
    private final ITable table;
    public TableListeningPanel(ITable table){
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
                newSubtable(key, (ITable) value);
            } else {
                newValue(key, value);
            }
        }
    }

    public abstract void newSubtable(String key, ITable table);

    public abstract void newValue(String key, Object value);
}
