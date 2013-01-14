package edu.wpi.first.smartdashboard.types;

import java.io.DataInputStream;
import java.io.IOException;

/**
 *
 * @author pmalmsten
 */
public class DoubleParser implements IByteParser {
    public Object parse(DataInputStream data) throws Types.ParseFailedException {
        try {
            return data.readDouble();
        } catch (IOException ex) {
            throw new Types.ParseFailedException("Parse of double failed", ex);
        }
    }
}
