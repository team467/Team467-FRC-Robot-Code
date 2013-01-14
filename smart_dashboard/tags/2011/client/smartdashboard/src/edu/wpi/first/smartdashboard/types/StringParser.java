package edu.wpi.first.smartdashboard.types;

import java.io.DataInputStream;
import java.io.IOException;

/**
 *
 * @author pmalmsten
 */
public class StringParser implements IByteParser {
    public Object parse(DataInputStream data) throws Types.ParseFailedException {

        StringBuilder sb = new StringBuilder();
        try {
            while (data.available() > 0) {
                sb.append(data.readChar());
            }
        } catch (IOException ex) {
            throw new Types.ParseFailedException("Failed to parse string", ex);
        }
        
        return sb.toString();
    }
}
