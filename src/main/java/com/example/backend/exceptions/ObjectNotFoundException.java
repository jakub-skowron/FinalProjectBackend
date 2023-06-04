package com.example.backend.exceptions;

import java.util.NoSuchElementException;

public class ObjectNotFoundException extends NoSuchElementException {
    public ObjectNotFoundException(String message){
        super(message);
    }
}
