/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.networking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Bla
 * @author Joe
 */
class ConnectionManager {

    static final boolean IS_SERVER = false;
    /** The port number to use */
    private static final int PORT = 1735;
    /** Whether or not the connections have been initialized */
    private static boolean initialized = false;
    /** The set of all active connections */
    private static Connection connection;
    private static final Object lock = new Object();
    static String ipaddress;
    private static Set tables = new Set();

    public synchronized static void initialize() {
        if (ipaddress == null) {
            throw new NetworkTable.NoTeamNumberException();
        }
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
        Socket socket;

        // Open the server
        while (true) {
            try {
                synchronized (lock) {
                    socket = new Socket();
                    SocketAddress address = new InetSocketAddress(ipaddress, PORT);
                    socket.connect(address);
                    socket.setTcpNoDelay(true);
                    connection = new Connection(socket);

                    synchronized (ConnectionManager.class) {
                        for (int i = 0; i < tables.size(); i++) {
                            Table table = (Table) tables.get(i);
                            connection.offer(new TableRequest(table.string, table.table));
                            table.table.addConnection(connection);
                        }
                    }

                    connection.start();

                    lock.wait();
                }
            } catch (Exception ex) {
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex1) {
            }
        }
    }

    static void removeConnection(Connection connection) {
        synchronized (lock) {
            ConnectionManager.connection = null;
            lock.notify();
        }
    }

    static synchronized void requestTable(String name, NetworkTable table) {
        tables.add(new Table(name, table));
        if (connection != null) {
            synchronized (lock) {
                if (connection != null) {
                    connection.offer(new TableRequest(name, table));
                    table.addConnection(connection);
                }
            }
        }
    }

    private static class Table {

        final String string;
        final NetworkTable table;

        public Table(String string, NetworkTable table) {
            this.string = string;
            this.table = table;
        }
    }
}
