package com.example.hubservice.management.hub.exceptions;

public class DeviceInControlSignalNotSpecifiedException extends RuntimeException {
    public DeviceInControlSignalNotSpecifiedException(Long controlSignalId) {
        super("Device in controlSignal " + controlSignalId + " has not been specified");
    }
}
