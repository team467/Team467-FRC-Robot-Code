package edu.wpi.first.smartdashboard.types;

import java.io.DataInputStream;
import java.io.IOException;

/**
 *
 * @author pmalmsten
 */
public class FloatParser implements IByteParser {
    public Object parse(DataInputStream data) throws Types.ParseFailedException {
        try {
            return data.readFloat();
        } catch (IOException ex) {
            throw new Types.ParseFailedException("Parse of float failed", ex);
        }
    }
}
