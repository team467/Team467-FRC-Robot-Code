/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelspeedcalibration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTP;

/**
 *
 * @author Kyle
 */
public class FTPClass
{

    static FTPClient ftpClient;

    private static void showServerReply(FTPClient ftpClient)
    {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0)
        {
            for (String aReply : replies)
            {
                System.out.println("SERVER: " + aReply);
            }
        }
    }

    public static void connectToServer(ServerOperationEnum serverEnum)
    {
        System.out.println("Starting FTP connect...");
        //used for connecting
        String server = WheelSpeedCalibrationMap.IP_ADDRESS_CRIO;
        int port = 21;
        String user = WheelSpeedCalibrationMap.CRIO_USERNAME;
        String pass = WheelSpeedCalibrationMap.CRIO_PASSWORD;
        ftpClient = new FTPClient();
        try
        {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            ftpClient.connect(server, port);
            showServerReply(ftpClient);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode))
            {
                System.out.println("Operation failed. Server reply code: " + replyCode);
                return;
            }
            boolean logInSucess = ftpClient.login(user, pass);
            showServerReply(ftpClient);
            if (!logInSucess)
            {
                System.out.println("Could not login to the server");
                return;
            }
            else
            {
                System.out.println("LOGGED IN SERVER");
                if (serverEnum == ServerOperationEnum.PULL)
                {
                    pullFile();
                }
                else if (serverEnum == ServerOperationEnum.PUSH)
                {
                    pushFile();
                }
            }

        }
        catch (IOException ex)
        {
            System.out.println("Oops! Something wrong happened");
            ex.printStackTrace();
        }
    }

    private static void pullFile()
    {
        OutputStream outputStream1 = null;
        try
        {
            //using retrieveFile(String, OutputStream)
            String remoteFile1 = WheelSpeedCalibrationMap.PATH_TO_ROBOT_FILE;
            System.out.println(WheelSpeedCalibrationMap.PATH_TO_LOCAL_FILE);
            File downloadFile1 = new File(WheelSpeedCalibrationMap.PATH_TO_LOCAL_FILE);
            outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
            boolean retriveFileSucess = ftpClient.retrieveFile(remoteFile1, outputStream1);
            outputStream1.close();
            if (retriveFileSucess)
            {
                System.out.println("File has been downloaded successfully.");
            }
            else
            {
                System.out.println(ftpClient.getReplyString());
            }

        }
        catch (IOException ex)
        {
            Logger.getLogger(FTPClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                if (outputStream1 != null)
                {
                    outputStream1.close();
                }
            }
            catch (IOException ex)
            {
                Logger.getLogger(FTPClass.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void pushFile()
    {
        InputStream inputStream = null;
        try
        {
            File firstLocalFile = new File(WheelSpeedCalibrationMap.PATH_TO_LOCAL_FILE);
            String remoteFile = WheelSpeedCalibrationMap.PATH_TO_ROBOT_FILE;
            inputStream = new FileInputStream(firstLocalFile);
            System.out.println("Start uploading file");
            boolean done = ftpClient.storeFile(remoteFile, inputStream);
            System.out.println(ftpClient.getReplyString());
            inputStream.close();
            if (done)
            {
                System.out.println("The file is uploaded successfully.");
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(FTPClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                inputStream.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(FTPClass.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}