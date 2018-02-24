package com.calculator.assignment;

import static com.calculator.assignment.utils.CalculatorUtils.VARIABLE_REGEX;
import static com.calculator.assignment.utils.CalculatorUtils.getExpressionWithOperators;
import static com.calculator.assignment.utils.CalculatorUtils.isOperator;
import static com.calculator.assignment.utils.CalculatorUtils.isPlainVariable;
import static com.calculator.assignment.utils.CalculatorUtils.validateExpresion;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.log4j.Logger;

import com.calculator.assignment.utils.CalculatorUtils;

public class Calculator {
    private final static Logger LOGGER = Logger.getLogger(Calculator.class);

    private static Stack<String> expressionTracker = new Stack<String>();
    private static Map<String, String> assignments = new HashMap<String, String>();
    private static Map<String, String> backupValues = new HashMap<String, String>();

    public static int evaluateExpression(String input) throws Exception {
        assignments.clear();
        backupValues.clear();
        validateExpresion(input);
        String expression = getExpressionWithOperators(input);

        String[] details = expression.split(",");
        for (String currentValue : details) {
            if (isOperator(currentValue) || isVariable(currentValue)) {
                LOGGER.debug("Pushing " + currentValue + " into stack");
                expressionTracker.push(currentValue);
                continue;
            }

            if (isNumericaValue(currentValue) && expressionTracker.size() > 1) {
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

        return Integer.parseInt(expressionTracker.pop());
    }

    private static String getValue(String variable) {
        String value = assignments.get(variable);
        if (value == null) {
            value = backupValues.get(variable);
        }

        if (value == null) {
            value = variable;
        }

        return value;
    }

    private static void evaluatePossibleExpressions() {
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

        if (expressionTracker.isEmpty()) {
            backupValues.putAll(assignments);
            assignments.clear();
        }
    }

    private static void assignValue(String variable, String value) {
        if (isNumericaValue(value)) {
            assignments.put(variable, value);
            backupValues.remove(variable);
        }
    }

    private static boolean isAssignment(String str) {
        return "=".equals(str);
    }

    private static boolean isVariable(String str) {
        return str.matches(VARIABLE_REGEX) && !assignments.containsKey(str) && !backupValues.containsKey(str);
    }

    private static boolean canAssignVariable(String str) {
        return str.matches(VARIABLE_REGEX) && !assignments.containsKey(str);
    }

    private static boolean isNumericaValue(String str) {
        str = assignments.get(str) != null ? assignments.get(str) : str;
        return CalculatorUtils.isNumeric(str);
    }

    private static int performOperation(String operator, String first, String second) {
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
}
