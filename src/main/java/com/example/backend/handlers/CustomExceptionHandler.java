package com.example.backend.handlers;

import com.example.backend.exceptions.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public String handleViolationException(ConstraintViolationException e){
        StringBuilder sb = new StringBuilder("Error: ");
        int i = 0;
        for (ConstraintViolation violation: e.getConstraintViolations()){
            sb.append(violation.getMessage());
            i++;
            if(e.getConstraintViolations().size() > 1 && i < e.getConstraintViolations().size()){
                sb.append(", ");
            }
        }
        return sb.toString();
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ObjectNotFoundException.class)
    public String handleObjectNotFoundException(Exception e) {
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ObjectAlreadyExistsException.class)
    public String handleObjectAlreadyExistsException(Exception e) {
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(StartDateAfterEndDateException.class)
    public String handleStartDateAfterEndDateException(Exception e) {
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(StartDateEqualsEndDateException.class)
    public String handleStartDateEqualsEndDateException(Exception e) {
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DateInThePastException.class)
    public String handleDateInThePastException(Exception e) {
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RoomAvailableException.class)
    public String handleRoomAvailableException(Exception e) {
        return e.getMessage();
    }
}
