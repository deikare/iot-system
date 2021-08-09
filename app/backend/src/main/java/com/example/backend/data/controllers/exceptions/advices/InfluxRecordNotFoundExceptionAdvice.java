package com.example.backend.data.controllers.exceptions.advices;

import com.example.backend.data.controllers.exceptions.InfluxRecordNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InfluxRecordNotFoundExceptionAdvice {
    @ExceptionHandler(InfluxRecordNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String recordNotFoundHandler(InfluxRecordNotFoundException e) {
        return e.getMessage();
    }
}
