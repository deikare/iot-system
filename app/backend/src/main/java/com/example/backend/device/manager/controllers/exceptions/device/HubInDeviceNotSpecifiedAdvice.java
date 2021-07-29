package com.example.backend.device.manager.controllers.exceptions.device;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class HubInDeviceNotSpecifiedAdvice {
    @ResponseBody
    @ExceptionHandler(HubInDeviceNotSpecifiedException.class)
    @ResponseStatus(HttpStatus.PARTIAL_CONTENT)
    String hubNotSpecifiedInDeviceHandler(HubInDeviceNotSpecifiedException e) {
        return e.getMessage();
    }
}
