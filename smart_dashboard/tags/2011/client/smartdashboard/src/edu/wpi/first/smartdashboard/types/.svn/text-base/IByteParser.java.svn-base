package edu.wpi.first.smartdashboard.types;

import java.io.DataInputStream;

/**
 * Defines the methods a class which can parse data from a DataInputStream
 * should have.
 * @author pmalmsten
 */
public interface IByteParser {
    /**
     * Parse the next data available into an appropriate Object representation.
     * @param data The DataInputStream from which data should be read.
     * @return An Object which represents the parsed form of the data to be read.
     * @throws edu.wpi.first.smartdashboard.types.Types.ParseFailedException
     */
    Object parse(DataInputStream data) throws Types.ParseFailedException;
}
