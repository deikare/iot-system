package com.example.hubservice.device.manager.model.exceptions;

public class DeviceNotFoundException extends RuntimeException{
    public DeviceNotFoundException(Long deviceId) {
        super("Could not find device " + deviceId);
    }
}
