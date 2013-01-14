package edu.wpi.first.smartdashboard.gui;

import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.properties.PropertyHolder;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JComponent;

/**
 *
 * @author Mitchell Wills
 */
public abstract class NewWidget extends JComponent implements PropertyHolder{
    public NewWidget(){
        setOpaque(false);//TODO should widgets be opaque?
    }
    public abstract void cleanupWidget();
    
    private boolean isObstruction = true;
    public boolean isObstruction(){
        return isObstruction;
    }
    public void setObstruction(boolean isObstruction){
        this.isObstruction = isObstruction;
    }
    
    private boolean isResizable = true;
    public boolean isResizable(){
        return isResizable;
    }
    public void setResizable(boolean isResizable){
        this.isResizable = isResizable;
    }
    
    
    
    private Map<String, Property> properties = new LinkedHashMap<String, Property>();
    public Map<String, Property> getProperties() {
        return properties;
    }
    public boolean validatePropertyChange(Property property, Object value) {
        return true;
    }
}
