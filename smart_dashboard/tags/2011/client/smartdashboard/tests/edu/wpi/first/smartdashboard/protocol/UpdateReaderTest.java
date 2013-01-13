package edu.wpi.first.smartdashboard.protocol;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;
import edu.wpi.first.smartdashboard.protocol.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author pmalmsten
 */
public class UpdateReaderTest {
    private UpdateReader reader = new UpdateReader();
    private ByteArrayInputStream bupdate;
    private DataInputStream iupdate;

    @Before
    public void createUpdate() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(out);
        try {
            dout.write(0);
            dout.write(1);
            dout.write((byte) 26);

            byte[] update = out.toByteArray();

            bupdate = new ByteArrayInputStream(update);
            iupdate = new DataInputStream(bupdate);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * UpdateReader should properly parse a given 
     * announcement packet.
     */
    @Test
    public void testReadUpdate() {
        List<Object> actual = new ArrayList<Object>();
        try {
            reader.read(actual, iupdate);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Exception caught!");
        }

        assertEquals("List must be three elements large", 3, actual.size());
        assertEquals("State update identifier must be correct", PacketReader.UPDATE, ((Integer) actual.get(0)).intValue());
        assertEquals("First element must be correct", (int) 0, actual.get(1));
        assertEquals("Second element must be an array of the proper length", 1, ((byte[])actual.get(2)).length);
        assertEquals("First element of array must be correct", 26, ((byte[]) actual.get(2))[0]);
    }
}

