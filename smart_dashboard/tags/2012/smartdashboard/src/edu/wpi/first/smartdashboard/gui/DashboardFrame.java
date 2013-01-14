package edu.wpi.first.smartdashboard.gui;

import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.robot.Robot;
import edu.wpi.first.smartdashboard.types.DataType;
import edu.wpi.first.smartdashboard.types.DisplayElementRegistry;
import edu.wpi.first.smartdashboard.xml.SmartDashboardXMLReader;
import edu.wpi.first.smartdashboard.xml.SmartDashboardXMLWriter;
import edu.wpi.first.smartdashboard.xml.XMLWidget;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

/**
 * This class defines the main window for the FRC program.  It contains
 * almost no logic except for the {@link DashboardFrame#load(java.lang.String) load(...)}
 * and {@link DashboardFrame#save(java.lang.String) save(...)} method.
 * @author Joe Grinstead
 */
public class DashboardFrame extends JFrame {

    /** The singleton instance */
    private static DashboardFrame instance;

    /**
     * Initializes the singleton instance.  Only performs logic on its first call.
     * @param competition whether or not the frame should display as though
     * it were on the FIRST provided netbook
     */
    public static void initialize(final boolean competition) {
        if (instance == null) {
            instance = new DashboardFrame(competition);
        }
    }

    /**
     * Returns the instance, creating it (and setting it to not be in competition)
     * if it does not already exist.
     * @return the instance
     */
    public static DashboardFrame getInstance() {
        initialize(false);
        return instance;
    }

    /*
     * If the menu bar is set to "hidden," then this defines what portion of the
     * top screen is reserved for revealing the menu bar when the mouse moves over
     * it
     */
    private static final int MENU_HEADER = 10;
    /** The size the frame should be when displayed on the netbook */
    private static final Dimension NETBOOK_SIZE = new Dimension(1024, 400);
    /** The minimum size of the frame */
    private static final Dimension MINIMUM_SIZE = new Dimension(300, 200);
    /** The content of the frame */
    private DashboardPanel panel;
    /** The menu bar */
    private JMenuBar menuBar;
    /** The property table (there is only this one ever) */
    private PropertyEditor propEditor = null;
    /** Whether or not the menu bar should be hidden */
    private boolean shouldHideMenu = DashboardPrefs.getInstance().hideMenu.getValue();

    /**
     * Initializes the frame.
     * @param competition whether or not to display as though it were on the netbook
     */
    private DashboardFrame(boolean competition) {
        super("SmartDashboard - " + DashboardPrefs.getInstance().team.getValue());

        setLayout(new BorderLayout());

        // The content of the frame is really contained in the panel and menu
        panel = new DashboardPanel();
        menuBar = new DashboardMenu(panel);

        if (!shouldHideMenu) {
            add(menuBar, BorderLayout.NORTH);
        }
        add(panel, BorderLayout.CENTER);

        // Look for when the menu bar should be displayed
        MouseAdapter hideListener = new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                if (shouldHideMenu && e.getY() < MENU_HEADER) {
                    add(menuBar, BorderLayout.NORTH);
                    validate();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (shouldHideMenu) {
                    remove(menuBar);
                    validate();
                }
            }
        };
        panel.addMouseListener(hideListener);
        panel.addMouseMotionListener(hideListener);

        // Set the size / look
        if (competition) {
            setPreferredSize(NETBOOK_SIZE);
            setUndecorated(true);
            setLocation(0, 0);
            setResizable(false);
        } else {
            setMinimumSize(MINIMUM_SIZE);

            // Closing operation is handled manually
            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

            DashboardPrefs prefs = DashboardPrefs.getInstance();
            setPreferredSize(new Dimension(prefs.width.getValue(), prefs.height.getValue()));
            setLocation(prefs.x.getValue(), prefs.y.getValue());
        }

