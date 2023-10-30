package message;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MessageOutputStream extends OutputStream {
    DataOutputStream out;

    MessageOutputStream(OutputStream out) {
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
        // write header (type of message)
        out.writeUTF(message.type());
        // write line separator
        out.writeChar('\n');
        // write packet contents
        out.writeUTF(message.body());
    }
}
