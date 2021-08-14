package com.example.hubservice.device.manager.model.exceptions.builders;

import com.example.backend.device.manager.controllers.exceptions.DeviceNotFoundException;
import com.example.backend.device.manager.service.Builder;
import org.springframework.stereotype.Component;

@Component
public class DeviceNotFoundExceptionBuilder implements Builder <DeviceNotFoundException> {
    @Override
    public DeviceNotFoundException newObject(Long id) {
        return new DeviceNotFoundException(id);
    }
}
