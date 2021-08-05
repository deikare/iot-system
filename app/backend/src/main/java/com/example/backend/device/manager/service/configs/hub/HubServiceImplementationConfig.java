package com.example.backend.device.manager.service.configs.hub;

import com.example.backend.device.manager.controllers.exceptions.builders.HubNotFoundExceptionBuilder;
import com.example.backend.device.manager.controllers.exceptions.HubNotFoundException;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.repositories.HubRepository;
import com.example.backend.device.manager.service.implementation.crud.MasterServiceImplementation;
import com.example.backend.device.manager.service.implementation.filtering.BasePaginationAndFilteringServiceImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HubServiceImplementationConfig{
    private final HubRepository repository;
    private final HubNotFoundExceptionBuilder builder;

    public HubServiceImplementationConfig(HubRepository repository, HubNotFoundExceptionBuilder builder) {
        this.repository = repository;
        this.builder = builder;
    }

    @Bean
    public MasterServiceImplementation<Hub, Device, HubNotFoundException> hubServiceImplementation() {
        return new MasterServiceImplementation<>(repository, builder);
    }

    @Bean
    public BasePaginationAndFilteringServiceImplementation<Hub> hubFilteringServiceImplementation() {
        return new BasePaginationAndFilteringServiceImplementation<>(repository);
    }
}
