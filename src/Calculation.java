import java.util.Stack;
public class Calculation {
    Stack<String> signs = new Stack<>();
    Stack<String> values = new Stack<>();
    String calculate(String input) {
        int start = 0;
        int end ;
        double result = 0;

        String substring = "";
        for (int i = 0; i < input.length(); i++) {
            end = i;
            if (input.charAt(i) == ' ') {
                substring = input.substring(start, end);
                start = i + 1;
                if (substring.equals("+") || substring.equals("-") || substring.equals("/") || substring.equals("*")) {
                    double secondValue = Double.parseDouble(values.pop());
                    double firstValue = Double.parseDouble(values.pop());
                    switch(substring) {
                        case "+":
                            result = firstValue + secondValue;
                            values.push(String.valueOf(result));
                            //System.out.println(firstValue + " + " + secondValue + " = " + result);
                            break;
                        case "-":
                            result = firstValue - secondValue;
                            values.push(String.valueOf(result));
                            //System.out.println(firstValue + " - " + secondValue + " = " + result);
                            break;
                        case "*":
                            result = firstValue * secondValue;
                            values.push(String.valueOf(result));
                            //System.out.println(firstValue + " * " + secondValue + " = " + result);
                            break;
                        case "/":
                            result = firstValue / secondValue;
                            values.push(String.valueOf(result));
                            //System.out.println(firstValue + " / " + secondValue + " = " + result);
                            break;
                        default:
                            System.out.println("Eraro");
                    }
                } else {
                    values.push(substring);
                }
            }
        }
        return String.valueOf(result);
    }

}