package dev.coll.math;

public class CalculationResult {
  private String doubleResult;
  private String bigDecimalResult;

  public CalculationResult(String doubleResult, String bigDecimalResult) {
    this.doubleResult = doubleResult;
    this.bigDecimalResult = bigDecimalResult;
  }

  public String getDoubleResult() {
    return doubleResult;
  }

  public String getBigRealResult() {
    return bigDecimalResult;
  }
}
