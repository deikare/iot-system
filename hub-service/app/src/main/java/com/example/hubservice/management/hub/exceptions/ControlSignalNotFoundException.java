package com.example.hubservice.management.hub.exceptions;

public class ControlSignalNotFoundException extends RuntimeException {
    public ControlSignalNotFoundException(Long id) {
        super("Could not find controlSignal " + id);
    }
}
