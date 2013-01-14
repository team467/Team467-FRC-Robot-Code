package edu.wpi.first.smartdashboard.gui.elements;

import edu.wpi.first.smartdashboard.gui.Widget;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.types.DataType;
import edu.wpi.first.smartdashboard.types.named.SchedulerType;
import edu.wpi.first.wpilibj.networking.NetworkListener;
import edu.wpi.first.wpilibj.networking.NetworkTable;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Jeff Copeland
 */
public class Scheduler extends Widget {

    public static final DataType[] TYPES = {SchedulerType.get()};
    private static final String NO_COMMAND_CARD = "No Command";
    private static final String COMMAND_CARD = "Commands";
    private int count;
    private JLabel noCommands;
    private JPanel commandLabels;
    private JPanel cancelButtons;
    private JPanel commandPanel;
    private List<JLabel> labels;
    private List<JButton> buttons;
    private NetworkTable table;
    private GridLayout commandLayout;
    private GridLayout cancelLayout;
    private CardLayout cardLayout;
    private NetworkListener listener = new NetworkListener() {

        boolean running = false;

        public void valueChanged(String key, Object value) {
            if (running) {
                return;
            }
            running = true;
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    int newCount = 0;
                    synchronized (table) {
                        newCount = table.getInt("count");
                        for (int i = 0; i < newCount; i++) {
                            final NetworkTable command = table.getSubTable(String.valueOf(i + 1));

                            if (i >= labels.size()) {
                                labels.add(new JLabel());
                            }
                            JLabel label = labels.get(i);
                            label.setText(command.getString("name"));

                            if (i >= buttons.size()) {
                                JButton button = new JButton("cancel");
                                final int index = i + 1;
                                button.addActionListener(new ActionListener() {

                                    public void actionPerformed(ActionEvent e) {
                                        table.getSubTable(Integer.toString(index)).putBoolean("running", false);
                                    }
                                });
                                buttons.add(button);
                            }
                            JButton button = buttons.get(i);

                            if (i > count - 1) {
                                commandLabels.add(label);
                                cancelButtons.add(button);
                            }
                        }
                    }

                    if (count > newCount) {
                        for (int i = newCount; i < count; i++) {
                            commandLabels.remove(labels.get(i));
                            cancelButtons.remove(buttons.get(i));
                        }
                    }

                    count = newCount;

                    cardLayout.show(Scheduler.this, count == 0 ? NO_COMMAND_CARD : COMMAND_CARD);

                    running = false;
                }
            });
        }

        public void valueConfirmed(String key, Object value) {
            valueChanged(key, value);
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
        revalidate();
        repaint();
    }

    @Override
    public void init() {
        setLayout(cardLayout = new CardLayout());

        labels = new ArrayList<JLabel>();
        buttons = new ArrayList<JButton>();

        commandPanel = new JPanel();
        commandPanel.setLayout(new GridLayout(0, 2));

        commandLabels = new JPanel();
        cancelButtons = new JPanel();

        commandPanel.add(commandLabels, BorderLayout.WEST);
        commandPanel.add(cancelButtons, BorderLayout.CENTER);

        commandLayout = new GridLayout(0, 1);
        cancelLayout = new GridLayout(0, 1);

        commandLabels.setLayout(commandLayout);
        cancelButtons.setLayout(cancelLayout);

        add(commandPanel, COMMAND_CARD);

        noCommands = new JLabel("No commands running.");
        noCommands.setHorizontalAlignment(JLabel.CENTER);
        add(noCommands, NO_COMMAND_CARD);

        cardLayout.show(this, NO_COMMAND_CARD);

        repaint();
    }

    @Override
    public void propertyChanged(Property property) {
    }
}
