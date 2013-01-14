package edu.wpi.first.smartdashboard;

import edu.wpi.first.smartdashboard.state.ReceivedState.UnknownFieldException;
import edu.wpi.first.smartdashboard.types.Types.Type;
import edu.wpi.first.smartdashboard.types.Types.TypeException;
import java.io.IOException;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;

import edu.wpi.first.smartdashboard.StateManager;
import edu.wpi.first.smartdashboard.types.Types;
import edu.wpi.first.smartdashboard.protocol.*;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

/**
 * Tests StateManager
 * @author pmalmsten
 */
public class StateManagerTest {
    private StateManager m_manager;

    @Before
    public void setUp() {
         m_manager = new StateManager(null);
    }

    /**
     * Ensure that the default state of a StateManager
     * is sane and matches expectations.
     */
    @Test
    public void testDefaultState() {
        assertTrue("Name table should be empty",
                m_manager.getReceivedState().getFieldNames().isEmpty());
    }

    private List<Object> generateAnAnnouncement(int fieldId, String fieldName,
            Type fieldType) {
        List<Object> sampleData = new ArrayList<Object>();
        sampleData.add(PacketReader.ANNOUNCEMENT);
        sampleData.add(fieldId);
        sampleData.add(fieldType);
        sampleData.add(fieldName);
        return sampleData;
    }

    private List<Object> generateACharUpdate(int fieldId) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(bout);

        dout.writeChar(5);

        List<Object> update = new ArrayList<Object>();
        update.add(PacketReader.UPDATE);
        update.add(fieldId);
        update.add(bout.toByteArray()); // Data
        return update;
    }

    /**
     * Providing a valid field announcement, a StateManager should
     * indicate the proper state.
     */
    @Test
    public void testAnnounceNewField()
            throws TypeException, UnknownFieldException {
        List<Object> ann = generateAnAnnouncement(1, "Test", Types.Type.CHAR);

        m_manager.updateState(ann);

        assertEquals("One field is registered",
                1, m_manager.getReceivedState().getFieldNames().size());
        assertTrue("The name table should contain 'Test'",
                m_manager.getReceivedState().getFieldNames().contains("Test"));
        assertEquals("The type of the field should be correct",
                Types.Type.CHAR, m_manager.getReceivedState().getRecord("Test").getType());
    }

    /**
     * Providing a valid field announcement and update, a StateManager should
     * indicate the proper state.
     */
    @Test
    public void testAnnounceAndUpdate()
            throws IOException, TypeException, UnknownFieldException {
        List<Object> ann = generateAnAnnouncement(0, "Test", Types.Type.CHAR);
        List<Object> update = generateACharUpdate(0);

        m_manager.updateState(ann);
        m_manager.updateState(update);
        assertEquals("The proper object of the proper value should be in the state table",
                (char) 5, m_manager.getReceivedState().getRecord("Test").getValue());
    }

    
    @Test
    public void testMultipleAnnouncements()
            throws TypeException, UnknownFieldException {
        List<Object> ann = generateAnAnnouncement(1, "Test", Types.Type.CHAR);
        m_manager.updateState(ann);

        assertEquals("One field is registered",
                1, m_manager.getReceivedState().getFieldNames().size());
        assertEquals("The type of the field should be correct",
                Types.Type.CHAR, m_manager.getReceivedState().getRecord("Test").getType());

        // Announce the same field again.
        ann = generateAnAnnouncement(1, "Test", Types.Type.CHAR);
        m_manager.updateState(ann);

        assertEquals("One field is registered",
                1, m_manager.getReceivedState().getFieldNames().size());
        assertTrue("The name table should contain 'Test'",
                m_manager.getReceivedState().getFieldNames().contains("Test"));
        assertEquals("The type of the field should be correct",
                Types.Type.CHAR, m_manager.getReceivedState().getRecord("Test").getType());
    }

    @Test
    public void testConflictingIdAnnouncementsAreResolved() throws TypeException, UnknownFieldException {
        List<Object> ann = generateAnAnnouncement(0, "Test 1", Types.Type.CHAR);
        m_manager.updateState(ann);
        ann = generateAnAnnouncement(1, "Test 2", Types.Type.CHAR);
        m_manager.updateState(ann);

        // Check initial results
        assertEquals("Two fields are registered",
                2, m_manager.getReceivedState().getFieldNames().size());
        assertTrue("The name table should contain 'Test 1'",
                m_manager.getReceivedState().getFieldNames().contains("Test 1"));
        assertTrue("The name table should contain 'Test 2'",
                m_manager.getReceivedState().getFieldNames().contains("Test 2"));

        assertEquals("The ID of the field 'Test 1' should be correct",
                0, m_manager.getReceivedState().getRecord("Test 1").getId());
        assertEquals("The ID of the field 'Test 2' should be correct",
                1, m_manager.getReceivedState().getRecord("Test 2").getId());


        // Flip ID's (simulating a robot restart)
        ann = generateAnAnnouncement(0, "Test 2", Types.Type.CHAR);
        m_manager.updateState(ann);
        ann = generateAnAnnouncement(1, "Test 1", Types.Type.CHAR);
        m_manager.updateState(ann);

        // Check final results
        assertEquals("Two fields are registered",
                2, m_manager.getReceivedState().getFieldNames().size());
        assertTrue("The name table should contain 'Test 1'",
                m_manager.getReceivedState().getFieldNames().contains("Test 1"));
        assertTrue("The name table should contain 'Test 2'",
                m_manager.getReceivedState().getFieldNames().contains("Test 2"));

        assertEquals("The ID of the field 'Test 1' should be correct",
                1, m_manager.getReceivedState().getRecord("Test 1").getId());
        assertEquals("The ID of the field 'Test 2' should be correct",
                0, m_manager.getReceivedState().getRecord("Test 2").getId());
    }

}
