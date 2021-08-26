package com.example.backend.device.manager.controllers.exceptions.advices;

import com.example.backend.device.manager.controllers.exceptions.EntityNotModifiedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class EntityNotModifiedAdvice {
    @ExceptionHandler(EntityNotModifiedException.class)
    @ResponseStatus(HttpStatus.NOT_MODIFIED)
    String entityNotModifiedHandler (EntityNotModifiedException e) {
        return e.getMessage();
    }
}
