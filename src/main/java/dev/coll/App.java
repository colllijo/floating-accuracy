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
    final int SAMPLE_SIZE = 1000000;
    final String RESULT_DIRECTORY = "analytics";
    final double GRAVITATIONAL_CONSTANT = 6.67430e-11; // m³/kg·s²
    final double COULOMB_CONSTANT = 8.9875517923e9; // N·m²/C²
    // TODO: Add the Expression parameters

    Map<String, String> expressions = Map.of(
            "Kinematik", "({0} + {1}) * {2} / {3}",
            "Gravitation", "(6.67430e-11 *({0} * {1}))/ (({2} +{3})*({2} +{3}))",
            "Colombo", "(8.9875517923e9 *({0} * {1}))/ ({2} *{2})"
    );
    Map<String, CalculationParameter[]> params = Map.of(
            "Kinematik", new CalculationParameter[] {
                    new CalculationParameter(-100, 100, false),
                    new CalculationParameter(20.02),
                    new CalculationParameter(0, 200, false),
                    new CalculationParameter(-100, 100, true)
            },
            "Gravitation",new CalculationParameter[] {
                    new CalculationParameter(1e23, 1e28, false) //Min: Merkur masse /3 Max: Unter Braune Zwerg Masse 13 Jupitermasse
                    ,
                    new CalculationParameter(20.02),
                    new CalculationParameter(0, 200, false),
                    new CalculationParameter(-100, 100, true)
            }
    );
    String[] keys = expressions.keySet().toArray(new String[0]);

    ObjectMapper mapper = new ObjectMapper();

    for (int i = 0; i < keys.length; i++) {
      BufferedWriter writer = new BufferedWriter(new FileWriter(new File(new File(RESULT_DIRECTORY), keys[i] + "-result.json")));
      writer.write("[");

      for (int j = 0; j < SAMPLE_SIZE; j++) {
        String[] values = Arrays.stream(params.get(keys[i]))
                .map(CalculationParameter::getValueAsString)
                .toArray(String[]::new);

        Calculation calc = new Calculation(expressions.get(keys[i]));
        CalculationResult result = calc.evaluate(values);
        CalculationDto dto = new CalculationDto(result, calc.getSteps(), values);

        writer.write(mapper.writeValueAsString(dto));
        if (j < SAMPLE_SIZE - 1) { writer.write(","); }
      }

      writer.write("]");
      writer.close();
    }
  }
}
