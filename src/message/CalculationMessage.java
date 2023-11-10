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

    @Override
    public boolean equals(Object o) {
        if (o instanceof CalculationMessage other) {
            return expression.equals(other.expression);
        }
        return false;
    }
}
