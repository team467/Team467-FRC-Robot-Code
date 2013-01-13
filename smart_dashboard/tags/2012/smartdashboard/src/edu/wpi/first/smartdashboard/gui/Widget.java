package edu.wpi.first.smartdashboard.gui;

import edu.wpi.first.smartdashboard.types.DataType;
import edu.wpi.first.smartdashboard.types.DisplayElementRegistry;

/**
 *
 * @author Joe Grinstead
 */
public abstract class Widget extends DisplayElement {

    private String name;
    private DataType type;

    public void setFieldName(String name) {
        this.name = name;
    }

    public String getFieldName() {
        return name;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public DataType getType() {
        return type;
    }

    public abstract void setValue(Object value);

    public boolean supportsType(DataType type) {
        return DisplayElementRegistry.supportsType(getClass(), type);
    }
}
