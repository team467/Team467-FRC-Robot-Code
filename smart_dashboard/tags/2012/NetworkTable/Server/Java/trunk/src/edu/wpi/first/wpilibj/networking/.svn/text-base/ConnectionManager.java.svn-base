/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Joe
 */
class ConnectionManager {

    static final boolean IS_SERVER = true;
    /** The port number to use */
    private static final int PORT = 1735;
    /** Whether or not the connections have been initialized */
    private static boolean initialized = false;
    /** The set of all active connections */
    private static Set connections = new Set();

    public synchronized static void initialize() {
        if (!initialized) {
            initialized = true;
            new Thread() {

                @Override
                public void run() {
                    acceptConnections();
                }
            }.start();
        }
    }

    /**
     * This will constantly look for and accept connections.
     * This should be given a dedicated thread, and should only be called once.
     * Will be called in the initialize method if called for the first time.
     */
    private static void acceptConnections() {
        ServerSocket server = null;

        // Open the server
        while (true) {
            try {
                server = new ServerSocket(PORT);
                break;
            } catch (IOException ex) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex1) {
                }
            }
        }

        try {
            while (true) {
                // Wait for a connection
                Socket socket = server.accept();

                socket.setTcpNoDelay(true);

                addConnection(new Connection(socket));
            }
        } catch (IOException ex) {
            System.out.println("NetworkingTable: LOST SERVER!");
        }
    }

    private static void addConnection(Connection connection) {
        connections.add(connection);
        connection.start();
    }

    static void removeConnection(Connection connection) {
        connections.remove(connection);
    }
}
