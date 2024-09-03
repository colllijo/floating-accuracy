package dev.coll.math;

import org.apache.commons.math3.util.BigReal;

public enum Operator {
  ADD, SUBTRACT, MULTIPLY, DIVIDE;

  public static Operator fromChar(char c) {
    switch (c) {
      case '+':
        return ADD;
      case '-':
        return SUBTRACT;
      case '*':
        return MULTIPLY;
      case '/':
        return DIVIDE;
      default:
        return null;
    }
  }

  // Function to check if operator1 has higher precedence
  // than operator2
  public static boolean hasPrecedence(char operator1, char operator2) {
    if (operator2 == '(' || operator2 == ')') return false;
    return (fromChar(operator1) != MULTIPLY && fromChar(operator1) != DIVIDE)
            || (fromChar(operator2) != ADD && fromChar(operator2) != SUBTRACT);
  }

  public double apply(double b, double a) {
    switch (this) {
      case ADD:
        return a + b;
      case SUBTRACT:
        return a - b;
      case MULTIPLY:
        return a * b;
      case DIVIDE:
        return a / b;
      default:
        return 0;
    }
  }

  public BigReal apply(BigReal b, BigReal a) {
    switch (this) {
      case ADD:
        return a.add(b);
      case SUBTRACT:
        return a.subtract(b);
      case MULTIPLY:
        return a.multiply(b);
      case DIVIDE:
        return a.divide(b);
      default:
        return BigReal.ZERO;
    }
  }
}
