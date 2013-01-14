package edu.wpi.first.smartdashboard.state;

import edu.wpi.first.smartdashboard.types.Types;
import edu.wpi.first.smartdashboard.util.IStateListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReceivedState implements java.io.Serializable {
    public static class UnknownFieldException extends Exception
            implements java.io.Serializable {
        public UnknownFieldException(String string) {
            super(string);
        }
    }

    private final Map<String, Record> m_byName = new HashMap<String, Record>();
    private final Map<Integer, Record> m_byId = new HashMap<Integer, Record>();
    private final Map<String, Record> m_limboByName = new HashMap<String, Record>();

    /**
     * Gets the record associated with the given name.
     * @param name The name of the record which should be looked up.
     * @return The Record associated with the given name.
     * @throws edu.wpi.first.smartdashboard.StateManager.ReceivedState.UnknownFieldException
     */
    public Record getRecord(String name) throws UnknownFieldException {
        Record r = m_byName.get(name);
        if (r == null) {
            throw new UnknownFieldException("Unknown name: " + name);
        }

        return r;
    }

    /**
     * Gets the record associated with the given ID.
     * @param id The ID of the record which should be looked up.
     * @return The Record associated with the given ID.
     * @throws edu.wpi.first.smartdashboard.StateManager.ReceivedState.UnknownFieldException
     */
    public Record getRecord(int id) throws UnknownFieldException {
        Record r = m_byId.get(id);
        if (r == null) {
            throw new UnknownFieldException("Unknown id: " + id);
        }

        return r;
    }

    /**
     * Registers the presence of a field with the given name, id, and type. This
     * can add a new field, replace a known field if the name or type changed,
     * or do nothing if an existing field is fine.
     *
     * @param name the field name
     * @param id the field id number
     * @param type the field data type
     * @param listeners the listeners to register for field value updates
     *
     * @return the Record for this field
     */
    public Record register(String name, int id, Types.Type type,
            List<IStateListener> listeners) {

        // Check whether the announced field already exists
        Record existing = m_byName.get(name);
        if (existing != null) {
            if (existing.getType() == type) {
                //TODO: make a better way to restore hidden UI elements.
                if(existing.getStateReceiverCount() == 0) {
                    for (IStateListener listener : listeners) {
                       existing.addStateReceiver(listener.newField(existing));
                    }
                }

                if(existing.getId() != id) {
                    // Move anything at the new ID location to limbo
                    Record displaced = m_byId.get(id);
                    if(displaced != null) {
                        m_limboByName.put(displaced.getName(), displaced);
                        m_byName.remove(displaced.getName());
                        m_byId.remove(id);
                    }

                    // Move the old record to the new ID location
                    m_byId.remove(existing.getId());
                    existing.setId(id);
                    m_byId.put(id, existing);
                }
                
                return existing;
            }

            //TODO: this could leave memory tied up since a GUI object hangs on
            //to the Record, even though the Record is no longer managed.
            m_byName.remove(existing.getName());
            m_byId.remove(existing.getId());
        }

        // Check whether the announced field already exists, but was put in limbo.
        existing = m_limboByName.get(name);
        if(existing != null) {
            if(existing.getType() == type) {
                //TODO: make a better way to restore hidden UI elements.
                if(existing.getStateReceiverCount() == 0) {
                    for (IStateListener listener : listeners) {
                       existing.addStateReceiver(listener.newField(existing));
                    }
                }

                // Move the old record to the new ID location
                // Take it out of limbo
                existing.setId(id);
                m_byId.put(id, existing);
                m_byName.put(name, existing);
                m_limboByName.remove(name);

                return existing;
            }

            m_limboByName.remove(existing.getName());
        }

        Record r = new Record(name, id, type);

        m_byName.put(name, r);
        m_byId.put(id, r);

        for (IStateListener listener : listeners) {
           r.addStateReceiver(listener.newField(r));
        }

        return r;
    }

    /**
     * Returns the names of all announced fields
     * @return A set of all String names of all announced fields.
     */
    public Set<String> getFieldNames() {
        return m_byName.keySet();
    }
}
