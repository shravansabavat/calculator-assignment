package com.calculator.assignment.utils;

import org.junit.Test;

public class CalculatorUtilsTest {

    @Test(expected = Exception.class)
    public void validateExpresionShouldThrowExceptionForInvalidExpression1() throws Exception {
        CalculatorUtils.validateExpresion("add(mult(1,2)))");
    }

    @Test(expected = Exception.class)
    public void validateExpresionShouldThrowExceptionForInvalidExpression3() throws Exception {
        CalculatorUtils.validateExpresion("add(1,2))");
    }

    @Test(expected = Exception.class)
    public void validateExpresionShouldThrowExceptionForInvalidExpression4() throws Exception {
        CalculatorUtils.validateExpresion("add((1,2)");
    }

    @Test(expected = Exception.class)
    public void validateExpresionShouldThrowExceptionForUnknownOperator() throws Exception {
        CalculatorUtils.validateExpresion("add(mult(2, 2), blah(9, 3))");
    }

    @Test
    public void validateExpresionShouldAllowbothMultAndMultiForMultiplication() throws Exception {
        CalculatorUtils.validateExpresion("add(mult(2, 2), multi(9, 3))");
    }

    @Test
    public void validateExpresionShouldNotThrowExceptionForValidExpression() throws Exception {
        CalculatorUtils.validateExpresion("add(1, mult(1,2))");
        CalculatorUtils.validateExpresion("add(mult(2, 2), div(9, 3))");
        CalculatorUtils.validateExpresion("mult(add(2, 2), div(9, 3))");
        CalculatorUtils.validateExpresion("add(10, mult(2, 3))");
        CalculatorUtils.validateExpresion("add(1, mult(12, 3))");
    }
}
