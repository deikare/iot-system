package com.example.backend.device.manager.controllers.exceptions;

public class ControlSignalResponseNotFoundException extends RuntimeException {
    public ControlSignalResponseNotFoundException(Long id) {
        super("Could not find controlSignalResponse " + id);
    }
}
