package com.example.hubservice.device.manager.model.exceptions.advices;

import com.example.backend.device.manager.controllers.exceptions.ControlSignalInResponseNotSpecifiedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControlSignalInResponseNotSpecifiedAdvice {
    @ExceptionHandler(ControlSignalInResponseNotSpecifiedException.class)
    @ResponseStatus(HttpStatus.PARTIAL_CONTENT)
    String controlSignalNotSpecifiedInResponseHandler(ControlSignalInResponseNotSpecifiedException e) {
        return e.getMessage();
    }
}
