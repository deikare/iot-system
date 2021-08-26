package com.example.backend.device.manager.controllers.exceptions.advices;

import com.example.backend.device.manager.controllers.exceptions.DeviceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class DeviceNotFoundAdvice {
    @ExceptionHandler(DeviceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String deviceNotFoundHandler (DeviceNotFoundException e) {
        return e.getMessage();
    }
}
