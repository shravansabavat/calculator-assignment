package com.calculator.assignment.utils;

public class ExpressionEvaluationUtils {

    public static final String VARIABLE_REGEX = "^[a-zA-Z]*$";

    public static void validate(String input) throws Exception {
        String[] details = input.split(",");
        for (String currentValue : details) {
            if (!isNumeric(currentValue) && !isOperator(currentValue) && !isPlainVariable(currentValue)) {
                throw new Exception("Cannot evaluate, invalid expression");
            }
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
}
