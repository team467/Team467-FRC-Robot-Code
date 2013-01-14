package edu.wpi.first.smartdashboard.gui.elements;

import edu.wpi.first.smartdashboard.properties.BooleanProperty;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.types.DataType;
import edu.wpi.first.smartdashboard.types.named.StringChooserType;
import edu.wpi.first.wpilibj.networking.NetworkListener;
import edu.wpi.first.wpilibj.networking.NetworkTable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 *
 * @author Joe Grinstead
 */
public class Chooser extends EditableDisplayElement implements NetworkListener {

    private static final String DEFAULT = "default";
    private static final String COUNT = "count";
    private static final String SELECTED = "selected";

    public static final DataType[] TYPES = {StringChooserType.get()};

    public final BooleanProperty useRadioButtons = new BooleanProperty(this, "Use Radio Buttons", true);

    private NetworkTable table;
    private Display display;
    private String selection;

    @Override
    public void init() {
        setResizable(false);
    }

    @Override
    public void setValue(Object value) {
        if (table != null) {
            table.removeListenerFromAll(this);
        }

        selection = null;

        table = (NetworkTable) value;

        if (display != null) {
            remove(display.panel);
        }

        display = useRadioButtons.getValue() ? new RadioButtons() : new ComboBox();
        display.setValue(table);

        table.addListenerToAll(this);
    }

    public void valueChanged(String key, Object value) {
        if (key.equals(COUNT)) {
            display.setValue(table);
        } else if (key.equals(DEFAULT) && selection == null) {
            display.setValue(table);
        } else if (key.equals(SELECTED)) {
            display.setSelected((String) value);
        }
    }

    public void valueConfirmed(String key, Object value) {
        valueChanged(key, value);
    }


    @Override
    public void propertyChanged(Property property) {
        if (property == useRadioButtons) {
            setValue(table);
        } else if (property == editable) {
            display.setEditable(editable.getValue());
        }
    }

    private abstract class Display {

        JPanel panel = new JPanel();

        public Display() {
            panel.setOpaque(false);

            add(panel);
        }

        abstract void setEditable(boolean editable);

        abstract void setValue(NetworkTable table);

        abstract void setSelected(String selected);
    }

    private class RadioButtons extends Display implements ActionListener {

        JPanel groupPanel;
        ButtonGroup group;
        JRadioButton selected;
        Map<String, JRadioButton> buttons;

        @Override
        void setEditable(boolean editable) {
            for (JRadioButton button : buttons.values()) {
                button.setEnabled(editable);
            }
        }

        @Override
        void setValue(NetworkTable table) {
            if (groupPanel != null) {
                panel.remove(groupPanel);
                for (JRadioButton button : buttons.values()) {
                    group.remove(button);
                }
                buttons.clear();
            }

            groupPanel = new JPanel();
            groupPanel.setOpaque(false);
            groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));

            group = new ButtonGroup();

            buttons = new HashMap<String, JRadioButton>();

            synchronized (table) {
                int values = table.getInt(COUNT);

                boolean hasSelection = false;

                for (int i = 0; i < values; i++) {
                    String choice = table.getString(String.valueOf(i));
                    hasSelection |= choice.equals(selection);

                    JRadioButton button = new JRadioButton(choice);
                    buttons.put(choice, button);
                    group.add(button);
                    groupPanel.add(button);
                    button.setActionCommand(choice);
                    button.addActionListener(this);
                }

                if (!hasSelection) {
                    selection = null;
                }

                if (table.containsKey(SELECTED)) {
                    selection = table.getString(SELECTED);
                }

                if (selection != null) {
                    table.putString(SELECTED, selection);
                    selected = buttons.get(selection);
                    selected.setSelected(true);
                } else if (table.containsKey(DEFAULT)) {
                    selected = buttons.get(table.getString(DEFAULT));
                    selected.setSelected(true);
                } else {
                    selected = null;
                }
            }

            setEnabled(editable.getValue());

            panel.add(groupPanel);

            revalidate();
            repaint();

            setSize(getPreferredSize());
        }

        @Override
        void setSelected(String selected) {
            buttons.get(selected).setSelected(true);
        }

        public void actionPerformed(ActionEvent e) {
            String userChoice = e.getActionCommand();
            if (selection == null || !selection.equals(userChoice)) {
                selection = userChoice;
                table.putString(SELECTED, selection);
            }
        }
    }

    private class ComboBox extends Display implements ItemListener {

        JComboBox combo;

        @Override
        void setEditable(boolean editable) {
            if (combo != null) {
                combo.setEnabled(editable);
            }
        }

        @Override
        void setValue(NetworkTable table) {
            if (combo != null) {
                panel.remove(combo);
                combo.removeItemListener(this);
            }

            combo = new JComboBox();

            synchronized (table) {
                int values = table.getInt(COUNT);

                boolean hasSelection = false;

                for (int i = 0; i < values; i++) {
                    String choice = table.getString(String.valueOf(i));
                    hasSelection |= choice.equals(selection);
                    combo.addItem(choice);
                }

                if (!hasSelection) {
                    selection = null;
                }

                if (table.containsKey(SELECTED)) {
                    selection = table.getString(SELECTED);
                }

                if (selection != null) {
                    combo.setSelectedItem(selection);
                    table.putString(SELECTED, selection);
                } else if (table.containsKey(DEFAULT)) {
                    combo.setSelectedItem(table.getString(DEFAULT));
                }
            }

            panel.add(combo);

            combo.addItemListener(this);

            combo.setEnabled(editable.getValue());

            revalidate();
            repaint();

            setSize(getPreferredSize());
        }

        @Override
        void setSelected(String selected) {
            if (combo != null) {
                combo.setSelectedItem(selected);
            }
        }

        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String userChoice = (String) e.getItem();
                if (!userChoice.equals(selection)) {
                    selection = userChoice;
                    table.putString(SELECTED, selection);
                }
            }
        }
    }
}
