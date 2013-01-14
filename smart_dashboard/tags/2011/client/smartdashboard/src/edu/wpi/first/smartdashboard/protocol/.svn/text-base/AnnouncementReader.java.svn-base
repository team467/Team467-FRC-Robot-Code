package edu.wpi.first.smartdashboard.protocol;

import edu.wpi.first.smartdashboard.types.Types;
import edu.wpi.first.smartdashboard.types.Types.UnrecognizedTypeException;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Reads announcement frames from network data
 * @author pmalmsten
 */
public class AnnouncementReader implements IReader {
    public static final int ID_INDEX = 1;
    public static final int TYPE_INDEX = 2;
    public static final int NAME_INDEX = 3;

    public void read(List<Object> output, DataInputStream is) throws IOException, UnrecognizedTypeException {
        int id = is.readUnsignedByte();
        int type = is.readUnsignedByte();
        String name = is.readUTF();

        output.add(PacketReader.ANNOUNCEMENT);
        output.add(id);
        output.add(Types.Type.reverseLookup(type));
        output.add(name);
    }
}