        // Call our own exit function when the frame is closed
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });

        // Make resizing affect the preference variables for window size
        addComponentListener(new ComponentListener() {

            public void componentResized(ComponentEvent e) {
                DashboardPrefs.getInstance().width.setValue(getWidth());
                DashboardPrefs.getInstance().height.setValue(getHeight());
            }

            public void componentMoved(ComponentEvent e) {
                DashboardPrefs.getInstance().x.setValue(getX());
                DashboardPrefs.getInstance().y.setValue(getY());
            }

            public void componentShown(ComponentEvent e) {
            }

            public void componentHidden(ComponentEvent e) {
            }
        });
    }

    /**
     * Returns the panel that contains all the widgets
     * @return the panel that contains all the widgets
     */
    public DashboardPanel getPanel() {
        return panel;
    }

    /**
     * Returns the property editor
     * @return the property editor
     */
    public PropertyEditor getPropertyEditor() {
        if (propEditor == null) {
            propEditor = new PropertyEditor(this);
        }
        return propEditor;
    }

    /**
     * Sets whether or not the menu should be hidden.
     * This does not attempt to change the property setting, instead
     * the property setting should call this.
     * @param shouldHide whether or not the menu should hide
     */
    public void setShouldHideMenu(boolean shouldHide) {
        if (shouldHideMenu != shouldHide) {
            shouldHideMenu = shouldHide;
            if (shouldHideMenu) {
                remove(menuBar);
            } else {
                add(menuBar, BorderLayout.NORTH);
            }
            validate();
        }
    }

    /**
     * Saves the frame to the file of the given name
     * @param path the path to save the file to
     */
    public void save(String path) {
        try {
            SmartDashboardXMLWriter writer = new SmartDashboardXMLWriter(path);

            for (DisplayElement element : panel.getElements()) {
                boolean isWidget = element instanceof Widget;
                assert isWidget || element instanceof StaticWidget;
                if (isWidget) {
                    writer.beginWidget(((Widget) element).getFieldName(), ((Widget) element).getType().getName(), element.getClass().getName());
                } else {
                    writer.beginStaticWidget(element.getClass().getName());
                }
                writer.addLocation(element.getLocation());

                Dimension size = element.getSavedSize();
                if (size.width > 0) {
                    writer.addWidth(size.width);
                }
                if (size.height > 0) {
                    writer.addHeight(size.height);
                }

                for (Map.Entry<String, Property> prop : element.getProperties().entrySet()) {
                    if (!prop.getValue().isDefault()) {
                        writer.addProperty(prop.getKey(), prop.getValue().getSaveValue());
                    }
                }

                if (isWidget) {
                    writer.endWidget();
                } else {
                    writer.endStaticWidget();
                }
            }

            for (String field : panel.getHiddenFields()) {
                writer.addHiddenField(field);
            }

            writer.close();
        } catch (IOException ex) {
        }
    }

    /**
     * Loads the current state of this MainWindow and any significant objects
     * it contains
     */
    public void load(String path) {
        try {
            SmartDashboardXMLReader reader = new SmartDashboardXMLReader(path);

            List<XMLWidget> widgets = reader.getXMLWidgets();
            for (int i = widgets.size(); i > 0; i--) {
                XMLWidget widget = widgets.get(i - 1);
                DisplayElement element = widget.convertToDisplayElement();
                if (element instanceof Widget) {
                    Widget e = (Widget) element;
                    Object value = null;
                    if (Robot.getTable().containsKey(e.getFieldName())) {
                        value = Robot.getTable().getValue(e.getFieldName());
                        DataType type = DataType.getType(value);
                        if (DisplayElementRegistry.supportsType(e.getClass(), type)) {
                            panel.setField(e.getFieldName(), e, type, value, e.getSavedLocation());
                        }
                    } else {
                        panel.setField(e.getFieldName(), e, widget.getType(), null, e.getSavedLocation());
                    }
                } else if (element instanceof StaticWidget) {
                    StaticWidget e = (StaticWidget) element;
                    panel.addElement(e, widget.getLocation());
                } else {
                    // TODO tell the user it was null
                }
            }

            for (String field : reader.getHiddenFields()) {
                panel.removeField(field);
            }

            Map<String, Property> prefs = DashboardPrefs.getInstance().getProperties();
            for (Map.Entry<String, String> entry : reader.getProperties().entrySet()) {
                Property prop = prefs.get(entry.getKey());
                prop.setValue(entry.getValue());
            }

            repaint();
        } catch (FileNotFoundException e) {
            // TODO tell the user
        }
    }

    /**
     * Exits the program, prompting the user to save.
     */
    public void exit() {
        int result = JOptionPane.showConfirmDialog(
                this,
                new String[]{"Do you wish to save this layout?"},
                "Save before quiting?",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE);
        switch (result) {
            case JOptionPane.YES_OPTION:
                save(DashboardPrefs.getInstance().saveFile.getValue());
            case JOptionPane.NO_OPTION:
                System.exit(0);
            default: // Do Nothing (they called cancel)
        }
    }
}
