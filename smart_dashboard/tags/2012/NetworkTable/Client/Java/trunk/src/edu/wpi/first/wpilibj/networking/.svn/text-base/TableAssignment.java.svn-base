/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.networking;

/**
 *
 * @author Joe
 */
class TableAssignment implements Data {

    private final NetworkTable table;
    private final Integer alteriorId;

    public TableAssignment(NetworkTable table, Integer alteriorId) {
        this.table = table;
        this.alteriorId = alteriorId;
    }

    public void encode(Buffer buffer) {
        buffer.writeByte(Data.TABLE_ASSIGNMENT);
        buffer.writeTableId(alteriorId.intValue());
        table.encodeName(buffer);
    }

}
