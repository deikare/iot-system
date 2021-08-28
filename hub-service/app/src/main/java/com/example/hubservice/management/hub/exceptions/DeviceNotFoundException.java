package com.example.hubservice.management.hub.exceptions;

public class DeviceNotFoundException extends RuntimeException{
    public DeviceNotFoundException(Long deviceId) {
        super("Could not find device " + deviceId);
    }
}
