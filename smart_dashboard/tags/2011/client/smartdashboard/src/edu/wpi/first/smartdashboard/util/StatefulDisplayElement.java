package edu.wpi.first.smartdashboard.util;

import edu.wpi.first.smartdashboard.state.Record;

/**
 * Base definition which all UI elements should extend from.
 * @author pmalmsten
 */
public abstract class StatefulDisplayElement extends DisplayElement implements IStateUpdatable {
    protected Record m_record;
    protected String m_name;

    public void setFieldName(String name) {
	m_name = name;
    }

    public String getFieldName() {
	return m_name;
    }

    public void setRecord(Record r) {
	m_record = r;
    }
    
    @Override
    public void disconnect() {
	m_record.removeStateReceiver(this);
    }

    public Record getRecord() {
	return m_record;
    }
}
