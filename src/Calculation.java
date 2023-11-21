import java.util.Stack;
public class Calculation {
    Stack<String> values = new Stack<>(); // Stack used to hold all numerical values, from which they will be accessed.
    String calculate(String input) { // Function that receives an equation as a string in reverse Polish notation.
        int start = 0; // Index values marking where the substring begins and ends.
        int end;
        double result = 0; // Stores what will be returned.
        String substring = ""; // Initializes substring.
        for (int i = 0; i < input.length(); i++) { // Iterates through the input string and moves the substring indexes.
            end = i;
            //System.out.println(input.charAt(i));
            if (input.charAt(i) == ' ') { // If there is a space, it means the start of a new substring.
                substring = input.substring(start, end); // Substring is created using the indexes.
                start = i + 1; // Start index is updated so that the next substring does not include a space.

                // If the substring is equal to an operation sign, or it is the last substring (which should be an operand) it does the operation.
                if (substring.equals("+") || substring.equals("-") || substring.equals("/") || substring.equals("*") || i == input.length() - 1) {

                    // Stores values into temporary variables to check if they are numbers or not.
                    String secondValueStr = values.pop();
                    String firstValueStr = values.pop();
                    try {
                        Double.parseDouble(secondValueStr);
                        Double.parseDouble(firstValueStr);
                    } catch (NumberFormatException e) {
                        return "Wrong equation syntax.";
                    }

                    double secondValue = Double.parseDouble(secondValueStr); // If they are numbers, it converts the substrings into doubles.
                    double firstValue = Double.parseDouble(firstValueStr);

                    switch(substring) { // Checks which operation the sign represents, and carries it out.
                        case "+": // Addition.
                            result = firstValue + secondValue;
                            // Resulting value gets pushed to stack so that it can be used later on.
                            values.push(String.valueOf(result));
                            break;
                        case "-": // Subtraction.
                            result = firstValue - secondValue;
                            values.push(String.valueOf(result));
                            break;
                        case "*": // Multiplication.
                            result = firstValue * secondValue;
                            values.push(String.valueOf(result));
                            break;
                        case "/": // Division.
                            result = firstValue / secondValue;
                            values.push(String.valueOf(result));
                            break;
                        default:
                            System.out.println("Wrong equation syntax.");
                    }
                } else { // If the substring is not an operational sign, it should only be a number; it gets pushed to stack.
                    values.push(substring);
                }
            }
        }
        return String.valueOf(result);
        //Prints result.
    }
}