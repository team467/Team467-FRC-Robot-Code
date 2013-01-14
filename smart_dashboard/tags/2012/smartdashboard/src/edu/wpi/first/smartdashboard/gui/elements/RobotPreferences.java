package edu.wpi.first.smartdashboard.gui.elements;

import edu.wpi.first.smartdashboard.gui.StaticWidget;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.robot.Robot;
import edu.wpi.first.wpilibj.networking.NetworkAdditionListener;
import edu.wpi.first.wpilibj.networking.NetworkListener;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Joe Grinstead
 */
public class RobotPreferences extends StaticWidget implements NetworkListener, NetworkAdditionListener {

    private JTable table;
    private PreferenceTableModel model;
    private Map<String, String> values;
    private JButton save;
    private Runnable repainter = new Runnable() {

        public void run() {
            save.setEnabled(!Robot.getPreferences().getBoolean(Robot.PREF_SAVE_FIELD));
            repaint();
            repainting = false;
        }
    };
    private boolean repainting = false;

    @Override
    public void init() {
        save = new JButton("Save");
        save.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Robot.getPreferences().putBoolean(Robot.PREF_SAVE_FIELD, true);
            }
        });

        values = new LinkedHashMap<String, String>();

        Robot.getPreferences().addAdditionListener(this, true);
        Robot.getPreferences().addListenerToAll(this);

        model = new PreferenceTableModel();

        table = new JTable(model);

        setLayout(new BorderLayout());

        add(table, BorderLayout.NORTH);
        add(save, BorderLayout.SOUTH);
    }

    @Override
    public void disconnect() {
        Robot.getPreferences().removeAdditionListener(this);
        Robot.getPreferences().removeListenerFromAll(this);
    }

    @Override
    public void propertyChanged(Property property) {
    }

    public void valueChanged(String key, Object value) {
        if (key.equals(Robot.PREF_SAVE_FIELD)) {
            save.setEnabled(!(Boolean) value);
        } else {
            values.put(key, value.toString());
        }
        System.out.println(key + "->" + value);
        if (!repainting) {
            repainting = true;
            EventQueue.invokeLater(repainter);
        }
    }

    public void valueConfirmed(String key, Object value) {
        valueChanged(key, value);
    }

    public void fieldAdded(String key, Object value) {
        valueChanged(key, value);
    }

    private class PreferenceTableModel extends AbstractTableModel {

        public int getRowCount() {
            return values.size() + 2;
        }

        public int getColumnCount() {
            return 2;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                if (!values.containsKey(aValue.toString())) {
                    values.put(aValue.toString(), "");
                }
            } else {
                int row = 1;
                for (Map.Entry<String, String> entry : values.entrySet()) {
                    if (row++ == rowIndex) {
                        entry.setValue(aValue.toString());
                        Robot.getPreferences().putString(entry.getKey(), entry.getValue());
                    }
                }
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return (rowIndex > 0 && columnIndex > 0 && rowIndex <= values.size())
                    || (rowIndex == values.size() + 1 && columnIndex == 0);
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rowIndex == 0) {
                return columnIndex == 0 ? "Key" : "Value";
            } else if (rowIndex == values.size() + 1) {
                return columnIndex == 0 ? "Add..." : "";
            }
            int row = 1;
            for (Map.Entry<String, String> entry : values.entrySet()) {
                if (row++ == rowIndex) {
                    return columnIndex == 0 ? entry.getKey() : entry.getValue();
                }
            }
            return "ERROR";
        }
    }
}
