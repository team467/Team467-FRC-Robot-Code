/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.livewindow;

import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import java.util.Vector;

/**
 * The LiveWindow class is the public interface for putting sensors and
 * actuators on the LiveWindow.
 *
 * @author Alex Henning
 */
public class LiveWindow {

    private static Vector sensors = new Vector();
    private static Vector actuators = new Vector();
    private static Vector components = new Vector();
    private static ITable livewindowTable = NetworkTable.getTable("LiveWindow");
    private static ITable statusTable = livewindowTable.getSubTable("~STATUS~");
    private static boolean liveWindowEnabled = false;

    /**
     * Set the enabled state of LiveWindow.
     * If it's being enabled, turn off the scheduler and remove all the commands from the queue
     * and enable all the components registered for LiveWindow. If it's being disabled, stop all
     * the registered components and reenable the scheduler.
     * TODO: add code to disable PID loops when enabling LiveWindow. The commands should reenable
     * the PID loops themselves when they get rescheduled. This prevents arms from starting to move
     * around, etc. after a period of adjusting them in LiveWindow mode.
     */
    public static void setEnabled(boolean enabled) {
        if (liveWindowEnabled != enabled) {
            if (enabled) {
                System.out.println("Starting live window mode.");
                Scheduler.getInstance().disable();
                Scheduler.getInstance().removeAll();
                for (int i = 0; i < components.size(); i++) {
                    LiveWindowSendable component = ((LiveWindowSendable) components.elementAt(i));
                    component.startLiveWindowMode();
                }
            } else {
                System.out.println("stopping live window mode.");
                for (int i = 0; i < components.size(); i++) {
                    LiveWindowSendable component = ((LiveWindowSendable) components.elementAt(i));
                    component.stopLiveWindowMode();
                }
                Scheduler.getInstance().enable();
            }
            liveWindowEnabled = enabled;
            statusTable.putBoolean("LW Enabled", enabled);
        }
    }

    public static void run() {
        updateValues();
    }

    /**
     * Add a Sensor associated with the subsystem and with call it by the given
     * name.
     *
     * @param subsystem The subsystem this component is part of.
     * @param name The name of this component.
     * @param component A LiveWindowSendable component that represents a sensor.
     */
    public static void addSensor(String subsystem, String name, LiveWindowSendable component) {
        addComponent(subsystem, name, component);
        sensors.addElement(component);
    }

    /**
     * Add an Actuator associated with the subsystem and with call it by the
     * given name.
     *
     * @param subsystem The subsystem this component is part of.
     * @param name The name of this component.
     * @param component A LiveWindowSendable component that represents a
     * actuator.
     */
    public static void addActuator(String subsystem, String name, LiveWindowSendable component) {
        addComponent(subsystem, name, component);
        actuators.addElement(component);
    }

    /**
     * Add a Component associated with the subsystem and with call it by the
     * given name.
     *
     * @param subsystem The subsystem this component is part of.
     * @param name The name of this component.
     * @param component A LiveWindowSendable component that represents a
     * component.
     */
    private static void addComponent(String subsystem, String name, LiveWindowSendable component) {
        System.out.println("Initializing table for '" + subsystem + "' '" + name + "'");
        livewindowTable.getSubTable(subsystem).putString("~TYPE~", "LW Subsystem");
        ITable table = livewindowTable.getSubTable(subsystem).getSubTable(name);
        table.putString("~TYPE~", component.getSmartDashboardType());
        table.putString("Name", name);
        table.putString("Subsystem", subsystem);
        component.initTable(table);
        components.addElement(component);
    }

    /**
     * Puts all sensor values on the live window.
     */
    private static void updateValues() {
        for (int i = 0; i < sensors.size(); i++) {
            LiveWindowSendable sensor = ((LiveWindowSendable) sensors.elementAt(i));
            sensor.updateTable();
        }
        // TODO: Add actuators?
        // TODO: Add better rate limiting.
    }
}
