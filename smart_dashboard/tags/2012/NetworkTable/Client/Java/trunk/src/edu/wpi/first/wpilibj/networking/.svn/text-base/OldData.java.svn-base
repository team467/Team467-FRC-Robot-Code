/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.networking;

/**
 *
 * 
 * @author Joe
 */
class OldData implements Data {
    
    final Entry entry;

    public OldData(Entry data) {
        this.entry = data;
    }

    public void encode(Buffer buffer) {
        buffer.writeByte(Data.OLD_DATA);
        entry.encode(buffer);
    }

}
