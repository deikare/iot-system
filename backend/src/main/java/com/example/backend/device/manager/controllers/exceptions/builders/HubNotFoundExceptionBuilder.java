package com.example.backend.device.manager.controllers.exceptions.builders;

import com.example.backend.device.manager.controllers.exceptions.HubNotFoundException;
import com.example.backend.device.manager.service.Builder;
import org.springframework.stereotype.Component;

@Component
public class HubNotFoundExceptionBuilder implements Builder<HubNotFoundException, String> {
    @Override
    public HubNotFoundException newObject(String id) {
        return new HubNotFoundException(id);
    }
}
