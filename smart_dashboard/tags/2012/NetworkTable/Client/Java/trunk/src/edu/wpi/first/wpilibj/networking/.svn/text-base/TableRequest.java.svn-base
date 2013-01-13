/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.networking;

/**
 *
 * @author Joe
 */
public class TableRequest implements Data {

    private final NetworkTable table;
    private final String name;

    public TableRequest(String name, NetworkTable table) {
        this.table = table;
        this.name = name;
    }

    public void encode(Buffer buffer) {
        buffer.writeByte(Data.TABLE_REQUEST);
        buffer.writeString(name);
        table.encodeName(buffer);
    }

}
