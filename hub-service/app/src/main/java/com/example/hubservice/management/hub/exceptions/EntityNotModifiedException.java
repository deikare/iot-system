package com.example.hubservice.management.hub.exceptions;

public class EntityNotModifiedException extends RuntimeException {
    public EntityNotModifiedException() {
        super("Entity not modified");
    }
}
