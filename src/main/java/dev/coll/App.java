package dev.coll;

import dev.coll.math.Calculation;
import dev.coll.math.CalculationResult;

public class App {
  public static void main(String[] args) {
    String expression = "({0} + {1}) * {2}";

    Calculation calc = new Calculation(expression);
    CalculationResult result = calc.evaluate(new String[] { "0.1", "0.2", "2" });

    System.out.println("Double result: " + result.getDoubleResult());
    System.out.println("BigDecimal result: " + result.getBigDecimalResult());
  }
}
