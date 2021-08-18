package com.example.backend.device.manager.controllers.exceptions;

import org.springframework.stereotype.Component;

import java.util.UUID;

public class HubNotFoundException extends RuntimeException {
    public HubNotFoundException(String id) {
        super("Could not find hub " + id);
    }
}
