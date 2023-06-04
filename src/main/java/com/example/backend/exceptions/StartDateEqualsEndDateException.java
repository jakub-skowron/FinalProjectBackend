package com.example.backend.exceptions;

public class StartDateEqualsEndDateException extends IllegalArgumentException{
    public StartDateEqualsEndDateException(String message){
        super(message);
    }
}
