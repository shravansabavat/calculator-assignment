package com.calculator.assignment.utils;

import java.util.Arrays;
import java.util.Stack;

import org.apache.log4j.Logger;

import com.calculator.assignment.Calculator;
import com.calculator.assignment.exceptions.InvalidExpressionException;
import com.calculator.assignment.exceptions.InvalidOperatorException;

public class CalculatorUtils {
    private final static Logger LOGGER = Logger.getLogger(Calculator.class);
    public static final String VARIABLE_REGEX = "^[a-zA-Z]*$";

    public static void validateExpresion(String input) throws Exception {
        Stack<String> validatorStack = new Stack<String>();
        input = getExpressionToValidate(input);
        String[] details = input.split(",");

        String[] validStrings = Arrays.stream(details).filter(value -> value != null && value.length() > 0)
                .toArray(size -> new String[size]);

        String previous = null;
        for (String currentValue : validStrings) {
            if ("(".equals(currentValue)) {
                if (!isOperator(previous)) {
                    String message = "Invalid operator " + previous + " exception";
                    LOGGER.error(message);
                    throw new InvalidOperatorException(message);
                }
                validatorStack.push(currentValue);
            } else if (")".equals(currentValue)) {
                if (!isPlainVariable(previous) && !isNumeric(previous) && !")".equals(previous)) {
                    String message = ") should be prefixed with a number/variable or another )";
                    LOGGER.error(message);
                    throw new InvalidExpressionException(message);
                }

                if (validatorStack.empty()) {
                    throw new Exception("");
                } else if ("(".equals(validatorStack.peek())) {
                    validatorStack.pop();
                } else {
                    throw new Exception("");
                }
            } else if (isPlainVariable(currentValue) && isNumeric(currentValue)) {
                if (!previous.equals("(")) {
                    String message = "Any number or variable should be prefixed with (";
                    LOGGER.error(message);
                    throw new InvalidExpressionException(message);
                }
            }
            previous = currentValue;
        }
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static boolean isPlainVariable(String str) {
        return str.matches(VARIABLE_REGEX);
    }

    public static boolean isOperator(String str) {
        return "=+-/*".contains(str);
    }

    public static String getExpressionToValidate(String input) {
        input = input.trim();
        String expressionWithOperatos = input.replace(" ", "").toLowerCase();
        expressionWithOperatos = expressionWithOperatos.replace("(", ",(,");
        expressionWithOperatos = expressionWithOperatos.replace(")", ",)");
        expressionWithOperatos = expressionWithOperatos.replace("let", "=,");
        expressionWithOperatos = expressionWithOperatos.replace("add", "+,");
        expressionWithOperatos = expressionWithOperatos.replace("sub", "-,");
        expressionWithOperatos = expressionWithOperatos.replace("multi", "*,");
        expressionWithOperatos = expressionWithOperatos.replace("mult", "*,");
        expressionWithOperatos = expressionWithOperatos.replace("div", "/,");
        return expressionWithOperatos;
    }

    public static String getExpressionWithOperators(String input) {
        input = input.trim();
        String expressionWithOperatos = input.replace(" ", "").toLowerCase();
        expressionWithOperatos = expressionWithOperatos.replace("(", ",");
        expressionWithOperatos = expressionWithOperatos.replace(")", "");
        expressionWithOperatos = expressionWithOperatos.replace("(", ",");
        expressionWithOperatos = expressionWithOperatos.replace("let", "=");
        expressionWithOperatos = expressionWithOperatos.replace("add", "+");
        expressionWithOperatos = expressionWithOperatos.replace("sub", "-");
        expressionWithOperatos = expressionWithOperatos.replace("multi", "*");
        expressionWithOperatos = expressionWithOperatos.replace("mult", "*");
        expressionWithOperatos = expressionWithOperatos.replace("div", "/");
        return expressionWithOperatos;
    }
}
