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

    DataOutputStream theFileOut;
    DataInputStream theFileIn;
    FileConnection fc;
    boolean fileWriteOpened = false;
    boolean fileReadOpened = false;

    /**
     * Opens a write to a file. Cannot be called if another file write is open.
     *
     * @param filepath filepath such as "/output.txt"
     */
    public void openWriteFile(String filepath)
    {
        try
        {
            openFile(Connector.WRITE, filepath);
            theFileIn = fc.openDataInputStream();
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
    public void openReadFile(String filepath)
    {
        try
        {
            openFile(Connector.READ, filepath);
            theFileOut = fc.openDataOutputStream();
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
    private void openFile(int mode, String filepath)
    {
        if (!fc.isOpen())
        {
            try
            {
                fc = (FileConnection) Connector.open("file://" + filepath, mode);
                if (!fc.exists())
                {
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
    public void closeFile()
    {
        try
        {
            fc.close();
            theFileIn.close();
            theFileOut.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    /**
     * Writes an array of any length into the file pointed to with openWriteFile()
     * @param doubleArray Array of doubles to write
     */
    public void writeDoubleArray(double[] doubleArray)
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
     * Writes an array of any length into the file pointed to with openWriteFile() as a human readable strings
     * @param doubleArray Array of doubles to write as strings
     */
    public void writeDoubleArrayAsString(double[] doubleArray)
    {
        if (fileWriteOpened && doubleArray != null)
        {
            for (int i = 0; i < doubleArray.length; i++)
            {
                try
                {
                    theFileOut.writeChars(String.valueOf(doubleArray[i]));
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
     * @param numberOfValuesToRead
     * @return The array containing the values.
     */
    public double[] readDoubleArray(int numberOfValuesToRead)
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
