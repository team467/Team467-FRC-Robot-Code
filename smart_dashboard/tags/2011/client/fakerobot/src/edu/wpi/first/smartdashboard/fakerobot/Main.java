package edu.wpi.first.smartdashboard.fakerobot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author pmalmsten
 */
public class Main {

    public static class SenderThread extends Thread {

        DatagramSocket ds;
        InetAddress localhost;
        ByteArrayOutputStream bastream = new ByteArrayOutputStream();

        @Override
        public void run() {
            try {
                ds = new DatagramSocket();
            } catch (SocketException ex) {
                ex.printStackTrace();
                System.exit(1);
            }

            try {
                localhost = InetAddress.getByAddress(new byte[]{127, 0, 0, 1});
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
                System.exit(1);
            }

            while (true) {
                synchronized (DriverStation.getInstance().getStatusDataMonitor()) {
                    DriverStation.getInstance().writePacket(bastream);
                }

                DatagramPacket p = new DatagramPacket(bastream.toByteArray(),
                        bastream.size(),
                        localhost,
                        1165);
                try {
                    ds.send(p);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                // Clear buffer
                bastream.reset();

                // Emulate robot loop delay
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        SenderThread sender = new SenderThread();
        sender.start();

        System.out.println("Running...");

        while (true) {
            // Use dashboard
            for (int i = 0; i < 400; i++) {
                for (int j = 0; j < 10; j++) {
                    SmartDashboard.log(i, "Test " + j);
                }
                
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
