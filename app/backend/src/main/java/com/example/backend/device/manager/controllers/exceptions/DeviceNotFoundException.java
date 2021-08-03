package com.example.backend.device.manager.controllers.exceptions;

public class DeviceNotFoundException extends RuntimeException{
    public DeviceNotFoundException(Long deviceId) {
        super("Could not find device " + deviceId);
    }
}
