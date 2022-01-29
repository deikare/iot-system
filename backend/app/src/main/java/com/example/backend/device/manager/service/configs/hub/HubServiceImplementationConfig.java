package com.example.backend.device.manager.service.configs.hub;

import com.example.backend.device.manager.controllers.exceptions.HubNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.builders.HubNotFoundExceptionBuilder;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.repositories.HubRepository;
import com.example.backend.device.manager.service.implementation.crud.MasterServiceImplementation;
import com.example.backend.utilities.loggers.abstracts.ConfigLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HubServiceImplementationConfig {
    private final HubRepository repository;
    private final HubNotFoundExceptionBuilder builder;

    private final Logger logger = LoggerFactory.getLogger(HubServiceImplementationConfig.class);

    public HubServiceImplementationConfig(HubRepository repository, HubNotFoundExceptionBuilder builder) {
        this.repository = repository;
        this.builder = builder;
    }

    @Bean
    public MasterServiceImplementation<Hub, Device, String, HubNotFoundException> hubServiceImplementation() {
        MasterServiceImplementation<Hub, Device, String, HubNotFoundException> result = new MasterServiceImplementation<>(repository, builder);
        ConfigLogger.produceConfigBeanCreationLog(logger, result, "HubCrudServiceImplementation");
        return result;
    }
}
