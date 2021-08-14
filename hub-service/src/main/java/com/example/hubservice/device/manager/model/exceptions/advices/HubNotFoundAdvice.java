package com.example.hubservice.device.manager.model.exceptions.advices;

import com.example.backend.device.manager.controllers.exceptions.HubNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class HubNotFoundAdvice {
    @ExceptionHandler(HubNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String hubNotFoundHandler (HubNotFoundException e) {
        return e.getMessage();
    }
}
