package edu.wpi.first.smartdashboard.protocol;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;
import edu.wpi.first.smartdashboard.types.Types.UnrecognizedTypeException;

/**
 * Represents an object which can parse a particular type of frame from the
 * network.
 * @author pmalmsten
 */
public interface IReader {
    /**
     * Reads and parses the given data into a standard data format.
     *
     * The first element of this data must indicate the type of data which
     * was read (with a value like PacketReader.ANNOUNCEMENT).
     */
    void read(List<Object> output, DataInputStream is) throws IOException, UnrecognizedTypeException;
}
