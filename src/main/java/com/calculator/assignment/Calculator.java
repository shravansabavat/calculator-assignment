package com.calculator.assignment;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Calculator {
    private static Stack<String> expression = new Stack<String>();
    private static Map<String, String> assignments = new HashMap<String, String>();
    private static Map<String, String> backupValues = new HashMap<String, String>();

    public static int evaluateExpression(String input) {
        assignments.clear();
        backupValues.clear();
        input = getActualExpression(input);

        String[] details = input.split(",");
        for (String currentValue : details) {
            if (isOperator(currentValue) || isVariable(currentValue)) {
                expression.push(currentValue);
                continue;
            }

            if (isNumericaValue(currentValue) && expression.size() > 1) {
                String previousValue = expression.pop();

                if (isNumericaValue(previousValue) && isOperator(expression.peek())) {
                    String operator = expression.pop();
                    currentValue = performOperation(operator, previousValue, currentValue) + "";
                    expression.push(currentValue);
                    evaluatePossibleExpressions();
                } else if (canAssignVariable(previousValue) && isAssignment(expression.peek())) {
                    expression.pop();
                    assignValue(previousValue, currentValue);
                } else {
                    expression.push(previousValue);
                    expression.push(currentValue);
                }
            } else {
                expression.push(currentValue);
            }

        }

        if (!expression.isEmpty() && expression.size() > 2) {
            String currentValue = getValue(expression.pop());
            while (expression.size() > 1) {
                String previousValue = getValue(expression.pop());
                if (isOperator(expression.peek())) {
                    String operator = expression.pop();
                    currentValue = performOperation(operator, currentValue, previousValue) + "";
                    expression.push(currentValue);
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
            if (isNumericaValue(currentValue) && expression.size() > 1) {
                String previousValue = expression.pop();

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
        return str.matches("[a-zA-Z]") && !assignments.containsKey(str) && !backupValues.containsKey(str);
    }

    private static boolean canAssignVariable(String str) {
        return str.matches("[a-zA-Z]") && !assignments.containsKey(str);
    }

    private static boolean isPlainVariable(String str) {
        return str.matches("[a-zA-Z]");
    }

    private static boolean isOperator(String str) {
        return "=+-/*".contains(str);
    }

    private static boolean isNumericaValue(String str) {
        str = assignments.get(str) != null ? assignments.get(str) : str;
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static String getActualExpression(String input) {
        String tmp = input.replace(" ", "").toLowerCase();
        tmp = tmp.replace("(", ",");
        tmp = tmp.replace(")", "");
        tmp = tmp.replace("(", ",");
        tmp = tmp.replace("let", "=");
        tmp = tmp.replace("add", "+");
        tmp = tmp.replace("sub", "-");
        tmp = tmp.replace("mult", "*");
        tmp = tmp.replace("div", "/");
        return tmp;
    }

    private static int performOperation(String operator, String first, String second) {
        if (isPlainVariable(first)) {
            first = assignments.get(first);
        }
        if (isPlainVariable(second)) {
            second = assignments.get(second);
        }
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
        return result;
    }
}
