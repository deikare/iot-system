package com.example.hubservice.device.manager.model.exceptions;

public class HubNotFoundException extends RuntimeException {
    public HubNotFoundException(Long id) {
        super("Could not find hub " + id);
    }
}
