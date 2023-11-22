package message;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class MessageInputStream extends InputStream {
    DataInputStream in;

    public MessageInputStream(InputStream in) {
        this(new DataInputStream(in));
    }

    public MessageInputStream(DataInputStream in) {
        this.in = in;
    }

    @Override
    public int read() throws IOException {
        return in.read();
    }

    public Message readMessage() throws IOException {
        final int HEADER_LEN = 4;
        byte[] headerBytes = new byte[HEADER_LEN];
        int nread = 0;
        while (nread < HEADER_LEN) {
            int n = in.read(headerBytes, nread, HEADER_LEN - nread);
            if (n == -1) {
                if (nread > 0) {
                    throw new RuntimeException("Incomplete packet; read " + nread + " bytes of header");
                }
                return null;
            }
            nread += n;
        }

        String header = new String(headerBytes);

        StringBuilder body = new StringBuilder();
        return switch (header) {
            case "CONN" -> {
                // read until line separator
                while (true) {
                    int b = in.read();
                    if (b == -1) {
                        throw new RuntimeException("Incomplete packet; read " + nread + " bytes of header");
                    }
                    if (b == '\n') {
                        break;
                    }
                    body.append((char) b);
                }
                yield new ConnectionMessage(body.toString());
            }
            case "CALC" -> {
                // read until line separator
                while (true) {
                    int b = in.read();
                    if (b == -1) {
                        throw new RuntimeException("Incomplete packet; read " + nread + " bytes of header");
                    }
                    if (b == '\n') {
                        break;
                    }
                    body.append((char) b);
                }
                yield new CalculationMessage(body.toString());
            }
            case "TERM" -> new TerminationMessage();
            default -> throw new RuntimeException("Illegal message type " + header);
        };
    }
}
