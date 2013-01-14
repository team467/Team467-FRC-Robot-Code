package edu.wpi.first.smartdashboard.net;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Provides capabilities associated with listening to a UDP network
 * socket for dashboard traffic
 * @author pmalmsten
 */
public class UDPListener {
    public static final int BUFFER_SIZE = 2048;
    private DatagramSocket m_socket = null;
    private DatagramPacket m_packet = null;
    private byte[] m_buffer = new byte[BUFFER_SIZE];
    private int m_updateNumOffset = 0;
    private int m_updateNum = 0;

    /**
     * Initializes the UDPListener for listening on a port for dashboard data
     * @param port The port number on which to listen
     * @param updateNumOffset The byte offset at which the update number of the dashboard data can be found
     */
    public UDPListener(int port, int updateNumOffset) throws java.net.SocketException {
        m_socket = new DatagramSocket(port);
        m_packet = new DatagramPacket(m_buffer, BUFFER_SIZE);
        m_updateNumOffset = updateNumOffset;
    }

    /**
     * Receives data from the network; blocks until a new packet with a new
     * update number is received.
     * @return A byte array containing the dashboard data to be processed
     */
    public DataInputStream recv() throws java.io.IOException {
        ByteArrayInputStream bstream = null;
        DataInputStream dinput = null;
        int currentUpdateNum = m_updateNum;
        int length = 0;

        while(currentUpdateNum == m_updateNum) {
            m_socket.receive(m_packet);

            bstream = new ByteArrayInputStream(m_buffer);
            dinput = new DataInputStream(bstream);

            dinput.skip(m_updateNumOffset);
            currentUpdateNum = dinput.readUnsignedByte();

            length = dinput.readUnsignedShort();
            length = (length << 16);
            length += dinput.readUnsignedShort();

            if(length < 1)
                // Drop the packet
                currentUpdateNum = m_updateNum;
        }
	m_updateNum = currentUpdateNum;
        
        // Create copy of useful data.
        // - This ensures that mutiple callers do not access the same buffer
        // - The copy stream only contains useful information (no 0's at the end)
        byte[] copy = new byte[length];
        System.arraycopy(m_buffer, m_updateNumOffset + 5, copy, 0, length);
        ByteArrayInputStream bcopy = new ByteArrayInputStream(copy);
       
        return new DataInputStream(bcopy);
    }
}
