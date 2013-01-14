package edu.wpi.first.smartdashboard.util;

import edu.wpi.first.smartdashboard.state.Record;

/**
 * An interface for objects which would like to be notified when the value of a
 * particular field is updated.
 * @author pmalmsten
 */
public interface IStateUpdatable {
    /**
     * This method is called whenever the field associated with this object
     * receives a new value.
     * @param r The field data record (containing the newly-received value) associated
     * with this IStateUpdatable.
     */
    public void update(Record r);
}
