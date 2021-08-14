package com.example.hubservice.device.manager.model.exceptions;

public class ControlSignalNotFoundException extends RuntimeException {
    public ControlSignalNotFoundException(Long id) {
        super("Could not find controlSignal " + id);
    }
}
