package com.calculator.assignment;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.calculator.assignment.exceptions.InvalidExpressionException;

public class CalculatorTest {
    private Calculator calculator = null;

    @Before
    public void setup() {
        calculator = new Calculator();
    }

    @Test
    public void evaluateExpressionShouldEvaluateSimpleExpression() throws Exception {
        assertEquals(4, calculator.evaluateExpression("add(1,3)"));
        assertEquals(5, calculator.evaluateExpression("add(1,4)"));
        assertEquals(19, calculator.evaluateExpression("add(16,3)"));
    }

    @Test
    public void evaluateExpressionShouldEvaluateTwoExpressions() throws Exception {
        assertEquals(7, calculator.evaluateExpression("add(1, mult(2, 3))"));
        assertEquals(16, calculator.evaluateExpression("add(10, mult(2, 3))"));
        assertEquals(37, calculator.evaluateExpression("add(1, mult(12, 3))"));
        assertEquals(1, calculator.evaluateExpression("add(1, mult(0, 3))"));
    }

    @Test
    public void evaluateExpressionShouldEvaluateThreeExpressions() throws Exception {
        assertEquals(7, calculator.evaluateExpression("add(mult(2, 2), div(9, 3))"));
        assertEquals(12, calculator.evaluateExpression("mult(add(2, 2), div(9, 3))"));
        assertEquals(31, calculator.evaluateExpression("add(add(2, 2), mult(9, 3))"));
    }

    @Test
    public void evaluateExpressionShouldEvaluateWithSingleAssignment() throws Exception {
        assertEquals(10, calculator.evaluateExpression("let(abc, 5, add(abc, abc))"));
    }

    @Test
    public void evaluateExpressionShouldEvaluateWithTwoAssignments() throws Exception {
        assertEquals(55, calculator.evaluateExpression("let(a, 5, let(b, mult(a, 10), add(b, a)))"));
    }

    @Test
    public void evaluateExpressionShouldEvaluateWithTwoAssignmentsWithNegativeValues() throws Exception {
        assertEquals(45, calculator.evaluateExpression("let(a, -5, let(b, mult(a, -10), add(b, a)))"));
    }

    @Test
    public void evaluateExpressionShouldEvaluateWithThreeAssignments() throws Exception {
        assertEquals(40, calculator.evaluateExpression("let(a, let(b, 10, add(b, b)), let(b,20, add(a, b))"));
    }

    @Test
    public void evaluateExpressionShouldEvaluateWithThreeAssignmentsAndNegativeValues() throws Exception {
        assertEquals(0, calculator.evaluateExpression("let(a, let(b, -10, add(b, b)), let(b, 20, add(a, b))"));
    }

    @Test(expected = InvalidExpressionException.class)
    public void evaluateExpressionShouldThrowExcecptionForInvalidExpression() throws Exception {
        calculator.evaluateExpression("let(let(a,1),1,2)");
    }

    @Test
    public void evaluateExpressionShouldEvaluateExpressionProperly() throws Exception {
        assertEquals(2, calculator.evaluateExpression("let(b, let(a,1), a), add(a, b)"));
    }

    @Test(expected = InvalidExpressionException.class)
    public void evaluateExpressionShouldThrowExcecptionForInvalidExpression2() throws Exception {
        calculator.evaluateExpression("add(2,,,3)");
    }

    @Test(expected = InvalidExpressionException.class)
    public void evaluateExpressionShouldThrowExcecptionForInvalidExpression3() throws Exception {
        calculator.evaluateExpression("add(2,3))");
    }

    @Test(expected = InvalidExpressionException.class)
    public void evaluateExpressionShouldThrowExcecptionForInvalidExpression4() throws Exception {
        calculator.evaluateExpression("add((2,3)");
    }

    @Test(expected = InvalidExpressionException.class)
    public void evaluateExpressionShouldThrowExcecptionForInvalidExpression5() throws Exception {
        calculator.evaluateExpression("add(@,3)");
    }

    @Test(expected = InvalidExpressionException.class)
    public void evaluateExpressionShouldThrowExcecptionForInvalidCharacterInExpression() throws Exception {
        calculator.evaluateExpression("let(b,add(2,3)), let(c, 10), let(d), let(e, add(b,c), add(e,d))");
    }

    @Test
    public void evaluateExpressionShouldThrowExcecptionForInvalidExpression6() throws Exception {
        calculator.evaluateExpression("let(b,add(2,3)), let(c, 10), let(d, 5), let(e, add(b,c)), add(e,d)");
    }
}
