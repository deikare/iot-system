package com.example.hubservice.management.hub.exceptions;

public class NoHubsFoundException extends RuntimeException {
    public NoHubsFoundException() {
        super("No hubs found in database");
    }
}
