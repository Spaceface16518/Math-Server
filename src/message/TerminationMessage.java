package message;

public class TerminationMessage extends Message {
    public TerminationMessage() {
    }

    @Override
    public String type() {
        return "TERM";
    }

    @Override
    public String body() {
        return "";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof TerminationMessage;
    }
}
