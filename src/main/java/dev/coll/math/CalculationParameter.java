package dev.coll.math;

import java.util.UUID;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

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

    UUID uuid = UUID.randomUUID();
    long seed = uuid.getMostSignificantBits() ^ uuid.getLeastSignificantBits();
    RandomGenerator randomGenerator = RandomGeneratorFactory.of("L64X128MixRandom").create(seed);

    double result;
    if (maxValue > 1e6) {
      // Use logarithmic scale for large bounds, so that the distribution is more uniform
      double logMin = Math.log(minValue + 1); // +1 to avoid log(0)
      double logMax = Math.log(maxValue + 1);
      result = Math.exp(randomGenerator.nextDouble(logMin, logMax)) - 1;
    } else {
      result = randomGenerator.nextDouble(minValue, maxValue);
    }

    while (result == 0 && nonZero) {
      if (maxValue > 1e6) {
        double logMin = Math.log(minValue + 1);
        double logMax = Math.log(maxValue + 1);
        result = Math.exp(randomGenerator.nextDouble(logMin, logMax)) - 1;
      } else {
        result = randomGenerator.nextDouble(minValue, maxValue);
      }
    }

    return String.valueOf(result);
  }
}
