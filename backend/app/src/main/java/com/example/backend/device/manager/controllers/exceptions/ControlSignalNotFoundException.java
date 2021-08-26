package com.example.backend.device.manager.controllers.exceptions;

public class ControlSignalNotFoundException extends RuntimeException {
    public ControlSignalNotFoundException(Long id) {
        super("Could not find controlSignal " + id);
    }
}
