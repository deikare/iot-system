package com.example.backend.device.manager.controllers.exceptions.builders;

import com.example.backend.device.manager.controllers.exceptions.HubNotFoundException;
import com.example.backend.device.manager.service.Builder;
import org.springframework.stereotype.Component;

@Component
public class HubNotFoundExceptionBuilder implements Builder<HubNotFoundException> {
    @Override
    public HubNotFoundException newObject(Long id) {
        return new HubNotFoundException(id);
    }
}
