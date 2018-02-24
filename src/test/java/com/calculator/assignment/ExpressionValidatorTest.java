package com.calculator.assignment;

import org.junit.Assert;
import org.junit.Test;

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
