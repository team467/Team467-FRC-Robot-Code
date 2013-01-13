/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.smartdashboard.gui;

import edu.wpi.first.smartdashboard.util.DisplayElement;
import edu.wpi.first.smartdashboard.util.StatefulDisplayElement;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author brad
 */
public class PropertyEditor extends JDialog {

    JTable table;
    PropTableModel tableModel;
    DisplayElement selectedElement;
    String[] elementProperties;

    public PropertyEditor(JFrame frame) {
	super(frame, true);
	tableModel = new PropTableModel();
	table = new PropertiesTable(tableModel);
	table.setGridColor(Color.LIGHT_GRAY);
	table.setRowSelectionAllowed(false);
	JScrollPane scrollPane = new JScrollPane(table);
	setBounds(100, 100, 300, 400);
	getContentPane().setLayout(new BorderLayout());
	getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    void setDisplayElement(DisplayElement selectedElement) {
        this.selectedElement = selectedElement;
        if (selectedElement instanceof StatefulDisplayElement) {
            this.setTitle(((StatefulDisplayElement) selectedElement).getFieldName());
        } else {
            this.setTitle("Edit Properties");
        }
        elementProperties = selectedElement.getPropertiesKeys();
        tableModel.fireTableDataChanged();
    }

    class PropertiesTable extends JTable {

	TableCellEditor colorTableCellEditor = new ColorTableCellEditor();
	TableCellRenderer colorTableCellRenderer = new ColorTableCellRenderer();
	AbstractTableModel model;

	PropertiesTable(AbstractTableModel model) {
	    super(model);
	    this.model = model;
	}
	
	@Override
	public TableCellEditor getCellEditor(int row, int col) {
	    if (selectedElement.getPropertyValue(elementProperties[row]) instanceof Color) {
		return colorTableCellEditor;
	    }
	    else return super.getCellEditor(row, col);
	}

        @Override
	public TableCellRenderer getCellRenderer(int row, int col) {
	    if (model.getValueAt(row, col) instanceof Color) {
		return colorTableCellRenderer;
	    }
	    else return super.getCellRenderer(row, col);
	}
    }

    class ColorTableCellEditor extends AbstractCellEditor implements TableCellEditor {

	private JColorChooser colorChooser;
	private JDialog colorDialog;
	private JPanel panel;

	public ColorTableCellEditor() {
	    panel = new JPanel();
	    colorChooser = new JColorChooser();
	    colorDialog = JColorChooser.createDialog(PropertyEditor.this, "Color editor", true, colorChooser,
		    new ActionListener() {  // OK Button listener

			public void actionPerformed(ActionEvent ev) {
			    stopCellEditing();
			}

		    }, new ActionListener() { // Cancel button listener

		public void actionPerformed(ActionEvent ev) {
		    cancelCellEditing();
		}
	    });
	    colorDialog.addWindowListener(new WindowAdapter() {

		@Override
		public void windowClosing(WindowEvent event) {
		    cancelCellEditing();
		}
	    });
	}

	@Override
	public boolean shouldSelectCell(EventObject av) {
	    // start editing and tell caller it's OK to edit this cell
	    colorDialog.setVisible(true);
	    return true;
	}

	@Override
	public void cancelCellEditing() {
	    // editing is calceled -- hide dialog
	    colorDialog.setVisible(false);
	    super.cancelCellEditing();
	}

	@Override
	public boolean stopCellEditing() {
	    // editing is complete -- hide dialog
	    colorDialog.setVisible(false);
	    super.stopCellEditing();
	    return true;
	}

	public Object getCellEditorValue() {
	    return colorChooser.getColor();
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
	    // get the current color and store in the dialog in case the user starts
	    // editing it
	    colorChooser.setColor((Color) value);
	    return panel;
	}
    }

    class ColorTableCellRenderer extends JPanel implements TableCellRenderer {

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
	    setBackground((Color) value);
	    if (hasFocus) {
		setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
	    } else setBorder(null);
	    return this;
	}

    }

    class PropTableModel extends AbstractTableModel {

	public int getRowCount() {
	    return elementProperties.length;
	}

	public int getColumnCount() {
	    return 2;
	}

	@Override
	public String getColumnName(int i) {
	    if (i == 0) {
		return "Property";
	    } else if (i == 1) {
		return "Value";
	    } else {
		return "Error";
	    }
	}

	@Override
	public boolean isCellEditable(int row, int col) {
	    boolean editable = (col == 1);
	    return editable;
	}

	public Object getValueAt(int row, int col) {
	    assert (col == 0 || col == 1);
	    if (col == 0) {
		return elementProperties[row];
	    } else if (col == 1) {
		Object value = selectedElement.getPropertyValue(elementProperties[row]);
		return value;
	    } else {
		return "Bad row, col";
	    }
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
	    assert (col == 1);
	    selectedElement.propertyChange(elementProperties[row], value);
	}
    }
}
