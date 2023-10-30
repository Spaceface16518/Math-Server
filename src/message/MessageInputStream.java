package message;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MessageInputStream extends InputStream {
    DataInputStream in;

    MessageInputStream(InputStream in) {
        this(new DataInputStream(in));
    }

    MessageInputStream(DataInputStream in) {
        this.in = in;
    }

    @Override
    public int read() throws IOException {
        return in.read();
    }

    public Message readMessage() throws IOException {
        final int HEADER_LEN = 5;
        byte[] headerBytes = new byte[HEADER_LEN];
        int n = in.read(headerBytes);
        if (n != HEADER_LEN)
            throw new RuntimeException("Incomplete packet; length " + n + " was less than " + HEADER_LEN);

        String header = new String(headerBytes);

        return switch (header) {
            case "CONN" -> new ConnectionMessage(in.readUTF());
            case "CALC" -> new CalculationMessage(in.readUTF());
            case "TERM" -> new TerminationMessage();
            default -> throw new RuntimeException("Illegal message type " + header);
        };
    }
}