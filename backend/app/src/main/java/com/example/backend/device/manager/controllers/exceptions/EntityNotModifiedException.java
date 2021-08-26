package com.example.backend.device.manager.controllers.exceptions;

public class EntityNotModifiedException extends RuntimeException {
    public EntityNotModifiedException() {
        super("Entity not modified");
    }
}
