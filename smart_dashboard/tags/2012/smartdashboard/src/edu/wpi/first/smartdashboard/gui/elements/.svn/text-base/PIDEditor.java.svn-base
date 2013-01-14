package edu.wpi.first.smartdashboard.gui.elements;

import edu.wpi.first.smartdashboard.gui.Widget;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.types.DataType;
import edu.wpi.first.smartdashboard.types.named.PIDType;
import edu.wpi.first.wpilibj.networking.NetworkListener;
import edu.wpi.first.wpilibj.networking.NetworkTable;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author Joe Grinstead
 */
public class PIDEditor extends Widget {

    public static final DataType[] TYPES = {PIDType.get()};

    private NetworkTable table;
    private JTextField pField;
    private JTextField iField;
    private JTextField dField;
    private JTextField sField;
    private JCheckBox eBox;
    private JLabel pLabel;
    private JLabel iLabel;
    private JLabel dLabel;
    private JLabel sLabel;
    private JLabel eLabel;
    private Double pValue;
    private Double iValue;
    private Double dValue;
    private Double sValue;
    private Boolean eValue;
    private NetworkListener listener = new NetworkListener() {

        public void valueChanged(String key, Object value) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    pValue = table.getDouble("p");
                    iValue = table.getDouble("i");
                    dValue = table.getDouble("d");
                    sValue = table.getDouble("setpoint");
                    eValue = table.getBoolean("enabled");

                    pField.setText(pValue.toString());
                    iField.setText(iValue.toString());
                    dField.setText(dValue.toString());
                    sField.setText(sValue.toString());
                    eBox.setSelected(eValue);
                }
            });
        }

        public void valueConfirmed(String key, Object value) {
        }
    };

    @Override
    public void setValue(Object value) {
        if (table != null) {
            table.removeListenerFromAll(listener);
        }
        table = (NetworkTable) value;
        table.addListenerToAll(listener);

        listener.valueChanged(null, null);

    }

    @Override
    public void init() {
        pValue = 0.0;
        iValue = 0.0;
        dValue = 0.0;
        sValue = 0.0;
        eValue = false;

        setLayout(new PIDLayout());

        pLabel = new JLabel("P:");
        iLabel = new JLabel("I:");
        dLabel = new JLabel("D:");
        sLabel = new JLabel("Setpoint:");
        eLabel = new JLabel("Enabled:");
        pLabel.setHorizontalAlignment(JLabel.RIGHT);
        iLabel.setHorizontalAlignment(JLabel.RIGHT);
        dLabel.setHorizontalAlignment(JLabel.RIGHT);
        sLabel.setHorizontalAlignment(JLabel.RIGHT);
        eLabel.setHorizontalAlignment(JLabel.RIGHT);

        pField = new JTextField(pValue.toString());
        pField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    double value = Double.parseDouble(pField.getText());

                    if (value != pValue) {
                        table.putDouble("p", value);
                        pValue = value;
                    }
                    requestFocus();
                } catch (NumberFormatException ex) {
                    pField.setText(pValue.toString());
                }
            }
        });
        iField = new JTextField(iValue.toString());
        iField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    double value = Double.parseDouble(iField.getText());

                    if (value != iValue) {
                        table.putDouble("i", value);
                        iValue = value;
                    }
                    requestFocus();
                } catch (NumberFormatException ex) {
                    iField.setText(iValue.toString());
                }
            }
        });
        dField = new JTextField(dValue.toString());
        dField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    double value = Double.parseDouble(dField.getText());

                    if (value != dValue) {
                        table.putDouble("d", value);
                        dValue = value;
                    }
                    requestFocus();
                } catch (NumberFormatException ex) {
                    dField.setText(dValue.toString());
                }
            }
        });
        sField = new JTextField(sValue.toString());
        sField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    double value = Double.parseDouble(sField.getText());

                    if (value != sValue) {
                        table.putDouble("setpoint", value);
                        sValue = value;
                    }
                    requestFocus();
                } catch (NumberFormatException ex) {
                    sField.setText(sValue.toString());
                }
            }
        });
        eBox = new JCheckBox();
        eBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                boolean value = eBox.isSelected();
                if (value != eValue) {
                }
                table.putBoolean("enabled", value);
                eValue = value;

                pField.setEnabled(!value);
                iField.setEnabled(!value);
                dField.setEnabled(!value);

            }
        });
        eBox.setSelected(eValue);

        int columns = 10;
        pField.setColumns(columns);
        iField.setColumns(columns);
        dField.setColumns(columns);
        sField.setColumns(columns);

        add(pLabel);
        add(iLabel);
        add(dLabel);
        add(sLabel);
        add(eLabel);
        add(pField);
        add(iField);
        add(dField);
        add(sField);
        add(eBox);

        setMaximumSize(new Dimension(Integer.MAX_VALUE, getPreferredSize().height));

        revalidate();
        repaint();
    }

    @Override
    public void propertyChanged(Property property) {
    }

    private class PIDLayout implements LayoutManager {

        public void addLayoutComponent(String name, Component comp) {
        }

        public void removeLayoutComponent(Component comp) {
        }

        public Dimension preferredLayoutSize(Container parent) {
            int width = eLabel.getPreferredSize().width + sField.getPreferredSize().width;
            int height = 5 * Math.max(sLabel.getPreferredSize().height, sField.getPreferredSize().height);
            //+ Math.max(eLabel.getPreferredSize().height, eBox.getPreferredSize().height);

            return new Dimension(width, height);
        }

        public Dimension minimumLayoutSize(Container parent) {
            int width = eLabel.getMinimumSize().width + Math.max(sField.getMinimumSize().width, eBox.getMinimumSize().width);
            int height = 5 * Math.max(sLabel.getMinimumSize().height, sField.getMinimumSize().height);
            // + Math.max(eLabel.getMinimumSize().height, eBox.getMinimumSize().height);

            return new Dimension(width, height);
        }

        public void layoutContainer(Container parent) {
            int height = Math.max(eLabel.getPreferredSize().height, sField.getPreferredSize().height);
            int labelWidth = eLabel.getPreferredSize().width;
            int fieldWidth = getWidth() - labelWidth;
            JLabel[] labels = {pLabel, iLabel, dLabel, sLabel, eLabel};
            for (int i = 0; i < labels.length; i++) {
                JLabel label = labels[i];

                label.setSize(labelWidth, height);
                label.setLocation(0, i * height);

            }
            JTextField[] fields = {pField, iField, dField, sField};
            for (int i = 0; i < fields.length; i++) {
                JTextField field = fields[i];

                field.setSize(fieldWidth, height);
                field.setLocation(labelWidth, i * height);
            }
            eBox.setSize(eBox.getPreferredSize().width, height);
            eBox.setLocation(labelWidth, 4 * height);
        }
    }
}
