package com.example.backend.exceptions;

public class ObjectAlreadyExistsException extends IllegalArgumentException{
    public ObjectAlreadyExistsException(String message){
        super(message);
    }
}
