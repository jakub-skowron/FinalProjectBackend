package com.example.backend.exceptions;

import java.util.NoSuchElementException;

public class RoomAvailableException extends NoSuchElementException {
    public RoomAvailableException(String message){
        super(message);
    }
}
