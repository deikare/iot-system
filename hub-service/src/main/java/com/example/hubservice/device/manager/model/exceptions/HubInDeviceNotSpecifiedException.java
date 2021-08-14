package com.example.hubservice.device.manager.model.exceptions;

public class HubInDeviceNotSpecifiedException extends RuntimeException {
    public HubInDeviceNotSpecifiedException(Long deviceId) {
        super("Hub in device " + deviceId + " has not been specified");
    }
}
