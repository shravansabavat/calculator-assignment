package com.calculator.assignment.utils;

import org.junit.Assert;
import org.junit.Test;

import com.calculator.assignment.utils.ExpressionEvaluationUtils;

public class ExpressionValidatorTest {

    @Test
    public void validateShouldNotThrowAnyException() {
        try {
            ExpressionEvaluationUtils.validate("+,1,2");
            ExpressionEvaluationUtils.validate("=,a,2");
        } catch (Exception exp) {
            Assert.fail();
        }
    }
}
