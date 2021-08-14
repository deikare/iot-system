package com.example.hubservice.device.manager.model.exceptions;

public class ControlSignalResponseNotFoundException extends RuntimeException {
    public ControlSignalResponseNotFoundException(Long id) {
        super("Could not find controlSignalResponse " + id);
    }
}
