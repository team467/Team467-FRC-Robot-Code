package edu.wpi.first.smartdashboard.protocol;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Reads update frames from network data.
 * @author pmalmsten
 */
public class UpdateReader implements IReader {
    // Result List<Object> indexes
    public static final int ID_INDEX = 1;
    public static final int DATA_INDEX = 2;

    public void read(List<Object> output, DataInputStream is) throws IOException {
        int id = is.readUnsignedByte();
        int datalen = is.readUnsignedByte();
        byte[] slice = new byte[datalen];

        is.read(slice, 0, datalen);

        output.add(PacketReader.UPDATE);
        output.add(id);
        output.add(slice);
    }
}
