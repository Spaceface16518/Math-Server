package message;

public abstract class Message {

    /**
     * @return The type of the message, used as the header of the packet.
     */
    public abstract String type();


    /**
     * @return The contents of the message, used as the body of the packet.
     */
    public abstract String body();
}
