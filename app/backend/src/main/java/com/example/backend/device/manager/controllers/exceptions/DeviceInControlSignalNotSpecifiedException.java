package com.example.backend.device.manager.controllers.exceptions;

public class DeviceInControlSignalNotSpecifiedException extends RuntimeException {
    public DeviceInControlSignalNotSpecifiedException(Long controlSignalId) {
        super("Device in controlSignal " + controlSignalId + " has not been specified");
    }
}
