package com.example.hubservice.management.hub.exceptions.builders;

import com.example.hubservice.management.hub.exceptions.DeviceNotFoundException;
import com.example.hubservice.management.hub.service.Builder;
import org.springframework.stereotype.Component;

@Component
public class DeviceNotFoundExceptionBuilder implements Builder<DeviceNotFoundException, Long> {
    @Override
    public DeviceNotFoundException newObject(Long id) {
        return new DeviceNotFoundException(id);
    }
}
