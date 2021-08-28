package com.example.hubservice.management.hub.exceptions;

public class HubNotFoundException extends RuntimeException {
    public HubNotFoundException(String id) {
        super("Could not find hub " + id);
    }
}
