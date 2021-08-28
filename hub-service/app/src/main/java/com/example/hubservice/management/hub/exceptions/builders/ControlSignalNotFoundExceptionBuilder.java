package com.example.hubservice.management.hub.exceptions.builders;

import com.example.hubservice.management.hub.exceptions.ControlSignalNotFoundException;
import com.example.hubservice.management.hub.service.Builder;
import org.springframework.stereotype.Component;

@Component
public class ControlSignalNotFoundExceptionBuilder implements Builder<ControlSignalNotFoundException, Long> {
    @Override
    public ControlSignalNotFoundException newObject(Long id) {
        return new ControlSignalNotFoundException(id);
    }
}
