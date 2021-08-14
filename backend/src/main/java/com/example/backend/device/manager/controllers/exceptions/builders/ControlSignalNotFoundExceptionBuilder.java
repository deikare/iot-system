package com.example.backend.device.manager.controllers.exceptions.builders;

import com.example.backend.device.manager.controllers.exceptions.ControlSignalNotFoundException;
import com.example.backend.device.manager.service.Builder;
import org.springframework.stereotype.Component;

@Component
public class ControlSignalNotFoundExceptionBuilder implements Builder<ControlSignalNotFoundException> {
    @Override
    public ControlSignalNotFoundException newObject(Long id) {
        return new ControlSignalNotFoundException(id);
    }
}
