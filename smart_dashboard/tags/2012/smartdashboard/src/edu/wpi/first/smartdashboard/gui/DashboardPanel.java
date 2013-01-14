package edu.wpi.first.smartdashboard.gui;

import edu.wpi.first.smartdashboard.robot.Robot;
import edu.wpi.first.smartdashboard.types.DataType;
import edu.wpi.first.smartdashboard.types.DisplayElementRegistry;
import edu.wpi.first.smartdashboard.types.NamedDataType;
import edu.wpi.first.wpilibj.networking.NetworkAdditionListener;
import edu.wpi.first.wpilibj.networking.NetworkListener;
import edu.wpi.first.wpilibj.networking.NetworkTable;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * This is the main panel, it sits within the {@link DashboardFrame} and contains everything the user
 * sees except for the window outline.  This class is the workhorse of the GUI.
 * Inside here is where the logic is contained for how to respond to new fields
 * and various other things.
 * @author Joe Grinstead
 */
public class DashboardPanel extends JPanel {

    /** We use a glass pane technique for editable mode */
    private GlassPane glassPane = new GlassPane();
    /** This panel contains everything except the glass pane */
    private JPanel backPane = new JPanel();
    /** All the elements currently being displayed */
    private LinkedList<DisplayElement> elements = new LinkedList<DisplayElement>();
    /** All the fields that are currently being displayed */
    private Map<String, Widget> fields = new HashMap<String, Widget>();
    /** All the fields which are hidden (they have no widget) */
    private Set<String> hiddenFields = new HashSet<String>();
    /** Whether or not this is editable */
    private boolean editable = false;
    /** The listener which connects the panel to keep up to date on the robot */
    private RobotListener listener;

    /** 
     * Instantiates the panel
     */
    public DashboardPanel() {
        add(glassPane);
        add(backPane);

        backPane.setLayout(new DashboardLayout());
        backPane.setFocusable(true);

        setLayout(new DashboardLayout());

        setEditable(editable);
    }

    @Override
    public synchronized void addMouseListener(MouseListener l) {
        glassPane.addMouseListener(l);
        backPane.addMouseListener(l);
    }

    @Override
    public synchronized void addMouseMotionListener(MouseMotionListener l) {
        glassPane.addMouseMotionListener(l);
        backPane.addMouseMotionListener(l);
    }

    /**
     * Alerts the panel that it should begin to listen to the robot.
     */
    public void beginListening() {
        if (listener == null) {
            listener = new RobotListener();
            Robot.getTable().addAdditionListener(listener, true);
            Robot.getTable().addListenerToAll(listener);
        }
    }

    /**
     * Revalidates the content behind the glass pane
     */
    public void revalidateBacking() {
        backPane.revalidate();
    }

    /**
     * Sets whether or not this panel is editable.  If the panel becomes
     * editable, then it will pull the focus from the widgets.
     * @param editable whether or not the pane should be editable
     */
    public void setEditable(boolean editable) {
        this.editable = editable;

        glassPane.setVisible(editable);
        if (editable) {
            glassPane.requestFocus();
        }
    }

    /**
     * Returns whether or not this panel is editable.
     * @return whether or not this panel is editable
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * Returns the names of all the fields marked as hidden (they are the ones in the
     * {@link NetworkTable} that have been explicitly declared to be ignored by
     * the user).
     * @return the hidden fields
     */
    public Iterable<String> getHiddenFields() {
        return hiddenFields;
    }

    /**
     * Returns all the {@link DisplayElement DisplayElements} that are in this panel.
     * @return all the elements
     */
    public Iterable<DisplayElement> getElements() {
        return elements;
    }

    /**
     * Takes the given value and manipulates it based on the given type to be
     * able to be handed to a {@link Widget}.
     * Basically, if the type is a primitive, the value will be returned.
     * If the type is a {@link NamedDataType}, then the data subtable will be
     * returned.
     * If either the type of the value is {@code null}, then {@code null} will
     * be returned.
     * @param type the type of the data
     * @param value the data
     * @return the data to be handed to a {@link Widget}
     */
    private Object verifyValue(DataType type, Object value) {
        if (type == null || value == null) {
            return null;
        } else if (type instanceof NamedDataType) {
            return ((NetworkTable) value).getSubTable("Data");
        } else {
            return value;
        }
    }

