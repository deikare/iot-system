package com.example.backend.device.manager.controllers.exceptions.device;

public class DeviceNotFoundException extends RuntimeException{
    public DeviceNotFoundException(Long deviceId) {
        super("Could not find device " + deviceId);
    }
}
