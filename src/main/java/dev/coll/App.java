package dev.coll;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.coll.math.Calculation;
import dev.coll.math.CalculationDto;
import dev.coll.math.CalculationResult;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
  public static void main(String[] args) throws IOException {
    int sample_size = 1;

    Map<String, String> expressions = Map.of(
            "default", "({0} + {1}) * {2} / {3}"
    );
    String[] keys = expressions.keySet().toArray(new String[0]);

    List<CalculationDto> calculations = new ArrayList<>();

    for (int i = 0; i < keys.length; i++) {

      for (int j = 0; j < sample_size; j++) {
        String[] values = new String[] { "0.2", "0.1", "2", "3" };

        Calculation calc = new Calculation(expressions.get(keys[i]));
        CalculationResult result = calc.evaluate(values);

        calculations.add(new CalculationDto(result, calc.getSteps(), values));
      }

      ObjectMapper mapper = new ObjectMapper();
      BufferedWriter writer = new BufferedWriter(new FileWriter(keys[i] + "-result.json"));
      writer.write(mapper.writeValueAsString(calculations));

      writer.close();
    }
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
