package edu.wpi.first.smartdashboard.state;

import edu.wpi.first.smartdashboard.types.Types;
import edu.wpi.first.smartdashboard.types.Types.Type;
import edu.wpi.first.smartdashboard.util.IStateUpdatable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a field sent by a robot
 */
public class Record implements java.io.Serializable {

    private final String m_name;
    private int m_id;
    private final Types.Type m_type;
    private transient Object m_value;
    private final List<IStateUpdatable> m_receivers = new ArrayList<IStateUpdatable>();

    public Record(String name, int id, Type type) {
        m_name = name;
        m_id = id;
        m_type = type;
    }

    /**
     * Returns the name of this record
     * @return A String name
     */
    public synchronized String getName() {
        return m_name;
    }

    /**
     * Returns the int ID of this field
     * @return The int ID of this field
     */
    public synchronized int getId() {
        return m_id;
    }

    /**
     * Sets the int ID of this field
     * @param id The int ID of this field
     */
    public synchronized void setId(int id) {
        m_id = id;
    }

    /**
     * Gets the Type of this field
     * @return The Type of this field
     */
    public synchronized Types.Type getType() {
        return m_type;
    }

    /**
     * Gets the value of this field
     * @return The value of this field
     */
    public synchronized Object getValue() {
        return m_value;
    }

    /**
     * Sets the value of this field and updates all the UI objects that are
     * registered with this record.
     *
     * @param value The value to be associated with this field
     */
    public synchronized void setValue(Object value) {
        this.m_value = value;

        for (IStateUpdatable receiver : m_receivers) {
            receiver.update(this);
        }
    }

    /**
     * Adds an IStateUpdatable which should be notified whenever the
     * value of this Record is set. Null values are ignored.
     * @param r The IStateUpdatable which should be notified.
     */
    public synchronized void addStateReceiver(IStateUpdatable r) {
        if (r == null) {
            return;
        }
        m_receivers.add(r);
    }

    /**
     * Remove a IStateUpdateable from the list of notified objects.
     * This is called when a UI element is deleted (or delete and added
     * as a new type of display element).
     */
    public synchronized void removeStateReceiver(IStateUpdatable r) {
	m_receivers.remove(r);
    }

    public synchronized int getStateReceiverCount() {
        return m_receivers.size();
    }
}
