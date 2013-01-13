package edu.wpi.first.smartdashboard.types;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author jerrym
 */
public class Utf8Parser implements IByteParser {
    public Object parse(DataInputStream data) throws Types.ParseFailedException {
        try {
            return data.readUTF();
        } catch (IOException ex) {
            throw new Types.ParseFailedException("Failed to parse UTF-8 string", ex);
        }
    }

}
