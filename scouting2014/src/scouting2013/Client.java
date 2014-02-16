/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scouting2013;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aidan
 */
public class Client
{
    //Connection ports
    private final int PORT = 15123;
    
    //Client connection socket
    private Socket socket;
    
    /**
     * Connects the client to the server
     * @param serverIP
     * @throws IOException 
     */
    public void connect(String serverIP) throws IOException
    {
        //Create connection to host
        socket = new Socket(serverIP, PORT);
        System.out.println("Connected to server: " + socket);
    }
    
    /**
     * Sends a file to the server from the user directory
     * @param file
     * @throws IOException 
     */
    public void sendFile(String file) throws IOException
    {
        //Get the file to transfer
        String path = System.getProperty("user.dir") + "/" + file;
        System.out.println(path);
        File transferFile = new File(path);

        //Create a byte array to read the file into
        byte[] bytearray = new byte[(int) transferFile.length()];

        //Create file input stream
        FileInputStream fin = new FileInputStream(transferFile);
        BufferedInputStream bin = new BufferedInputStream(fin);

        //Read the file into the byte array
        bin.read(bytearray, 0, bytearray.length);

        //Get socket output stream
        OutputStream os = socket.getOutputStream();

        //Write byte array to output stream
        System.out.println("Sending Files...");
        os.write(bytearray);
        
        //Writing a byte of -1 indicates the file has ended
        os.write(-1);

        //Close connections
        os.flush();
        System.out.println("File transfer complete");
    }
    
    public void closeConnections()
    {
        try
        {
            socket.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
