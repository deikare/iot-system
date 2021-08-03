package com.example.backend.device.manager.controllers.exceptions.advices;

import com.example.backend.device.manager.controllers.exceptions.ControlSignalNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControlSignalNotFoundAdvice {
    @ExceptionHandler(ControlSignalNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String deviceNotFoundHandler (ControlSignalNotFoundException e) {
        return e.getMessage();
    }
}
