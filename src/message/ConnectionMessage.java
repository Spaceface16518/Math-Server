package message;

public class ConnectionMessage extends Message {
    String clientName;

    public ConnectionMessage(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public String body() {
        return clientName;
    }

    @Override
    public String type() {
        return "CONN";
    }
}
