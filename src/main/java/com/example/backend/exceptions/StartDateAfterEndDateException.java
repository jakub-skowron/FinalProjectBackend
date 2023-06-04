package com.example.backend.exceptions;

public class StartDateAfterEndDateException extends IllegalArgumentException{
    public StartDateAfterEndDateException(String message){
        super(message);
    }
}
