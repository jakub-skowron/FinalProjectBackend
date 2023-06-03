package com.example.backend.handlers;

import com.example.backend.exceptions.ObjectAlreadyExistsException;
import com.example.backend.exceptions.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ObjectNotFoundException.class)
    public String handleObjectNotFoundException(Exception e){
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ObjectAlreadyExistsException.class)
    public String handleObjectAlreadyExistsException(Exception e){
        return e.getMessage();
    }
}
