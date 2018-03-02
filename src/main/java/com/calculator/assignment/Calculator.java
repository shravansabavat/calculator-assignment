package com.calculator.assignment;

import static com.calculator.assignment.utils.CalculatorUtils.VARIABLE_REGEX;
import static com.calculator.assignment.utils.CalculatorUtils.getExpressionWithOperators;
import static com.calculator.assignment.utils.CalculatorUtils.isOperator;
import static com.calculator.assignment.utils.CalculatorUtils.isPlainVariable;
import static com.calculator.assignment.utils.CalculatorUtils.validateVerboseExpresion;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.log4j.Logger;

import com.calculator.assignment.exceptions.InvalidExpressionException;
import com.calculator.assignment.utils.CalculatorUtils;

public class Calculator {
    private final static Logger LOGGER = Logger.getLogger(Calculator.class);

    private Stack<String> expressionTracker = new Stack<String>();
    private Map<String, String> assignments = new HashMap<String, String>();
    private Map<String, String> variablesToFind = new HashMap<String, String>();

    public int evaluateExpression(String input) throws Exception {
        validateVerboseExpresion(input);
        String expression = getExpressionWithOperators(input);

        String[] details = expression.split(",");
        for (String currentValue : details) {
            // remove already found variable if we trying to assign again
            if (!expressionTracker.isEmpty() && isAssignment(expressionTracker.peek())
                    && isPlainVariable(currentValue)) {
                assignments.remove(currentValue);
            }

            if (isOperator(currentValue) || isVariable(currentValue)) {
                LOGGER.debug("Pushing " + currentValue + " into stack");
                expressionTracker.push(currentValue);

                if (isVariable(currentValue)) {
                    variablesToFind.put(currentValue, "");
                }

                continue;
            }

            if (isNumericaValue(currentValue) && expressionTracker.size() > 1) {
                if (isAssignment(expressionTracker.peek())) {
                    throw new InvalidExpressionException("LET operator should be folloed by a variable");
                }

                String previousValue = expressionTracker.pop();
                LOGGER.debug("Popped " + previousValue + " from stack");

                if (isNumericaValue(previousValue) && isOperator(expressionTracker.peek())) {
                    String operator = expressionTracker.pop();
                    LOGGER.debug("Performing operation " + operator + " on values " + previousValue + "," + currentValue
                            + "from stack");
                    currentValue = performOperation(operator, previousValue, currentValue) + "";
                    LOGGER.debug("Pushing result from operation" + currentValue + " into stack");
                    expressionTracker.push(currentValue);
                    evaluatePossibleExpressions();
                } else if (canAssignVariable(previousValue) && isAssignment(expressionTracker.peek())) {
                    expressionTracker.pop();
                    LOGGER.debug("Assigning variable:" + previousValue + " with value:" + currentValue);
                    assignValue(previousValue, currentValue);
                } else {
                    LOGGER.debug("Pushing " + previousValue + " into stack");
                    expressionTracker.push(previousValue);
                    LOGGER.debug("Pushing " + currentValue + " into stack");
                    expressionTracker.push(currentValue);
                }
            } else {
                if (isNumericaValue(currentValue) && isAssignment(expressionTracker.peek())) {
                    throw new InvalidExpressionException("LET operator should be folloed by a variable");
                }
                LOGGER.debug("Pushing " + currentValue + " into stack");
                expressionTracker.push(currentValue);
            }
        }

        if (!expressionTracker.isEmpty() && expressionTracker.size() > 2) {
            String first = expressionTracker.pop();
            LOGGER.debug("Popped " + first + " from stack");
            String currentValue = getValue(first);
            while (expressionTracker.size() > 1) {
                String second = expressionTracker.pop();
                LOGGER.debug("Popped " + second + " from stack");
                String previousValue = getValue(second);
                if (isOperator(expressionTracker.peek())) {
                    String operator = expressionTracker.pop();
                    currentValue = performOperation(operator, currentValue, previousValue) + "";
                    expressionTracker.push(currentValue);
                    LOGGER.debug("Pushing result from operation" + currentValue + " into stack");
                }
            }
        }

        if (variablesToFind.size() > 0) {
            variablesToFind.keySet().stream().forEach(variable -> {
                LOGGER.error("Invalid expression, cannot assign variable:" + variable);
            });
            throw new InvalidExpressionException(input);
        }

        return Integer.parseInt(expressionTracker.pop());
    }

