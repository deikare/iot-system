package com.example.backend.device.manager.controllers.exceptions.hub;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class HubNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(HubNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String hubNotFoundHandler (HubNotFoundException e) {
        return e.getMessage();
    }
}
