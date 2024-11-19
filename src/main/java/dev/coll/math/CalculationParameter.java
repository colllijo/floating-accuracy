package dev.coll.math;

import java.util.concurrent.ThreadLocalRandom;

public class CalculationParameter {
  private final ParamType type;
  private final float maxValue;
  private final float minValue;
  private final float value;
  private final boolean nonZero;

  public CalculationParameter(float minValue, float maxValue, boolean nonZero) {
    this.type = ParamType.RANDOMIZED;
    this.value = 0;
    this.maxValue = maxValue;
    this.minValue = minValue;
    this.nonZero = nonZero;
  }

  public CalculationParameter(float value) {
    this.type = ParamType.CONSTANT;
    this.value = value;
    this.maxValue = 0;
    this.minValue = 0;
    this.nonZero = false;
  }

  public String getValueAsString() {
    if (type == ParamType.CONSTANT) return String.valueOf(value);

    ThreadLocalRandom threadRandom = ThreadLocalRandom.current();
    float result = threadRandom.nextFloat(minValue, maxValue);
    while (result == 0 && nonZero) {
      result = threadRandom.nextFloat(minValue, maxValue);
    }
    return String.valueOf(result);
  }
}
