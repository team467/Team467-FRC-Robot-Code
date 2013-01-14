package edu.wpi.first.smartdashboard.util;

import edu.wpi.first.smartdashboard.state.Record;

/**
 * An interface for objects which would like to respond when a new field
 * arrives.
 * @author pmalmsten
 */
public interface IStateListener {
    /**
     * When an object which implements this interface is registered with an
     * appropriate state manager, this method will be called whenever a new field
     * is announced.
     * @param r The data record which is associated with the new field
     * @return An IStateUpdatable object which should receive state change
     * notifications concerning this field.
     */
    public IStateUpdatable newField(Record r);
}
