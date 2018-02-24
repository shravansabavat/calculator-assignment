package com.calculator.assignment.exceptions;

public class InvalidOperatorException extends Exception {

    private static final long serialVersionUID = -6767303015702529822L;

    public InvalidOperatorException() {
        super();
    }

    public InvalidOperatorException(String message) {
        super(message);
    }
}
