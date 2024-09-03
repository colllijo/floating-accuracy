package dev.coll.math;

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

//  public BigReal apply(BigReal b, BigReal a) {
//    switch (this) {
//      case ADD:
//        return a.add(b);
//      case SUBTRACT:
//        return a.subtract(b);
//      case MULTIPLY:
//        return a.multiply(b);
//      case DIVIDE:
//        return a.divide(b);
//      default:
//        return BigReal.ZERO;
//    }
//  }
}
