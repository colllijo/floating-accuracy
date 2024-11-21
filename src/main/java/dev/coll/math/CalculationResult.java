package dev.coll.math;

public class CalculationResult {
  private String floatResult;
  private String bigDecimalResult;
  private String resultDifference;

  public CalculationResult(String floatResult, String bigDecimalResult, String resultDifference) {
    this.floatResult = floatResult;
    this.bigDecimalResult = bigDecimalResult;
    this.resultDifference = resultDifference;
  }

  public String getFloatResult() {
    return floatResult;
  }

  public String getBigRealResult() {
    return bigDecimalResult;
  }

  public String getResultDifference() {
    return resultDifference;
  }
}
