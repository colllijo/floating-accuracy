package dev.coll;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.coll.math.Calculation;
import dev.coll.math.CalculationDto;
import dev.coll.math.CalculationParameter;
import dev.coll.math.CalculationResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class App {
  public static void main(String[] args) throws IOException {
    final int SAMPLE_SIZE = 1;
    final String RESULT_DIRECTORY = "analytics";

    Map<String, String> expressions = Map.of(
            "default", "({0} + {1}) * {2} / {3}"
    );
    Map<String, CalculationParameter[]> params = Map.of(
            "default", new CalculationParameter[] {
                    new CalculationParameter(-100, 100, false),
                    new CalculationParameter(20.02),
                    new CalculationParameter(0, 200, false),
                    new CalculationParameter(-100, 100, true)
            }
    );
    String[] keys = expressions.keySet().toArray(new String[0]);

    List<CalculationDto> calculations = new ArrayList<>();

    for (int i = 0; i < keys.length; i++) {
      for (int j = 0; j < SAMPLE_SIZE; j++) {
        String[] values = Arrays.stream(params.get(keys[i]))
                .map(CalculationParameter::getValueAsString)
                .toArray(String[]::new);

        Calculation calc = new Calculation(expressions.get(keys[i]));
        CalculationResult result = calc.evaluate(values);

        calculations.add(new CalculationDto(result, calc.getSteps(), values));
      }

      ObjectMapper mapper = new ObjectMapper();
      BufferedWriter writer = new BufferedWriter(new FileWriter(new File(new File(RESULT_DIRECTORY), keys[i] + "-result.json")));
      writer.write(mapper.writeValueAsString(calculations));

      writer.close();
    }
  }
}
