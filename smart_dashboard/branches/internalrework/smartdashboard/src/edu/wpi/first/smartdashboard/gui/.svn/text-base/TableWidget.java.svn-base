/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.smartdashboard.gui;

import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

/**
 *
 * @author Mitchell Wills
 */
public abstract class TableWidget extends NewWidget implements ITableListener{
    private final ITable table;
    public TableWidget(ITable table){
        this.table = table;
        table.addTableListener(this, true);
        table.addSubTableListener(this);
    }
    
    public void cleanupWidget(){
        table.removeTableListener(this);
    }
}
