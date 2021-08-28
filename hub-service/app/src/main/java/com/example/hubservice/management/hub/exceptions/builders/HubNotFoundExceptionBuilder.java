package com.example.hubservice.management.hub.exceptions.builders;

import com.example.hubservice.management.hub.exceptions.HubNotFoundException;
import com.example.hubservice.management.hub.service.Builder;
import org.springframework.stereotype.Component;

@Component
public class HubNotFoundExceptionBuilder implements Builder<HubNotFoundException, String> {
    @Override
    public HubNotFoundException newObject(String id) {
        return new HubNotFoundException(id);
    }
}
