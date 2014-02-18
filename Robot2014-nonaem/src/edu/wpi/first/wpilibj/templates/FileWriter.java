/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import com.sun.squawk.microedition.io.FileConnection;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.Connector;

/**
 *
 * @author Team467
 */
public class FileWriter
{

    private static DataOutputStream theFileOut;
    private static DataInputStream theFileIn;
    private static FileConnection fc;
    private static boolean fileWriteOpened = false;
    private static boolean fileReadOpened = false;

    /**
     * Opens a write to a file. Cannot be called if another file write is open.
     *
     * @param filepath filepath such as "/output.txt"
     */
    public static void openWriteFile(String filepath)
    {
        try
        {            
            openFile(Connector.WRITE, filepath);
            theFileOut = fc.openDataOutputStream();
            fileWriteOpened = true;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Opens a read to a file. Cannot be called if another file write is open.
     *
     * @param filepath filepath such as "/output.txt"
     */
    public static void openReadFile(String filepath)
    {
        try
        {
            openFile(Connector.READ, filepath);
            theFileIn = fc.openDataInputStream();
            fileReadOpened = true;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * private function to read/write the file.
     *
     * @param mode
     * @param filepath
     */
    private static void openFile(int mode, String filepath)
    {
        if (!fileReadOpened && !fileWriteOpened)
        {
            try
            {
                fc = (FileConnection) Connector.open("file://" + filepath, mode);
                if (mode == Connector.WRITE)
                {
                    fc.delete();
                    fc.create();
                }

            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
        else
        {
            System.out.println("Error, file aready open");
        }
    }

    /**
     * Closes both read and write files.
     */
    public static void closeFile()
    {
        try
        {
            if (fc != null)
            {
                fc.close();
            }
            if (theFileIn != null)
            {
                theFileIn.close();
            }
            if (theFileOut != null)
            {
                theFileOut.close();
            }
            fileReadOpened = false;
            fileWriteOpened = false;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Writes an array of any length into the file pointed to with
     * openWriteFile()
     *
     * @param doubleArray Array of doubles to write
     */
    public static void writeDoubleArray(double[] doubleArray)
    {
        if (fileWriteOpened && doubleArray != null)
        {
            for (int i = 0; i < doubleArray.length; i++)
            {
                try
                {
                    theFileOut.writeDouble(doubleArray[i]);
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Writes an array of any length into the file pointed to with
     * openWriteFile() as a human readable strings
     *
     * @param doubleArray Array of doubles to write as strings
     */
    public static void writeDoubleArrayAsString(double[] doubleArray)
    {
        if (fileWriteOpened && doubleArray != null)
        {
            for (int i = 0; i < doubleArray.length; i++)
            {
                try
                {

                    theFileOut.write((String.valueOf(doubleArray[i]) + "\n").getBytes());

                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * reads the specified number of values and spits it into an array.
     *
     * @param numberOfValuesToRead
     * @return The array containing the values.
     */
    public static double[] readDoubleArray(int numberOfValuesToRead)
    {
        double[] array = new double[numberOfValuesToRead];
        if (fileWriteOpened)
        {
            for (int i = 0; i < numberOfValuesToRead; i++)
            {
                try
                {
                    array[i] = theFileIn.readDouble();
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
        return array;
    }
}
