package edu.wpi.first.smartdashboard.gui;

/**
 *
 * @author Mitchell Wills
 */
public class ValueType implements NewDataType{
    public static ValueType getTypeOf(Object value) {
        if(value==null)
            throw new NullPointerException("Cannot get type of null value");
        return getType(value.getClass());
    }
    public static ValueType getType(Class<?> type) {
        if(type==null)
            throw new NullPointerException("Cannot get type for null class");
        return new ValueType(type);
    }
    
    public static final ValueType BOOLEAN = getType(Boolean.class);
    public static final ValueType DOUBLE = getType(Double.class);
    public static final ValueType INTEGER = getType(Integer.class);
    public static final ValueType NUMBER = getType(Number.class);
    public static final ValueType STRING = getType(String.class);

    
    private final Class<?> type;
    
    private ValueType(Class<?> type){
        this.type = type;
    }
    
    public Class<?> getType(){
        return type;
    }
    
}
