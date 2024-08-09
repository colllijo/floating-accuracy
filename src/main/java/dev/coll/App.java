package dev.coll;

import java.util.LinkedList;

import dev.coll.math.Calculation;
import dev.coll.math.CalculationResult;
import dev.coll.math.CalculationStep;

public class App {
  public static void main(String[] args) {
    String expression = "({0} + {1}) * {2}";

    Calculation calc = new Calculation(expression);
    CalculationResult result = calc.evaluate(new String[] { "0.1", "0.2", "2" });
    LinkedList<CalculationStep> steps = calc.getSteps();

    for (CalculationStep step : steps) {
      System.out.println(step);
    }

    System.out.println("Double result: " + result.getDoubleResult());
    System.out.println("BigDecimal result: " + result.getBigDecimalResult());
  }
}
