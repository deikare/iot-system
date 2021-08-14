package com.example.hubservice.device.manager.model.exceptions;

public class DeviceInControlSignalNotSpecifiedException extends RuntimeException {
    public DeviceInControlSignalNotSpecifiedException(Long controlSignalId) {
        super("Device in controlSignal " + controlSignalId + " has not been specified");
    }
}
