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

    Map<String, String> expressions = Map.of(
        "kinematics", "{0} + {1} * {2} -(1/2) * {3} * ({2} * {2})", // s=s0+v*t-(1/2)*a*(t*t)
        "gravitational-force", "({0} * ({1} * {2})) / (({3} + {4}) * ({3} + {4}))", // FG=(G*(M*m))/((R+h)*(R+h))
        "coulomb-force", "({0} * ({1} * {2})) / ({3} * {3})" // FC=( k *(q1*q2))/(r*r)
    );
    Map<String, CalculationParameter[]> params = Map.of(
        // https://www.autoscout24.de/informieren/ratgeber/beste-autos/die-schnellsten-autos-der-welt/ 12.11.24
        // https://www.carwow.de/automagazin/beste-autos/beste-autos-nach-autotypen/top-10-schnellste-autos-der-welt-2024 12.11.24
        // https://www.adac-fahrtraining.de/tipps-fuer-lange-urlaubsfahrten 12.11.24
        // https://de.wikipedia.org/wiki/Supersportwagen 12.11.24
        "kinematics", new CalculationParameter[] {
            new CalculationParameter(0, 500000, false), // ADAC empfiehlt nicht mehr als 500 Km zu fahren für eine lange Reise.
            new CalculationParameter(-130, 130, false), // Es sollte 130 Meter pro Sekunde sein → 475 km/h schnellste zugelassen Auto
            new CalculationParameter(0, 86400, false), // Es sollte ein Tag als Maximum
            new CalculationParameter(-15, 15, false), // Es sollte 15 Meter pro Sekunde im Quadrat sein -> stärkste beschleunigte zugelassen Auto
        },
        // https://de.wikipedia.org/wiki/Liste_der_Planeten_des_Sonnensystems 12.11.24
        // https://de.wikipedia.org/wiki/Brauner_Zwerg 12.11.24
        "gravitational-force", new CalculationParameter[] {
            new CalculationParameter(GRAVITATIONAL_CONSTANT),
            new CalculationParameter(1e23, 1e28, false), // Min: Merkur masse /3 Max: Unter Braune Zwerg Masse 13 Jupitermasse
            new CalculationParameter(0, 1e28, false), // Min: 0 wie Menschen, Kuchen etc. Max: Unter Braune Zwerg Masse 13 Jupitermasse
            new CalculationParameter(0, 1e6, true), // Min: 0 wie Kugelradius etc. Max: Unter dem Radius vom Braune Zwerg
            new CalculationParameter(0, 1e16, false)// Min: 0 Abstandgegenstände etc. Max: Lichtjahr Abstand
        },
        // https://de.wikipedia.org/wiki/Elektrische_Ladung 12.11.24
        // https://de.wikipedia.org/wiki/%C3%85ngstr%C3%B6m_(Einheit) 12.11.24
        "coulomb-force", new CalculationParameter[] {
            new CalculationParameter(COULOMB_CONSTANT),
            new CalculationParameter(-1e-19, 1e-19, false), // Min&Max: Typische Ladungen von Partikel
            new CalculationParameter(-1e-19, 1e-19, false), // Min&Max: Typische Ladungen von Partikel
            new CalculationParameter(1e-10, 1, true),// Min: 1 Angstrom Max:1 Meter
        });

    String[] keys = expressions.keySet().toArray(new String[0]);
    ObjectMapper mapper = new ObjectMapper();

    for (int i = 0; i < keys.length; i++) {
      BufferedWriter writer = new BufferedWriter(
          new FileWriter(new File(new File(RESULT_DIRECTORY), keys[i] + "-result.json")));
      writer.write("[");

      for (int j = 0; j < SAMPLE_SIZE; j++) {
        String[] values = Arrays.stream(params.get(keys[i]))
            .map(CalculationParameter::getValueAsString)
            .toArray(String[]::new);

        Calculation calc = new Calculation(expressions.get(keys[i]));
        CalculationResult result = calc.evaluate(values);
        CalculationDto dto = new CalculationDto(result, calc.getSteps(), values);

        writer.write(mapper.writeValueAsString(dto));
        if (j < SAMPLE_SIZE - 1) {
          writer.write(",");
        }
      }

      writer.write("]");
      writer.close();
    }
  }
}
