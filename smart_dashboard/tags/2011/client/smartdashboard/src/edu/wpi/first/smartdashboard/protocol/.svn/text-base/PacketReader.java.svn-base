package edu.wpi.first.smartdashboard.protocol;

import edu.wpi.first.smartdashboard.types.Types.UnrecognizedTypeException;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.List;
import java.util.HashMap;

/**
 * Reads a frame from the network and extracts any/all data it contains.
 * @author pmalmsten
 */
public class PacketReader {
    public static final int ANNOUNCEMENT = 0;
    public static final int UPDATE = 1;
    private Map<Integer,IReader> m_readerTable = new HashMap<Integer,IReader>();
    private static PacketReader instance = new PacketReader();

    /**
     * Creates a PacketReader
     */
    private PacketReader() {
        m_readerTable.put(ANNOUNCEMENT, new AnnouncementReader());
        m_readerTable.put(UPDATE, new UpdateReader());
    }

    /**
     * Reads the next frame in the data input stream.
     * @param output The list in which resulting data is placed.
     * @param is The DataInputStream from which to read.
     * @throws NoSuchFieldException
     * @throws IOException
     * @throws UnrecognizedTypeException
     */
    public static void readPacket(List<Object> output, DataInputStream is) throws NoSuchFieldException, IOException, UnrecognizedTypeException {
        int packet_id = is.readUnsignedByte();
        IReader reader = getInstance().m_readerTable.get(packet_id);

        if (reader == null)
            throw new NoSuchFieldException(packet_id + " is not a valid packet type");

        reader.read(output, is);
    }

    /**
     * Returns the singleton PacketReader instance
     */
    public static PacketReader getInstance() {
        return instance;
    }
}
