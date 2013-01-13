/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.networking;

/**
 *
 * @author Joe
 */
public interface NetworkListener {

    public void valueChanged(String key, Object value);

    public void valueConfirmed(String key, Object value);
}
