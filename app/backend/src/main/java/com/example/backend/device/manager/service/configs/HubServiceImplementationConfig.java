package com.example.backend.device.manager.service.configs;

import com.example.backend.device.manager.controllers.exceptions.builders.HubNotFoundExceptionBuilder;
import com.example.backend.device.manager.controllers.exceptions.hub.HubNotFoundException;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.repositories.HubRepository;
import com.example.backend.device.manager.service.implementation.MasterServiceImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HubServiceImplementationConfig {
    private final HubRepository repository;
    private final HubNotFoundExceptionBuilder builder;

    public HubServiceImplementationConfig(HubRepository repository, HubNotFoundExceptionBuilder builder) {
        this.repository = repository;
        this.builder = builder;
    }

    @Bean
    MasterServiceImplementation<Hub, Device, HubRepository, HubNotFoundException> masterHubServiceImplementation() {
        return new MasterServiceImplementation<Hub, Device, HubRepository, HubNotFoundException>(repository, builder);
    }
}
