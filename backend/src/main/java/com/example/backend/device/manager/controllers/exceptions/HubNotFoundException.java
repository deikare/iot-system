package com.example.backend.device.manager.controllers.exceptions;

public class HubNotFoundException extends RuntimeException {
    public HubNotFoundException(String id) {
        super("Could not find hub " + id);
    }
}
