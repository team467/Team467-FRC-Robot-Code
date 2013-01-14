package edu.wpi.first.smartdashboard.types;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the types of data which may be received and a variety of utility
 * methods which help convert raw data into their appropriate Object representation.
 * @author pmalmsten
 */
public class Types {
    /**
     * Base Exception class for Types failures
     */
    public static class TypeException extends Exception {
        private TypeException(String description) {
            super(description);
        }
        public TypeException(String description, Throwable t) {
            super(description, t);
        }
    }

    /**
     * Thrown when an attempt to parse raw bytes into an appropriate Object
     * fails.
     */
    public static class ParseFailedException extends TypeException {
        public ParseFailedException(String description) {
            super(description);
        }
        public ParseFailedException(String description, Throwable t) {
            super(description, t);
        }
    }

    /**
     * Thrown when a particular given type ID is not recognized.
     */
    public static class UnrecognizedTypeException extends TypeException {
        private UnrecognizedTypeException(String description) {
            super(description);
        }
        public UnrecognizedTypeException(String description, Throwable t) {
            super(description, t);
        }
    }

    /**
     * Represents a particular type of data received across the network (e.g.
     * int, boolean, etc.).
     */
    public enum Type {
        // The ordering of these types is deliberate!
        // Their values must match up with that of the robot side code
        NONE(-1),
        BYTE(0),
        CHAR(1),
        INT(2),
        LONG(3),
        SHORT(4),
        FLOAT(5),
        DOUBLE(6),
        STRING(7),
        BOOLEAN(8),
        STRING_UTF8(9);

        private final int m_id;
        private static Map<Integer, Type> s_reverseTable = new HashMap<Integer, Type>();
        
        // Prepopulate reverse-lookup table
        static {
            for(Type t : Type.values()) {
                s_reverseTable.put(t.getId(), t);
            }
        }

        private Type(int id) {
            m_id = id;
        }

        /**
         * Returns the integer ID of this particular type
         * @return The integer ID of this type.
         */
        public int getId() {
            return m_id;
        }

        /**
         * Looks up a type from the reverse lookup table.
         * @param id The id whose type should be returned.
         * @return The Type associated with the given id.
         */
        public static Type reverseLookup(int id) throws UnrecognizedTypeException {
            if(!s_reverseTable.containsKey(id))
                throw new UnrecognizedTypeException("The id: " + id + " is not " +
                        "associated with a Type.");

            return s_reverseTable.get(id);
        }
    }
   
    private static Types instance = new Types();
    private Map<Type, IByteParser> m_parsers = new EnumMap<Type, IByteParser>(Type.class);

    public Types() {
        m_parsers.put(Type.CHAR, new CharParser());
        m_parsers.put(Type.INT, new IntParser());
        m_parsers.put(Type.BYTE, new ByteParser());
        m_parsers.put(Type.BOOLEAN, new BooleanParser());
        m_parsers.put(Type.DOUBLE, new DoubleParser());
        m_parsers.put(Type.FLOAT, new FloatParser());
        m_parsers.put(Type.LONG, new LongParser());
        m_parsers.put(Type.SHORT, new ShortParser());
        m_parsers.put(Type.STRING, new StringParser());
        m_parsers.put(Type.STRING_UTF8, new Utf8Parser());
    }

    /**
     * Converts the given data into an appropriate Object representation.
     * @param data The data to convert.
     * @param type The Type which the given data should be converted to.
     * @return The Object representation of the given data.
     * @throws edu.wpi.first.smartdashboard.types.Types.TypeException
     */
    public static Object parse(byte[] data, Type type) throws TypeException {
        ByteArrayInputStream binput = new ByteArrayInputStream(data);
        DataInputStream dinput = new DataInputStream(binput);

        if(!instance.m_parsers.containsKey(type))
            throw new UnrecognizedTypeException("Type: " + type + " not recognized");
        
        return instance.m_parsers.get(type).parse(dinput);
    }
}
