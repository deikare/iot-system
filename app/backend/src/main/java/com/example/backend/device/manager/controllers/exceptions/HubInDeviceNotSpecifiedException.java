package com.example.backend.device.manager.controllers.exceptions;

public class HubInDeviceNotSpecifiedException extends RuntimeException {
    public HubInDeviceNotSpecifiedException(Long deviceId) {
        super("Hub in device " + deviceId + " has not been specified ");
    }
}
