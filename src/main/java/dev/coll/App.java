package dev.coll;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.coll.math.Calculation;
import dev.coll.math.CalculationDto;
import dev.coll.math.CalculationResult;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {
  public static void main(String[] args) throws IOException {
    int sample_size = 1;

    String expression = "({0} + {1}) * {2} / {3}";

    List<CalculationDto> calculations = new ArrayList<>();


    for (int i = 0; i < sample_size; i++) {
      String[] values = new String[] { "0.2", "0.1", "2", "3" };

      Calculation calc = new Calculation(expression);
      CalculationResult result = calc.evaluate(values);

      calculations.add(new CalculationDto(result, calc.getSteps(), values));
    }

    ObjectMapper mapper = new ObjectMapper();
    BufferedWriter writer = new BufferedWriter(new FileWriter("calc.json"));
    writer.write(mapper.writeValueAsString(calculations));

    writer.close();
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
