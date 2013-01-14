package edu.wpi.first.smartdashboard.protocol;

import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;
import edu.wpi.first.smartdashboard.protocol.*;
import edu.wpi.first.smartdashboard.types.Types;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import org.junit.Before;

/**
 * @author pmalmsten
 */
public class AnnouncementReaderTest {
    private AnnouncementReader reader = new AnnouncementReader();
    private ByteArrayInputStream bannouncement;
    private DataInputStream iannouncement;

    @Before
    public void createAnnouncement() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(out);

        String name = "test";
        dout.write(5); // Field ID
        dout.write(Types.Type.CHAR.getId());
        dout.writeUTF(name);

        byte[] announcement = out.toByteArray();

        bannouncement = new ByteArrayInputStream(announcement);
        iannouncement = new DataInputStream(bannouncement);
    }

    /**
     * AnnouncementReader should properly parse a given 
     * announcement packet.
     */
    @Test
    public void testReadAnnouncement() {
        List<Object> actual = new ArrayList<Object>();
        try {
            reader.read(actual, iannouncement);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Exception!");
        }

        assertTrue("Update type must be correct", ((Integer)actual.get(0)) == PacketReader.ANNOUNCEMENT);
        assertTrue("Field ID must be correct", ((Integer)actual.get(1)).intValue() == 5);
        assertTrue("Type must be correct", ((Types.Type)actual.get(2)).equals(Types.Type.CHAR));
        assertTrue("Name must be correct", ((String)actual.get(3)).equals("test"));
    }
}

