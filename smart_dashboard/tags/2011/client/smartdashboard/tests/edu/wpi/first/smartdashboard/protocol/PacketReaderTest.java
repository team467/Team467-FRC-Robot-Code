package edu.wpi.first.smartdashboard.protocol;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;
import edu.wpi.first.smartdashboard.protocol.PacketReader;
import edu.wpi.first.smartdashboard.types.Types;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.junit.Before;

/**
 * @author pmalmsten
 */
public class PacketReaderTest {
    private ByteArrayInputStream bannouncement;
    private DataInputStream iannouncement;

    @Before
    public void createAnnouncement() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(out);

        try {
            dout.write(PacketReader.ANNOUNCEMENT);
            dout.write(5); // Field ID
            dout.write(Types.Type.CHAR.getId());
            dout.writeUTF("test");

            byte[] announcement = out.toByteArray();

            bannouncement = new ByteArrayInputStream(announcement);
            iannouncement = new DataInputStream(bannouncement);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testGetInstance() {
        assertNotNull("Result of PacketReader.getInstance() must not be null",
                       PacketReader.getInstance());
    }

    /**
     * AnnouncementReader should properly parse a given 
     * announcement packet.
     */
    @Test
    public void testReadAnnouncement() {    
        try {
            List<Object> actual = new ArrayList<Object>();
            PacketReader.readPacket(actual, iannouncement);

            assertTrue("Update ID must be correct", ((Integer) actual.get(0)).intValue() == PacketReader.ANNOUNCEMENT);
            assertTrue("Field ID must be correct", ((Integer) actual.get(1)).intValue() == (byte) 5);
            assertTrue("Type must be correct", ((Types.Type) actual.get(2)).equals(Types.Type.CHAR));
            assertTrue("String must be correct", ((String) actual.get(3)).equals("test"));
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("NoSuchFieldException");
        }
    }
}

