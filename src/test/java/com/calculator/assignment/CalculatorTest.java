package com.calculator.assignment;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CalculatorTest {
    @Test
    public void evaluateExpressionShouldEvaluateSimpleExpression() {
        assertEquals(4, Calculator.evaluateExpression("add(1,3)"));
        assertEquals(5, Calculator.evaluateExpression("add(1,4)"));
        assertEquals(19, Calculator.evaluateExpression("add(16,3)"));
    }

    @Test
    public void evaluateExpressionShouldEvaluateTwoExpressions() {
        assertEquals(7, Calculator.evaluateExpression("add(1, mult(2, 3))"));
        assertEquals(16, Calculator.evaluateExpression("add(10, mult(2, 3))"));
        assertEquals(37, Calculator.evaluateExpression("add(1, mult(12, 3))"));
    }

    @Test
    public void evaluateExpressionShouldEvaluateThreeExpressions() {
        assertEquals(7, Calculator.evaluateExpression("add(mult(2, 2), div(9, 3))"));
        assertEquals(12, Calculator.evaluateExpression("mult(add(2, 2), div(9, 3))"));
        assertEquals(31, Calculator.evaluateExpression("add(add(2, 2), mult(9, 3))"));
    }

    @Test
    public void evaluateExpressionShouldEvaluateWithSingleAssignment() {
        assertEquals(10, Calculator.evaluateExpression("let(a, 5, add(a, a))"));
    }

    @Test
    public void evaluateExpressionShouldEvaluateWithTwoAssignments() {
        assertEquals(55, Calculator.evaluateExpression("let(a, 5, let(b, mult(a, 10), add(b, a)))"));
    }

    @Test
    public void evaluateExpressionShouldEvaluateWithTwoAssignmentsWithNegativeValues() {
        assertEquals(45, Calculator.evaluateExpression("let(a, -5, let(b, mult(a, -10), add(b, a)))"));
    }

    @Test
    public void evaluateExpressionShouldEvaluateWithThreeAssignments() {
        assertEquals(40, Calculator.evaluateExpression("let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b))"));
    }

    @Test
    public void evaluateExpressionShouldEvaluateWithThreeAssignmentsAndNegativeValues() {
        assertEquals(0, Calculator.evaluateExpression("let(a, let(b, -10, add(b, b)), let(b, 20, add(a, b))"));
    }
}
