package dev.coll.math;

import java.util.concurrent.ThreadLocalRandom;

public class CalculationParameter {
  private final ParamType type;
  private final double maxValue;
  private final double minValue;
  private final double value;
  private final boolean nonZero;

  public CalculationParameter(double minValue, double maxValue, boolean nonZero) {
    this.type = ParamType.RANDOMIZED;
    this.value = 0;
    this.maxValue = maxValue;
    this.minValue = minValue;
    this.nonZero = nonZero;
  }

  public CalculationParameter(double value) {
    this.type = ParamType.CONSTANT;
    this.value = value;
    this.maxValue = 0;
    this.minValue = 0;
    this.nonZero = false;
  }

  public String getValueAsString() {
    if (type == ParamType.CONSTANT) return String.valueOf(value);
    ThreadLocalRandom threadRandom = ThreadLocalRandom.current();
    double result = threadRandom.nextDouble(minValue, maxValue);
    while (result == 0 && nonZero) {
      result = threadRandom.nextDouble(minValue, maxValue);
    }
    return String.valueOf(result);
  }
}
