package dev.coll.math;

public record CalculationStep(String dValue, String bdValue, String difference) {
  public String toString() {
    return String.format("Float: %s,\t BigReal: %s,\t Difference: %s", dValue, bdValue, difference);
  }
}
