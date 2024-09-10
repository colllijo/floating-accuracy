package dev.coll;

import java.util.LinkedList;

import dev.coll.math.Calculation;
import dev.coll.math.CalculationResult;
import dev.coll.math.CalculationStep;

public class App {
  public static void main(String[] args) {

    String expression = "({0} + {1}) * {2} / {3}";

    Calculation calc = new Calculation(expression);
    CalculationResult result = calc.evaluate(new String[] { getRandomDoubleString(), getRandomDoubleString(), getRandomDoubleString(), getRandomNonZeroDouble() });
    LinkedList<CalculationStep> steps = calc.getSteps();

    for (CalculationStep step : steps) {
      System.out.println(step);
    }

    System.out.println("Double result: " + result.getDoubleResult());
    System.out.println("BigReal result: " + result.getBigRealResult());
  }

  private static String getRandomDoubleString() {
    return String.valueOf((Math.random() * 200) - 100);
  }

  private static String getRandomNonZeroDouble() {
    double result = 0.0;
    do {
      result = (Math.random() * 200) - 100;
    } while (result == 0.0);
    return String.valueOf(result);
  }
}
