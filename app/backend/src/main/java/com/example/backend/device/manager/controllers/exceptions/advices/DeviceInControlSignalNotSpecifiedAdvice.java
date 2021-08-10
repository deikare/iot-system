package com.example.backend.device.manager.controllers.exceptions.advices;

import com.example.backend.device.manager.controllers.exceptions.DeviceInControlSignalNotSpecifiedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DeviceInControlSignalNotSpecifiedAdvice {
    @ExceptionHandler(DeviceInControlSignalNotSpecifiedException.class)
    @ResponseStatus(HttpStatus.PARTIAL_CONTENT)
    String deviceNotSpecifiedInControlSignalHandler(DeviceInControlSignalNotSpecifiedException e) {
        return e.getMessage();
    }
}
