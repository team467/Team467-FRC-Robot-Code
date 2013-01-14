package edu.wpi.first.smartdashboard.gui;

import edu.wpi.first.smartdashboard.types.Types;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Centralized table which manages which DisplayElements are able to display
 * a given type of data.
 * 
 * @author pmalmsten
 */
public class DisplayElementRegistry {
    /**
     * Indicates that a pariticular type is not associated with any
     * DisplayElement.
     */
    public static class NoElementsRegisteredForType extends Exception {
        public NoElementsRegisteredForType(String descrip) {
            super(descrip);
        }
    }

    private static Map<Types.Type, List<Class>> m_elements = new EnumMap<Types.Type, List<Class>>(Types.Type.class);

    /**
     * Registers the given IDisplayElementFactory as able to display the indicated type
     * @param type The type (as specified by edu.wpi.first.smartdashboard.Types)
     * which the given IDisplayElementFactory is able to display.
     * @param e The IDisplayElementFactory to register with the given type
     */
    public static void register(Types.Type type, Class e) {
        if(!m_elements.containsKey(type)) {
            m_elements.put(type, new ArrayList<Class>());
        }
        m_elements.get(type).add(e);
    }

    /**
     * Registers the given IDisplayElementFactory as able to display all of the indicated
     * types in the given list.
     * @param types The list of types for which the given IDisplayElementFactory should
     * be registered.
     * @param e The IDisplayElementFactory to register
     */
//    public static void register(List<Types.Type> types, Class e) {
//        for(Types.Type t : types) {
//            register(t, e);
//        }
//    }

    /**
     * Registers the given IDisplayFactory as able to display all of the inidicated
     * types in the given array.
     * @param types An array of types for which the given IDisplayElementFactory
     * should be registered.
     * @param e The IDisplayElementFactory to register.
     */
    public static void register(Types.Type[] types, Class e) {
        for(int i = 0; i < types.length; i++) {
            register(types[i], e);
        }
    }

    /**
     * Unregisters the given IDisplayElementFactory from being indicated as able to
     * display the given type of data.
     * @param type The type from which the IDisplayElementFactory should be unregistered
     * @param e The IDisplayElementFactory to unregister
     */
    public static void unregister(Types.Type type, Class e) {
        List<Class> bucket = m_elements.get(type);
        
        if(bucket != null) {
            bucket.remove(e);
        }
    }

    /**
     * Unregisters the given IDisplayElementFactory from being indicated as able to
     * display any of the given types.
     * @param types A list of types from which the given IDisplayElementFactory should
     * be unregistered.
     * @param e The IDisplayElementFactory to be unregistered.
     */
    public static void unregister(List<Types.Type> types, Class e) {
        for(Types.Type i : types) {
            unregister(i, e);
        }
    }

    /**
     * Returns a list of IDisplayElementFactory which are suitable for displaying the
     * indicated type of data.
     * @param type The type of data to be displayed
     * @return A list of IDisplayElementFactorys which are suitable for displaying the
     * given data
     */
    public static List<Class> elementsForType(Types.Type type) throws NoElementsRegisteredForType {
        if(!m_elements.containsKey(type))
            throw new NoElementsRegisteredForType("No UI element registered to receive type: " + type);

        return m_elements.get(type);
    }
}
