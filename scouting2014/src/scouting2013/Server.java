/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scouting2013;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aidan
 */
public class Server implements Runnable
{
    //Id number of client (used for file writing)
    private static int clientId = 0;
    
    //Server port
    private static int PORT = 15123;
    
    /**
     * Main server thread which listens for client connections and passes those
     * connections onto client handlers
     */
    public void run()
    {
        try
        {
            //Open a new server socket
            ServerSocket serverSocket = new ServerSocket(PORT);
            
            while (true)
            {
                //Accept a connection from a client
                Socket socket = serverSocket.accept();
                System.out.println("Accepted connection : " + socket);
                
                //Pass handling to new client handler
                new Thread(new ClientHandler(socket, clientId)).start();
                clientId += 1;
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Class which handles connections to a specific client
     */
    private class ClientHandler implements Runnable
    {   
        //Client socket
        private Socket socket;

        //Client id
        private int id;
        
        public ClientHandler(Socket s, int id)
        {
            socket = s;
            this.id = id;
        }

        /**
         * Thread which handles individual clients connected to the server
         */
        public void run()
        {
            try
            {
                
                while (true)
                {
                    //Make variables for reading data
                    int filesize = 2022386;
                    int bytesRead;
                    int currentTot = 0;
                    byte[] bytearray = new byte[filesize];
                    
                    //Get input stream from socket
                    InputStream is = socket.getInputStream();
                    
                    //Continue checking the input stream until data is found
                    do
                    {
                        bytesRead =
                                is.read(bytearray, currentTot, (bytearray.length - currentTot));
                        if (bytesRead >= 0)
                        {
                            currentTot += bytesRead;
                        }
                        
                        //Bytes read of -1 means stream has ended
                        if (bytesRead != -1)
                        {
                            //Byte value of -1 indicates file end
                            if (bytearray[currentTot - 1] == -1)
                            {
                                //Remove ending byte (-1) and break out of the loop
                                currentTot -= 1;
                                bytearray = Arrays.copyOfRange(bytearray, 0, currentTot);
                                break;
                            }
                        }
                    }
                    while (bytesRead > -1);
                    
                    //Bytes read of -1 means stream has ended, so end this client
                    if (bytesRead == -1)
                    {
                        break;
                    }
                    
                    //Make file output stream to write data to a file
                    FileOutputStream fos = new FileOutputStream("Save Data/Save-" + id + ".ser");
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    
                    //Write gathered data to a file
                    bos.write(bytearray, 0, currentTot);
                    System.out.println("File Received");
                    
                    //Flush file output stream
                    bos.flush();
                    bos.close();
                }
                
                //Close socket
                System.out.println("Connection Ended : " + socket);
                socket.close();
                
            }
            catch (FileNotFoundException ex)
            {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (IOException ex)
            {
                System.out.println("Error");
            }
        }
    }
    
}
