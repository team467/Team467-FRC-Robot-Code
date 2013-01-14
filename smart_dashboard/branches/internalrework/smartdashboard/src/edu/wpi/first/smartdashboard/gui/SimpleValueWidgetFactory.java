/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.smartdashboard.gui;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 *
 * @author Mitchell Wills
 */
public class SimpleValueWidgetFactory implements ValueWidgetFactory{
    private final Class<? extends ValueWidget> widgetType;
    private Constructor<? extends ValueWidget> constructor = null;
    private Constructor<? extends ValueWidget> stringConstructor = null;
    private String name;
    private ValueType[] supportedTypes;
    
    public SimpleValueWidgetFactory(Class<? extends ValueWidget> widgetType){
        this.widgetType = widgetType;
        
        try {
            Field field = widgetType.getDeclaredField("NAME");
            int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers)) {
                throw new RuntimeException("NAME must be static");
            } else if (!Modifier.isFinal(modifiers)) {
                throw new RuntimeException("NAME must be final");
            }
            if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
                name = (String) field.get(null);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            name = widgetType.getSimpleName();
        }
        
        try {
            Field field = widgetType.getDeclaredField("TYPES");
            int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers)) {
                throw new RuntimeException("TYPES must be static in "+widgetType.getName());
            } else if (!Modifier.isFinal(modifiers)) {
                throw new RuntimeException("TYPES must be final in "+widgetType.getName());
            }
            if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
                supportedTypes = (ValueType[]) field.get(null);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Could not get TYPES value for "+widgetType.getName());
        }
        
        
        
        try {
            constructor = widgetType.getConstructor();
        } catch (NoSuchMethodException ex) {
        } catch (SecurityException ex) {
            throw new RuntimeException(ex);
        }
        try {
            stringConstructor = widgetType.getConstructor(String.class);
        } catch (NoSuchMethodException ex) {
        } catch (SecurityException ex) {
            throw new RuntimeException(ex);
        }
        if(constructor==null && stringConstructor==null)
                throw new RuntimeException("A simple value widget must have a default constructor or constructor which takes a string");
    }
    
    public ValueWidget createWidget(String name) {
        try {
            if(stringConstructor!=null)
                return stringConstructor.newInstance(name);
            else
                return constructor.newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean supports(Class<?> type) {
        for(ValueType valueType:supportedTypes){
            if(valueType.getType().isAssignableFrom(type))
                return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }
    
}
