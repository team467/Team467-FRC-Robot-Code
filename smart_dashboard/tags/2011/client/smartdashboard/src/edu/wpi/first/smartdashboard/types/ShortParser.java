package edu.wpi.first.smartdashboard.types;

import java.io.DataInputStream;
import java.io.IOException;

/**
 *
 * @author pmalmsten
 */
public class ShortParser implements IByteParser {
    public Object parse(DataInputStream data) throws Types.ParseFailedException {
        try {
            return data.readShort();
        } catch (IOException ex) {
            throw new Types.ParseFailedException("Parse of short failed", ex);
        }
    }
}
