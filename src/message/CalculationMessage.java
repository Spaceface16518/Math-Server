package message;

public class CalculationMessage extends Message {
    String expression;

    public CalculationMessage(String expression) {
        this.expression = expression;
    }

    @Override
    public String type() {
        return "CALC";
    }

    @Override
    public String body() {
        return expression;
    }
}
