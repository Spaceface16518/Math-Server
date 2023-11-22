package message;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MessageOutputStream extends OutputStream {
    DataOutputStream out;

    public MessageOutputStream(OutputStream out) {
        this(new DataOutputStream(out));
    }

    MessageOutputStream(DataOutputStream out) {
        this.out = out;
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }

    public void write(Message message) throws IOException {
        // header (type of message)
        String sb = message.type() +
                // write line separator
                System.lineSeparator() +
                // write packet contents
                message.body();

        out.writeBytes(sb);
    }
}
