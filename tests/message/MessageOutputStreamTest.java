package message;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class MessageOutputStreamTest {

    void assertWrittenEquals(String expected, Message message) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MessageOutputStream messageOutputStream = new MessageOutputStream(out);

        try {
            messageOutputStream.write(message);
            messageOutputStream.flush();
            messageOutputStream.close();
        } catch (IOException e) {
            fail(e);
        }

        assertEquals(expected, out.toString());
    }

    @Test
    void connection() {
        Message message = new ConnectionMessage("name");
        String expected = "CONNname";
        assertWrittenEquals(expected, message);
    }

    @Test
    void calculation() {
        Message message = new CalculationMessage("1 + 1");
        String expected = "CALC1 + 1";
        assertWrittenEquals(expected, message);
    }

    @Test
    void termination() {
        Message message = new TerminationMessage();
        String expected = "TERM";
        assertWrittenEquals(expected, message);
    }
}