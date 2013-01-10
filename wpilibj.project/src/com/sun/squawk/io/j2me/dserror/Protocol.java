/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008-2012. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.sun.squawk.io.j2me.dserror;

import com.sun.cldc.jna.Pointer;
import java.io.*;
import javax.microedition.io.*;
import com.sun.squawk.io.*;
import edu.wpi.first.wpilibj.communication.FRCControl;

/**
 * This Generic Connection Framework Protocol class writes to the FRC Driver Station error pane.
 * @author dw29446
 */
public class Protocol extends ConnectionBase implements OutputConnection {

    protected boolean opened = false;

    public Protocol() {
    }
    
    /**
     * Open the connection
     * @param name       the target for the connection
     * @param timeouts   a flag to indicate that the called wants
     *                   timeout exceptions
     */
    public Connection open(String protocol, String name, int mode, boolean timeouts)
            throws IOException {
        return this;
    }

    /**
     * Returns an output stream for this socket.
     *
     * @return     an output stream for writing bytes to this socket.
     * @exception  IOException  if an I/O error occurs when creating
     *                          the output stream.
     */
    public OutputStream openOutputStream()
            throws IOException {

        if (opened) {
            throw new IOException("Stream already opened");
        }
        opened = true;
        return new DSErrorOutputStream();
    }
}

/**
 * Output stream for the connection
 */
class DSErrorOutputStream extends OutputStream {

    final static int DEFAULT_BUFFER_SIZE = 512;
    private Pointer errorBuffer;
    private int index;
    private boolean errorOccurred;

    /**
     * Constructor
     */
    DSErrorOutputStream() {
        errorBuffer = new Pointer(DEFAULT_BUFFER_SIZE);
        index = 0;
    }

    /**
     * Writes the specified byte to this output stream.
     *
     * @param      b   the <code>byte</code>.
     * @exception  IOException  if an I/O error occurs. In particular,
     *             an <code>IOException</code> may be thrown if the
     *             output stream has been closed.
     */
    synchronized public void write(int b)
            throws IOException {
        if (errorOccurred) {
            return;
        }
        try {
            if (errorBuffer == null) {
                throw new IllegalStateException("DSErrorOutputStream is closed");
            }

            if (index >= errorBuffer.getSize()) {
                flush();
            }

            if (b == '\n' && index != 0) {
                flush(); // appends cr and nl...
            } else {
                errorBuffer.setByte(index++, (byte) b);
            }
        } catch (Throwable e) {
            errorOccurred = true;
            throw new RuntimeException("Squashing exception in error stream writer: " + e);
        }
    }

//    public synchronized void write(byte b[], int off, int len) throws IOException {
//        if (b == null) {
//            throw new NullPointerException();
//        } else if ((off < 0) || (off > b.length) || (len < 0) ||
//                   ((off + len) > b.length) || ((off + len) < 0)) {
//            throw new IndexOutOfBoundsException();
//        } else if (len == 0) {
//            return;
//        }
//        for (int i = 0 ; i < len ; i++) {
//            write(b[off + i]);
//        }
//    }

    /**
     * Flushes this output stream and forces any buffered output bytes
     * to be written out. The general contract of <code>flush</code> is
     * that calling it is an indication that, if any bytes previously
     * written have been buffered by the implementation of the output
     * stream, such bytes should immediately be written to their
     * intended destination.
     * <p>
     * The <code>flush</code> method of <code>OutputStream</code> does nothing.
     *
     * @exception  IOException  if an I/O error occurs.
     */
    public synchronized void flush()
            throws IOException {
        if (errorBuffer == null) {
            throw new IllegalStateException("DSErrorOutputStream is closed");
        }

        if (index > 0) {
            FRCControl.setErrorData(errorBuffer, index, 100);
            index = 0;
        }
    }

    /**
     * Close the stream
     *
     * @exception  IOException  if an I/O error occurs.
     */
    public synchronized void close() {
        if (errorBuffer != null) {
            index = 0;
            errorBuffer.free();
            errorBuffer = null;
        }
    }
}

