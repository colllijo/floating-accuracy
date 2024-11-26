package dev.coll.math;

public class CalculationResult {
  private String doubleResult;
  private String bigDecimalResult;
  private String resultDifference;

  public CalculationResult(String doubleResult, String bigDecimalResult, String resultDifference) {
    this.doubleResult = doubleResult;
    this.bigDecimalResult = bigDecimalResult;
    this.resultDifference = resultDifference;
  }

  public String getDoubleResult() {
    return doubleResult;
  }

  public String getBigRealResult() {
    return bigDecimalResult;
  }

  public String getResultDifference() {
    return resultDifference;
  }
}
