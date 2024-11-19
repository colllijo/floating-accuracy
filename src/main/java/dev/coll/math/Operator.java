package dev.coll.math;

import org.apache.commons.math3.util.BigReal;

public enum Operator {
  ADD, SUBTRACT, MULTIPLY, DIVIDE, POWER;

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
      case '^':
        return POWER;
      default:
        return null;
    }
  }

  // Function to check if operator1 has higher precedence
  // than operator2
  public static boolean hasPrecedence(char operator1, char operator2) {
    if (operator2 == '(' || operator2 == ')') return false;
    if (fromChar(operator1) == POWER) return false;
    return (fromChar(operator1) != MULTIPLY && fromChar(operator1) != DIVIDE)
            || (fromChar(operator2) != ADD && fromChar(operator2) != SUBTRACT);
  }

  public float apply(float b, float a) {
    switch (this) {
      case ADD:
        return a + b;
      case SUBTRACT:
        return a - b;
      case MULTIPLY:
        return a * b;
      case DIVIDE:
        return a / b;
      case POWER:
        return (float) Math.pow(a, b);
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
      case POWER:
        BigReal result = a;
        int exponent = b.bigDecimalValue().intValueExact();
        for (int i = 1; i < exponent; i++) {
          result = result.multiply(a);
        }
        return result;
      default:
        return BigReal.ZERO;
    }
  }
}
