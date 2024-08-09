package dev.coll.math;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Stack;

public class Calculation {
  String expression;

  public Calculation(String expression) {
    this.expression = expression;
  }

  public CalculationResult evaluate(String[] values) {
    char[] tokens = MessageFormat.format(expression, (Object[]) values).toCharArray();

    Stack<Double> doubleValues = new Stack<>();
    Stack<BigDecimal> bigDecimalvalues = new Stack<>();
    Stack<Character> operators = new Stack<>();

    // Iterate through each character in the expression
    for (int i = 0; i < tokens.length; i++) {
      if (tokens[i] == ' ')
        continue;

      if (Character.isDigit(tokens[i]) || tokens[i] == '.') {
        StringBuilder value = new StringBuilder();
        while (i < tokens.length
              && (Character.isDigit(tokens[i]) || tokens[i] == '.')) {
          value.append(tokens[i]);
          i++;
        }

        doubleValues.push(Double.parseDouble(value.toString()));
        bigDecimalvalues.push(new BigDecimal(value.toString()));

        i--;
      } else if (tokens[i] == '(') {
        operators.push(tokens[i]);
      } else if (tokens[i] == ')') {
        while (operators.peek() != '(') {
          char operator = operators.pop();

          doubleValues.push(applyOperator(operator, doubleValues.pop(), doubleValues.pop()));
          bigDecimalvalues.push(applyOperator(operator, bigDecimalvalues.pop(), bigDecimalvalues.pop()));
        }

        operators.pop();
      } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') {
        while (!operators.isEmpty()
              && hasPrecedence(tokens[i], operators.peek())) {
          char operator = operators.pop();

          doubleValues.push(applyOperator(operator, doubleValues.pop(), doubleValues.pop()));
          bigDecimalvalues.push(applyOperator(operator, bigDecimalvalues.pop(), bigDecimalvalues.pop()));
        }

        operators.push(tokens[i]);
      }
    }

    // Process any remaining operators in the stack
    while (!operators.isEmpty()) {
      char operator = operators.pop();
      doubleValues.push(applyOperator(operator, doubleValues.pop(), doubleValues.pop()));
      bigDecimalvalues.push(applyOperator(operator, bigDecimalvalues.pop(), bigDecimalvalues.pop()));
    }

    // The result is the only remaining element in the
    // values stack
    return new CalculationResult(doubleValues.pop().toString(), bigDecimalvalues.pop().toString());
  }

  // Function to check if operator1 has higher precedence
  // than operator2
  private boolean hasPrecedence(char operator1, char operator2) {
    if (operator2 == '(' || operator2 == ')') return false;
    return (operator1 != '*' && operator1 != '/')
        || (operator2 != '+' && operator2 != '-');
  }

  private double applyOperator(char operator, double b, double a) {
    switch (operator) {
      case '+':
        return a + b;
      case '-':
        return a - b;
      case '*':
        return a * b;
      case '/':
        if (b == 0) throw new ArithmeticException("Cannot divide by zero");
        return a / b;
    }
    return 0;
  }

  private BigDecimal applyOperator(char operator, BigDecimal b, BigDecimal a) {
    switch (operator) {
      case '+':
        return a.add(b);
      case '-':
        return a.subtract(b);
      case '*':
        return a.multiply(b);
      case '/':
        return a.divide(b);
    }
    return BigDecimal.ZERO;
  }
}
