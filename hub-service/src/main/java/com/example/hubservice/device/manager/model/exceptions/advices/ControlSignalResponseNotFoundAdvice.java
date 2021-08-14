package com.example.hubservice.device.manager.model.exceptions.advices;

import com.example.backend.device.manager.controllers.exceptions.ControlSignalResponseNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControlSignalResponseNotFoundAdvice {
    @ExceptionHandler(ControlSignalResponseNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String deviceNotFoundHandler (ControlSignalResponseNotFoundException e) {
        return e.getMessage();
    }
}