package dev.coll.math;

import java.util.LinkedList;

public class CalculationDto {
  public CalculationResult result;
  public LinkedList<CalculationStep> steps;
  public String[] values;

  public CalculationDto(CalculationResult result, LinkedList<CalculationStep> steps, String[] values) {
    this.result = result;
    this.steps = steps;
    this.values = values;
  }
}
