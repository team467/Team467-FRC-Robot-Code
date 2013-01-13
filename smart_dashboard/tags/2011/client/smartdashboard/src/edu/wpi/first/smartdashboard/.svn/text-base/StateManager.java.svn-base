package edu.wpi.first.smartdashboard;

import edu.wpi.first.smartdashboard.types.Types;
import edu.wpi.first.smartdashboard.gui.StatusBar;
import java.util.List;
import edu.wpi.first.smartdashboard.net.UDPListener;
import edu.wpi.first.smartdashboard.protocol.*;
import edu.wpi.first.smartdashboard.state.ReceivedState;
import edu.wpi.first.smartdashboard.state.Record;
import edu.wpi.first.smartdashboard.util.IStateListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Observer;

/**
 * Manages the state associated with fields declared by a robot
 * @author pmalmsten
 */
public class StateManager extends Thread {
    private UDPListener m_listener = null;
    private final List<IStateListener> m_listeners =
            Collections.synchronizedList(new ArrayList<IStateListener>());
    private final List<Observer> m_badDataListeners = new ArrayList<Observer>();
    private final List<Observer> m_goodDataListeners = new ArrayList<Observer>();

    private ReceivedState m_state = new ReceivedState();

    private final Object m_dataAvailable = new Object();

    public StateManager(UDPListener listener) {
        m_listener = listener;
    }

    /**
     * Runs this StateManager as a thread in the background which blocks on and
     * processes received UDP dashboard messages.
     */
    @Override
    public void run() {
        List<Object> output = new ArrayList<Object>();
        while(true) {
            try {
                DataInputStream istream = m_listener.recv();

                while(istream.available() > 0)
                {
                    output.clear();

                    try {
                        PacketReader.readPacket(output, istream);
                        updateState(output);

                        for(Observer o : m_goodDataListeners) {
                            o.update(null, StatusBar.StateTransition.GOOD_DATA);
                        }
                    } catch (ReceivedState.UnknownFieldException ex) {
                        for(Observer o : m_badDataListeners) {
                            o.update(null, StatusBar.StateTransition.BAD_DATA);
                        }
                    }
                    
                    synchronized(m_dataAvailable) {
                        m_dataAvailable.notifyAll();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Updates this StateManager with the given data.
     * @param data The data to process. This data must be a well-formed list
     *  with fields as defined by constants in an appropriate *Reader class in.
     *  the protocol package. The first element must always identify the type of
     *  data to follow.
     */
    public void updateState(List<Object> data) throws Types.TypeException, ReceivedState.UnknownFieldException {
        switch (((Integer) data.get(0)).intValue()) {
            case PacketReader.ANNOUNCEMENT:
                String name = (String) data.get(AnnouncementReader.NAME_INDEX);
                Integer id = (Integer) data.get(AnnouncementReader.ID_INDEX);
                Types.Type type = (Types.Type) data.get(AnnouncementReader.TYPE_INDEX);

                synchronized(m_listeners) {
                    m_state.register(name, id, type, m_listeners);
                }
                break;

            case PacketReader.UPDATE:
                Object rawValue = data.get(UpdateReader.DATA_INDEX);
                Integer field_id = (Integer) data.get(UpdateReader.ID_INDEX);
                Record record = m_state.getRecord(field_id);
                Types.Type field_type_id = record.getType();

                Object parsedValue = Types.parse((byte[]) rawValue, field_type_id);
                record.setValue(parsedValue);
                break;
        }
    }

    /**
     * Gets the received state table of this StateManager.
     * @return The ReceivedState of this StateManager.
     */
    public ReceivedState getReceivedState() {
        return m_state;
    }
    
    /**
     * Blocks the calling thread until new data is avalable.
     */
    public void waitForData() throws java.lang.InterruptedException {
        synchronized(m_dataAvailable) {
            m_dataAvailable.wait();
        }
    }

    /**
     * Registers the given IStateListener to be notified when a new field is
     * announced.
     * @param l The IStateListener to notify.
     */
    public void registerForAnnouncements(IStateListener l) {
        synchronized (m_listeners) {
            m_listeners.add(l);
        }
    }

    /**
     * Unregisters the given IStateListener such that it is no longer notified
     * of new field announcements.
     * @param l The IStateListener to unregister.
     */
    public void unregisterForAnnouncements(IStateListener l) {
        synchronized (m_listeners) {
            m_listeners.remove(l);
        }
    }

    /**
     * Registers the given object to receive events when bad (out of sync) data
     * arrives.
     * @param o The object to notify.
     */
    public void notifyOnBadData(Observer o) {
        if(o != null)
            m_badDataListeners.add(o);
    }

    /**
     * Registers the given object to receive events when good data arrives.
     * @param o The object to notify.
     */
    public void notifyOnGoodData(Observer o) {
        if(o != null)
            m_goodDataListeners.add(o);
    }

    /**
     * Writes this object's and any contained objects' important state
     * information to the given ObjectOutputStream.
     * @param objOut The stream to write to.
     * @throws IOException
     */
    public void saveState(ObjectOutputStream objOut) throws IOException {
       objOut.writeObject(m_state);
    }

    /**
     * Loads this object's and any contained object's important state
     * information from the given ObjectInputStream.
     * @param objIn The stream to read.
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public void loadState(ObjectInputStream objIn)
            throws IOException, ClassNotFoundException {
       m_state = (ReceivedState) objIn.readObject();
    }
}
