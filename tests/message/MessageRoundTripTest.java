package message;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class MessageRoundTripTest {

    @Test
    void testConnectionMessage() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MessageOutputStream messageOutputStream = new MessageOutputStream(out);

        Message message = new ConnectionMessage("name");
        try {
            messageOutputStream.write(message);
        } catch (IOException e) {
            fail(e);
        }

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        MessageInputStream messageInputStream = new MessageInputStream(in);

        try {
            Message readMessage = messageInputStream.readMessage();
            assertEquals(message, readMessage);
        } catch (IOException e) {
            fail(e);
        }
    }

    @Test
    void testCalculationMessage() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MessageOutputStream messageOutputStream = new MessageOutputStream(out);

        Message message = new CalculationMessage("1 + 1");
        try {
            messageOutputStream.write(message);
        } catch (IOException e) {
            fail(e);
        }

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        MessageInputStream messageInputStream = new MessageInputStream(in);

        try {
            Message readMessage = messageInputStream.readMessage();
            assertEquals(message, readMessage);
        } catch (IOException e) {
            fail(e);
        }
    }

    @Test
    void testTerminationMessage() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MessageOutputStream messageOutputStream = new MessageOutputStream(out);

        Message message = new TerminationMessage();
        try {
            messageOutputStream.write(message);
        } catch (IOException e) {
            fail(e);
        }

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        MessageInputStream messageInputStream = new MessageInputStream(in);

        try {
            Message readMessage = messageInputStream.readMessage();
            assertEquals(message, readMessage);
        } catch (IOException e) {
            fail(e);
        }
    }

}