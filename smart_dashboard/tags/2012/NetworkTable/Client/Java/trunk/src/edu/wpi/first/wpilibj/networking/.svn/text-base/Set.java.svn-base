/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.networking;

import java.util.Vector;

/**
 * A simple set object.
 * @author Joe Grinstead
 */
public class Set {

    private final Vector elements = new Vector();

    public synchronized boolean add(Object o) {
        if (!elements.contains(o)) {
            elements.addElement(o);
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean remove(Object o) {
        return elements.removeElement(o);
    }

    public synchronized Object get(int i) {
        return elements.elementAt(i);
    }

    public synchronized int size() {
        return elements.size();
    }
}