    /**
     * Clears the panel of all of its data, and then reload it.
     * This basically starts the panel over from the beginning.
     */
    public void clear() {
        hiddenFields.clear();
        fields.clear();
        for (DisplayElement element : elements) {
            element.disconnect();
            backPane.remove(element);
        }
        elements.clear();

        Robot.getTable().addAdditionListener(listener, true);

        repaint();
    }

    /**
     * Removes all fields which do not have values in the
     */
    public void removeUnusedFields() {
        ArrayList<String> unused = new ArrayList<String>();
        for (String field : fields.keySet()) {
            if (!Robot.getTable().containsKey(field)) {
                unused.add(field);
            }
        }
        for (String field : unused) {
            removeField(field);
            hiddenFields.remove(field);
        }
    }

    public void removeField(String field) {
        Widget elem = fields.get(field);
        hiddenFields.add(field);
        if (elem != null) {
            elem.disconnect();
            backPane.remove(elem);
            fields.remove(field);
            elements.remove(elem);
            repaint(elem.getBounds());
        }
    }

    public void removeElement(StaticWidget widget) {
        widget.disconnect();
        backPane.remove(widget);
        elements.remove(widget);
        repaint(widget.getBounds());
    }

    public void shiftToBack(DisplayElement element) {
        int count = 0;

        elements.remove(element);

        for (DisplayElement e : elements) {
            backPane.setComponentZOrder(e, count++);
        }
        backPane.setComponentZOrder(element, count);

        elements.add(element);

        repaint();
    }

    public void addElement(DisplayElement element, Point point) {
        element.init();

        if (point == null) {
            Dimension saved = element.getSavedSize();
            Dimension preferred = element.getPreferredSize();
            if (saved.width > 0) {
                preferred.width = saved.width;
            }
            if (saved.height > 0) {
                preferred.height = saved.height;
            }
            element.setSize(preferred);
            point = findSpace(element);
            element.setBounds(new Rectangle(point, preferred));
        }
        element.setSavedLocation(point);

        backPane.add(element);

        int count = 1;

        for (DisplayElement e : elements) {
            backPane.setComponentZOrder(e, count++);
        }
        backPane.setComponentZOrder(element, 0);

        elements.addFirst(element);

        revalidate();
        repaint();
    }

    public void setField(String key, Widget element, DataType type, Object value, Point point) {
        removeField(key);

        hiddenFields.remove(key);

        value = verifyValue(type, value);

        element.setFieldName(key);
        if (type != null) {
            element.setType(type);
        }

        fields.put(key, element);

        addElement(element, point);

        if (value != null) {
            element.setValue(value);
        }
    }

    public void addField(String key) {
        setField(key, null, Robot.getTable().containsKey(key) ? Robot.getTable().getValue(key) : null, null);
    }

    public void setField(String key, Class<? extends Widget> preferred, Object value, Point point) {
        setField(key, preferred, DataType.getType(value), value, point);
    }

    public void setField(String key, Class<? extends Widget> preferred, final DataType type, Object value, Point point) {
        Widget element = fields.get(key);

        if (type == null) {
            System.out.println("WARNING: has no way of handling data at field \"" + key + "\"");
            removeField(key);
        } else if (element != null && preferred == null && (element.getType() == type || element.supportsType(type))) {
            if (element.getType() != type) {
                element.setType(type);
            }

            value = verifyValue(type, value);
            if (value != null) {
                element.setValue(value);
            }
        } else {
            Class<? extends Widget> clazz = preferred == null ? type.getDefault() : preferred;

            if (clazz == null) {
                Set<Class<? extends Widget>> candidates = DisplayElementRegistry.getWidgetsForType(type);

                if (candidates.isEmpty()) {
                    System.out.println("WARNING: has no way of handling type " + type);
                    return;
                } else {
                    clazz = (Class<? extends Widget>) candidates.toArray()[0];
                }
            }

            try {
                element = clazz.newInstance();

                setField(key, element, type, value, point);
            } catch (InstantiationException ex) {
                System.out.println("ERROR: " + clazz.getName() + " has no default constructor!");
            } catch (IllegalAccessException ex) {
                System.out.println("ERROR: " + clazz.getName() + " has no public default constructor!");
            }
        }
    }

