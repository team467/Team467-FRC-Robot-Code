package edu.wpi.first.smartdashboard.util;

import edu.wpi.first.smartdashboard.gui.MainWindow;
import edu.wpi.first.smartdashboard.gui.layout.LayoutAllocator;
import edu.wpi.first.smartdashboard.gui.layout.LayoutAllocator.LayoutAllocation;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.HashMap;
import javax.swing.JPanel;

/**
 *
 * @author pmalmsten
 */
public abstract class DisplayElement extends JPanel {
    private LayoutAllocation m_allocation;

    public DisplayElement() {
        properties = new HashMap<String, Object>();

        final DisplayElement thisDisplayElement = this;
        this.addComponentListener(new ComponentListener() {

            // Whenever a component is resized
            public void componentResized(ComponentEvent e) {
                if(thisDisplayElement.getLayoutAllocation() != null) {
                    LayoutAllocation oldLa = thisDisplayElement.getLayoutAllocation();
                    oldLa.deallocate();

                    // Try to re-allocate the same location with the new size
                    LayoutAllocation newLa = LayoutAllocator.allocate(thisDisplayElement.getLocation(),
                                                                      thisDisplayElement.getWidth(),
                                                                      thisDisplayElement.getHeight());

                    if(newLa == null) {
                        // Try to get a completely new allocation
                        newLa = LayoutAllocator.allocate(thisDisplayElement.getWidth(),
                                                         thisDisplayElement.getHeight());
                    }

                    if(newLa != null) {
                        thisDisplayElement.setLayoutAllocation(newLa);
                        thisDisplayElement.setLocation(newLa.point);
                    } else {
                        thisDisplayElement.disconnect();
                        thisDisplayElement.getParent().remove(thisDisplayElement);
                    }
                    
                    MainWindow.getInstance().redrawDisplayElements();
                }
            }

            public void componentMoved(ComponentEvent e) {
            }

            public void componentShown(ComponentEvent e) {
            }

            public void componentHidden(ComponentEvent e) {
            }
        });
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

    /**
     * Make a change to a property for an object
     * @param key The name of the property being changed
     * @param value The value of the property
     * @return true if the change is OK and should happen
     */
    public abstract boolean propertyChange(String key, Object value);

    /**
     * Get the list of key names for the key table editor
     * @return An array of names of keys
     */
    public String[] getPropertiesKeys() {
	int i = 0;
	String[] returnValue = new String[properties.size()];
	for (String key: properties.keySet())
	    returnValue[i++] = key;
	return returnValue;
    }

    /**
     * Get the value of a particular property.
     * @param key The string name of the key
     * @return the value of this key
     */
    public abstract Object getPropertyValue(String key);

    protected void setProperty(String name, Object value) {
	properties.put(name, value);
    }

    public void setLayoutAllocation(LayoutAllocation alloc) {
        m_allocation = alloc;
    }

    public LayoutAllocation getLayoutAllocation() {
        return m_allocation;
    }

    protected HashMap<String, Object> properties;
}
