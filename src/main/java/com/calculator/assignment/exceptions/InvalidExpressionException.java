package com.calculator.assignment.exceptions;

public class InvalidExpressionException extends Exception {
    private static final long serialVersionUID = -2371858888134859793L;

    public InvalidExpressionException() {
        super();
    }

    public InvalidExpressionException(String message) {
        super(message);
    }
}