    public DisplayElement findElementContaining(Point point) {
        for (DisplayElement element : elements) {
            if (element.getBounds().contains(point)) {
                return element;
            }
        }
        return null;
    }
    private static final Random random = new Random();

    private Point findSpace(DisplayElement toPlace) {
        Stack<Point> positions = new Stack<Point>();
        positions.add(new Point(0, 0));

        Dimension size = toPlace.getPreferredSize();
        Dimension panelBounds = getSize();

        PositionLoop:
        while (!positions.isEmpty()) {
            Point position = positions.pop();
            Rectangle area = new Rectangle(position, size);

            if (area.x < 0 || area.y < 0 || area.x + area.width > panelBounds.width || area.y + area.height > panelBounds.height) {
                continue;
            }

            for (DisplayElement element : elements) {
                if (element != toPlace && element.isObstruction()) {
                    Rectangle bounds = element.getBounds();
                    // Test Intersection
                    if (!(bounds.x > area.x + area.width
                            || bounds.x + bounds.width < area.x
                            || bounds.y > area.y + area.height
                            || bounds.y + bounds.height < area.y)) {
                        Point right = new Point(bounds.x + bounds.width + 1, position.y);
                        if (positions.isEmpty()) {
                            positions.add(right);
                            right = null;
                        }
                        positions.add(new Point(position.x, bounds.y + bounds.height + 1));
                        if (right != null && Math.abs(right.x - area.x) < area.width / 3) {
                            positions.add(right);
                        }
                        continue PositionLoop;
                    }
                }
            }

            return position;
        }

        // If no space was found, jumble them at the beginning
        return new Point(random.nextInt(32), random.nextInt(32));
    }

    private class RobotListener implements NetworkListener, NetworkAdditionListener {

        public void valueChanged(final String key, final Object value) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    if (!hiddenFields.contains(key)) {
                        setField(key, null, value, null);
                    }
                }
            });
        }

        public void valueConfirmed(String key, Object value) {
        }

        public void fieldAdded(String name, Object value) {
            if (DashboardPrefs.getInstance().autoShowWidgets.getValue() || fields.containsKey(name)) {
                valueChanged(name, value);
            } else {
                hiddenFields.add(name);
            }
        }
    }

    private class DashboardLayout implements LayoutManager {

        public void addLayoutComponent(String name, Component comp) {
        }

        public void removeLayoutComponent(Component comp) {
        }

        public Dimension preferredLayoutSize(Container parent) {
            return new Dimension(640, 480); // Not going to be used
        }

        public Dimension minimumLayoutSize(Container parent) {
            return new Dimension(0, 0); // Not going to be used
        }

        public void layoutContainer(Container parent) {
            if (parent == DashboardPanel.this) {
                Dimension size = getSize();
                glassPane.setBounds(0, 0, size.width, size.height);
                backPane.setBounds(0, 0, size.width, size.height);
            } else { // Back Pane
                for (DisplayElement element : elements) {
                    element.setLocation(element.getSavedLocation());

                    Dimension savedSize = element.getSavedSize();
                    Dimension preferredSize = element.getPreferredSize();
                    Dimension size = new Dimension(preferredSize);
                    if (savedSize != null && savedSize.width != -1) {
                        size.width = savedSize.width;
                    }
                    if (savedSize != null && savedSize.height != -1) {
                        size.height = savedSize.height;
                    }

                    element.setSize(size);
                }
            }
        }
    }
}