    private String getValue(String variable) throws InvalidExpressionException {
        String value = assignments.get(variable);

        if (value == null) {
            value = variable;
        }

        if (isPlainVariable(value)) {
            String msg = "Cannot assign variable " + value + " in the expression";
            LOGGER.error(msg);
            throw new InvalidExpressionException(msg);
        }
        return value;
    }

    private void evaluatePossibleExpressions() throws InvalidExpressionException {
        boolean canEvaluateFurther = true;
        while (canEvaluateFurther && expressionTracker.size() > 2) {
            String currentValue = expressionTracker.pop();
            LOGGER.debug("Popped value " + currentValue + " from stack");
            if (isNumericaValue(currentValue) && expressionTracker.size() > 1) {
                String previousValue = expressionTracker.pop();
                LOGGER.debug("Popped value " + previousValue + " from stack");
                if (isNumericaValue(previousValue) && isOperator(expressionTracker.peek())) {
                    String operator = expressionTracker.pop();
                    currentValue = performOperation(operator, currentValue, previousValue) + "";
                    expressionTracker.push(currentValue);
                    evaluatePossibleExpressions();

                } else if (isVariable(previousValue) && isAssignment(expressionTracker.peek())) {
                    expressionTracker.pop();
                    assignValue(previousValue, currentValue);
                } else {
                    expressionTracker.push(previousValue);
                    expressionTracker.push(currentValue);
                }
            } else {
                expressionTracker.push(currentValue);
                canEvaluateFurther = false;
            }
        }
    }

    private void assignValue(String variable, String value) throws InvalidExpressionException {
        if (isNumericaValue(value)) {
            if (isPlainVariable(value)) {
                value = getValue(value);
            }
            assignments.put(variable, value);

            variablesToFind.remove(variable);
        }
    }

    private static boolean isAssignment(String str) {
        return "=".equals(str);
    }

    private boolean isVariable(String str) {
        return str.matches(VARIABLE_REGEX) && !assignments.containsKey(str);
    }

    private boolean canAssignVariable(String str) {
        return str.matches(VARIABLE_REGEX) && !assignments.containsKey(str);
    }

    private boolean isNumericaValue(String str) {
        str = assignments.get(str) != null ? assignments.get(str) : str;
        return CalculatorUtils.isNumeric(str);
    }

    private int performOperation(String operator, String first, String second) {
        if (isPlainVariable(first)) {
            first = assignments.get(first);
        }
        if (isPlainVariable(second)) {
            second = assignments.get(second);
        }
        LOGGER.debug("Performing operation " + operator + " on values " + first + "," + second);

        int result = 0;
        switch (operator) {
        case Operators.ADD:
            result = Integer.parseInt(first) + Integer.parseInt(second);
            break;
        case Operators.SUB:
            result = Integer.parseInt(first) - Integer.parseInt(second);
            break;
        case Operators.MULT:
            result = Integer.parseInt(first) * Integer.parseInt(second);
            break;
        case Operators.DIV:
            result = Integer.parseInt(first) / Integer.parseInt(second);
        }

        LOGGER.debug("Result of operation " + operator + " on values " + first + "," + second + " is " + result);
        return result;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new Calculator().evaluateExpression("let(let(a,1,2),1,2)"));
        System.out.println(new Calculator().evaluateExpression("add(2,,,3)"));
    }
}
