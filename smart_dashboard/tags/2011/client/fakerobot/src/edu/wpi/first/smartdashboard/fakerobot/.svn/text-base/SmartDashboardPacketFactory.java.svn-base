package edu.wpi.first.smartdashboard.fakerobot;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Creates raw data frames to be sent across the network for SmartDashboard
 * @author pmalmsten
 */
public class SmartDashboardPacketFactory {
    private static final byte ANNOUNCE_TYPE = 0;
    private static final byte UPDATE_TYPE = 1;
    private static final byte ANNOUNCE_PROFILE_TYPE = 2;

    /**
     * Announces a new field to the client GUI
     * @param name The name of the new field
     * @param type The type of data the field should accept
     * @param id The numerical id of the field
     */
    public static void announce(DataOutputStream s, String name, byte type, byte id) throws IOException {
        s.writeByte(ANNOUNCE_TYPE);
        s.writeByte(id);
        s.writeByte(type);
        s.writeUTF(name);
    }

    /**
     * Returns the expected length of an announcement frame.
     * @param name The name of the field to announce.
     * @return The byte length of an announcement for the field of the given name.
     */
    public static int getAnnounceLength(String name) {
        return 5 + SmartDashboard.utfLength(name);
    }

    /**
     * Writes a update frame prefix to the given DataOutputStream. Data of the
     * indicated length must follow, otherwise the resulting data stream
     * will be inconsistent!
     * @param s The stream to write to.
     * @param id The ID which the field is associated with.
     * @param length The length of the following data.
     * @throws IOException
     */
    public static void updatePrefix(DataOutputStream s, byte id, byte length) throws IOException {
        s.writeByte(UPDATE_TYPE);
        s.writeByte(id);
        s.writeByte(length);
    }

    /**
     * Returns the expected length of a complete update frame (prefix + data).
     * @param dataLen The length of the data included in the update.
     * @return The length, in bytes, of the complete update frame.
     */
    public static int getUpdateLength(int dataLen) {
        return 3 + dataLen;
    }

    /**
     * Informs the client as to which GUI profile should be loaded and used.
     *
     * This has not yet been implemented on the client!
     *
     * @param s The stream to write to.
     * @param name The name of the profile to load.
     * @throws IOException
     */
    public static void announceProfile(DataOutputStream s, String name) throws IOException {
        s.writeByte(ANNOUNCE_PROFILE_TYPE);
        s.writeUTF(name);
    }

    /**
     * Returns the expected length of a profile announcement frame.
     * @param name The name of the profile to be announced.
     * @return The length, in bytes, of the profile announcement frame.
     */
    public static int getAnnounceProfileLength(String name) {
        return 3 + SmartDashboard.utfLength(name);
    }
}

