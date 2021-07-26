package com.example.backend.device.manager.controllers.exceptions.hub;

public class HubNotFoundException extends RuntimeException {
    public HubNotFoundException(Long id) {
        super("Could not find hub " + id);
    }
}
