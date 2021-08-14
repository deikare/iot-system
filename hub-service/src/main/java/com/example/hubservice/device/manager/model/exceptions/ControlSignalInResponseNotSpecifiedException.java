package com.example.hubservice.device.manager.model.exceptions;

public class ControlSignalInResponseNotSpecifiedException extends RuntimeException {
    public ControlSignalInResponseNotSpecifiedException(Long controlSignalResponseId) {
        super("ControlSignal in controlSignalResponse " + controlSignalResponseId + " has not been specified");
    }
}
