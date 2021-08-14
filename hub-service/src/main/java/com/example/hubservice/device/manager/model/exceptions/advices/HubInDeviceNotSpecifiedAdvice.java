package com.example.hubservice.device.manager.model.exceptions.advices;

import com.example.backend.device.manager.controllers.exceptions.HubInDeviceNotSpecifiedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class HubInDeviceNotSpecifiedAdvice {
    @ExceptionHandler(HubInDeviceNotSpecifiedException.class)
    @ResponseStatus(HttpStatus.PARTIAL_CONTENT)
    String hubNotSpecifiedInDeviceHandler(HubInDeviceNotSpecifiedException e) {
        return e.getMessage();
    }
}
