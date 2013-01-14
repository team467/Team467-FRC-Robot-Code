package edu.wpi.first.smartdashboard.gui;

import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.properties.PropertyHolder;
import edu.wpi.first.smartdashboard.types.DataType;
import java.awt.Dimension;
import java.awt.Point;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JPanel;

/**
 *
 * @author pmalmsten
 */
public abstract class DisplayElement extends JPanel implements PropertyHolder {

    private Point savedLocation = new Point(0, 0);
    private Dimension savedDimension = new Dimension(-1, -1);
    private Map<String, Property> properties = new LinkedHashMap<String, Property>();
    private boolean resizable = true;
    private boolean obstruction = true;

    public DisplayElement() {
        setOpaque(false);
    }

    /**
     * Sets up and displays any internal subcomponents managed by this UI element.
     *
     * This will be called from within the GUI thread, so don't worry about
     * running things in the EventQueue. A DisplayElement should be fully drawn
     * on the screen and ready to receive data when this method returns.
     */
    public abstract void init();

    public void disconnect() {
    }

    public boolean validatePropertyChange(Property property, Object value) {
        return true;
    }

    public boolean isObstruction() {
        return obstruction;
    }

    public void setObstruction(boolean obstruction) {
        this.obstruction = obstruction;
    }

    public abstract void propertyChanged(Property property);

    public Map<String, Property> getProperties() {
        return properties;
    }

    protected void setDefault(String propertyName, String propertyType, Object value) {
        throw new UnsupportedOperationException("Deprecated");
    }

    protected Object getPropertyValue(String name) {
        return properties.get(name).getValue();
    }

    public Point getSavedLocation() {
        return new Point(savedLocation);
    }

    public Dimension getSavedSize() {
        return new Dimension(savedDimension);
    }

    public void setSavedLocation(Point p) {
        savedLocation = p;
    }

    public void setSavedSize(Dimension d) {
        savedDimension = new Dimension(d);
    }

    public boolean isResizable() {
        return resizable;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    protected void update(Property property, Object defaultValue) {
        if (property.hasDefault()) {
            propertyChanged(property);
        } else if (property.hasValue()) {
            property.setDefault(defaultValue);
            propertyChanged(property);
        } else {
            property.setDefault(defaultValue);
        }
    }

    public static String getName(Class<? extends DisplayElement> clazz) {
        try {
            Field field = clazz.getDeclaredField("NAME");
            int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers)) {
                throw new RuntimeException("TYPES must be static");
            } else if (!Modifier.isFinal(modifiers)) {
                throw new RuntimeException("TYPES must be final");
            }
            if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
                return (String) field.get(null);
            }
        } catch (Exception e) {
        }

        return clazz.getSimpleName();
    }
}
