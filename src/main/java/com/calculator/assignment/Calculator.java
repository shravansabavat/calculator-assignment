package com.calculator.assignment;

import static com.calculator.assignment.utils.ExpressionEvaluationUtils.VARIABLE_REGEX;
import static com.calculator.assignment.utils.ExpressionEvaluationUtils.isOperator;
import static com.calculator.assignment.utils.ExpressionEvaluationUtils.isPlainVariable;
import static com.calculator.assignment.utils.ExpressionEvaluationUtils.validate;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.log4j.Logger;

import com.calculator.assignment.utils.ExpressionEvaluationUtils;

public class Calculator {
    private final static Logger LOGGER = Logger.getLogger(Calculator.class);

    private static Stack<String> expression = new Stack<String>();
    private static Map<String, String> assignments = new HashMap<String, String>();
    private static Map<String, String> backupValues = new HashMap<String, String>();

    public static int evaluateExpression(String input) throws Exception {
        assignments.clear();
        backupValues.clear();
        input = getActualExpression(input);
        validate(input);

        String[] details = input.split(",");
        for (String currentValue : details) {
            if (isOperator(currentValue) || isVariable(currentValue)) {
                LOGGER.debug("Pushing " + currentValue + " into stack");
                expression.push(currentValue);
                continue;
            }

            if (isNumericaValue(currentValue) && expression.size() > 1) {
                String previousValue = expression.pop();
                LOGGER.debug("Popped " + previousValue + " from stack");

                if (isNumericaValue(previousValue) && isOperator(expression.peek())) {
                    String operator = expression.pop();
                    LOGGER.debug("Performing operation " + operator + " on values " + previousValue + "," + currentValue
                            + "from stack");
                    currentValue = performOperation(operator, previousValue, currentValue) + "";
                    LOGGER.debug("Pushing result from operation" + currentValue + " into stack");
                    expression.push(currentValue);
                    evaluatePossibleExpressions();
                } else if (canAssignVariable(previousValue) && isAssignment(expression.peek())) {
                    expression.pop();
                    LOGGER.debug("Assigning variable:" + previousValue + " with value:" + currentValue);
                    assignValue(previousValue, currentValue);
                } else {
                    LOGGER.debug("Pushing " + previousValue + " into stack");
                    expression.push(previousValue);
                    LOGGER.debug("Pushing " + currentValue + " into stack");
                    expression.push(currentValue);
                }
            } else {
                LOGGER.debug("Pushing " + currentValue + " into stack");
                expression.push(currentValue);
            }

        }

        if (!expression.isEmpty() && expression.size() > 2) {
            String first = expression.pop();
            LOGGER.debug("Popped " + first + " from stack");
            String currentValue = getValue(first);
            while (expression.size() > 1) {
                String second = expression.pop();
                LOGGER.debug("Popped " + second + " from stack");
                String previousValue = getValue(second);
                if (isOperator(expression.peek())) {
                    String operator = expression.pop();
                    currentValue = performOperation(operator, currentValue, previousValue) + "";
                    expression.push(currentValue);
                    LOGGER.debug("Pushing result from operation" + currentValue + " into stack");
                }
            }
        }

        return Integer.parseInt(expression.pop());
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
        while (canEvaluateFurther && expression.size() > 2) {
            String currentValue = expression.pop();
            LOGGER.debug("Popped value " + currentValue + " from stack");
            if (isNumericaValue(currentValue) && expression.size() > 1) {
                String previousValue = expression.pop();
                LOGGER.debug("Popped value " + previousValue + " from stack");
                if (isNumericaValue(previousValue) && isOperator(expression.peek())) {
                    String operator = expression.pop();
                    currentValue = performOperation(operator, currentValue, previousValue) + "";
                    expression.push(currentValue);
                    evaluatePossibleExpressions();

                } else if (isVariable(previousValue) && isAssignment(expression.peek())) {
                    expression.pop();
                    assignValue(previousValue, currentValue);
                } else {
                    expression.push(previousValue);
                    expression.push(currentValue);
                }
            } else {
                expression.push(currentValue);
                canEvaluateFurther = false;
            }
        }

        if (expression.isEmpty()) {
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
        return ExpressionEvaluationUtils.isNumeric(str);
    }

    public static String getActualExpression(String input) {
        input = input.trim();
        String expressionWithOperatos = input.replace(" ", "").toLowerCase();
        expressionWithOperatos = expressionWithOperatos.replace("(", ",");
        expressionWithOperatos = expressionWithOperatos.replace(")", "");
        expressionWithOperatos = expressionWithOperatos.replace("(", ",");
        expressionWithOperatos = expressionWithOperatos.replace("let", "=");
        expressionWithOperatos = expressionWithOperatos.replace("add", "+");
        expressionWithOperatos = expressionWithOperatos.replace("sub", "-");
        expressionWithOperatos = expressionWithOperatos.replace("mult", "*");
        expressionWithOperatos = expressionWithOperatos.replace("div", "/");
        return expressionWithOperatos;
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
