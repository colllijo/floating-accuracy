package dev.coll.math;

public record CalculationStep(String dValue, String bdValue, String difference) {
  public String toString() {
    return String.format("Double: %s,\t BigReal: %s,\t Difference: %s", dValue, bdValue, difference);
  }
}
