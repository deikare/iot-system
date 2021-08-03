package com.example.backend.device.manager.controllers.exceptions.builders;

import com.example.backend.device.manager.controllers.exceptions.ControlSignalResponseNotFoundException;
import com.example.backend.device.manager.service.Builder;
import org.springframework.stereotype.Component;

@Component
public class ControlSignalResponseNotFoundExceptionBuilder implements Builder<ControlSignalResponseNotFoundException> {
    @Override
    public ControlSignalResponseNotFoundException newObject(Long id) {
        return new ControlSignalResponseNotFoundException(id);
    }
}
