package dev.coll.math;

import org.apache.commons.math3.util.BigReal;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.Stack;

import static dev.coll.math.Operator.hasPrecedence;

public class Calculation {
  private final String expression;

  LinkedList<CalculationStep> steps = new LinkedList<>();

  public Calculation(String expression) {
    this.expression = expression;
  }

  public LinkedList<CalculationStep> getSteps() {
    return steps;
  }

  public CalculationResult evaluate(String[] values) {
    char[] tokens = MessageFormat.format(expression, (Object[]) values).toCharArray();

    Stack<Double> doubleValues = new Stack<>();
    Stack<BigReal> bigRealValues = new Stack<>();
    Stack<Character> operators = new Stack<>();

    // Iterate through each character in the expression
    for (int i = 0; i < tokens.length; i++) {
      if (tokens[i] == ' ')
        continue;

      if (Character.isDigit(tokens[i]) || tokens[i] == '.' || (tokens[i] == '-' && i + 1 < tokens.length && Character.isDigit(tokens[i + 1]))) {
        StringBuilder value = new StringBuilder();
        do {
          value.append(tokens[i]);
          i++;
        } while (i < tokens.length && (Character.isDigit(tokens[i]) || tokens[i] == '.'));

        doubleValues.push(Double.parseDouble(value.toString()));
        bigRealValues.push(new BigReal(value.toString()));

        i--;
      } else if (tokens[i] == '(') {
        operators.push(tokens[i]);
      } else if (tokens[i] == ')') {
        while (operators.peek() != '(') {
          char operator = operators.pop();

          try {
              doubleValues.push(Operator.fromChar(operator).apply(doubleValues.pop(), doubleValues.pop()));
              bigRealValues.push(Operator.fromChar(operator).apply(bigRealValues.pop(), bigRealValues.pop()));
          } catch (NullPointerException e) {
              System.err.println("Character '" + operator + "' is not a valid operator.");
          }
          addStep(doubleValues.peek(), bigRealValues.peek());
        }

        operators.pop();
      } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/' || tokens[i] == '^') {
        while (!operators.isEmpty()
              && hasPrecedence(tokens[i], operators.peek())) {
          char operator = operators.pop();

          try {
              doubleValues.push(Operator.fromChar(operator).apply(doubleValues.pop(), doubleValues.pop()));
              bigRealValues.push(Operator.fromChar(operator).apply(bigRealValues.pop(), bigRealValues.pop()));
          } catch (NullPointerException e) {
              System.err.println("Character '" + operator + "' is not a valid operator.");
          }
          addStep(doubleValues.peek(), bigRealValues.peek());
        }

        operators.push(tokens[i]);
      }
    }

    // Process any remaining operators in the stack
    while (!operators.isEmpty()) {
      char operator = operators.pop();

      try {
          doubleValues.push(Operator.fromChar(operator).apply(doubleValues.pop(), doubleValues.pop()));
          bigRealValues.push(Operator.fromChar(operator).apply(bigRealValues.pop(), bigRealValues.pop()));
      } catch (NullPointerException e) {
          System.err.println("Character '" + operator + "' is not a valid operator.");
      }
      addStep(doubleValues.peek(), bigRealValues.peek());
    }

    // The result is the only remaining element in the
    // values stack
    return new CalculationResult(doubleValues.pop().toString(), bigRealValues.pop().bigDecimalValue().toString(), steps.getLast().difference());
  }

  private void addStep(Double dValue, BigReal brValue) {
    BigReal difference = brValue.subtract(new BigReal(dValue));

    steps.add(new CalculationStep(dValue.toString(), brValue.bigDecimalValue().toString(), difference.bigDecimalValue().toString()));
  }



}
