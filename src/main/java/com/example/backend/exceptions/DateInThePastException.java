package com.example.backend.exceptions;

public class DateInThePastException extends IllegalArgumentException {
    public DateInThePastException(String message) {
        super(message);
    }
}
