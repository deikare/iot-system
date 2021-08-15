package com.example.backend.device.manager.service.configs.control.response;

import com.example.backend.data.config.InfluxDBConfig;
import com.example.backend.device.manager.controllers.exceptions.ControlSignalNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.ControlSignalResponseNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.builders.ControlSignalResponseNotFoundExceptionBuilder;
import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.ControlSignalResponse;
import com.example.backend.device.manager.repositories.ControlSignalResponseRepository;
import com.example.backend.device.manager.service.implementation.crud.DependentServiceImplementation;
import com.example.backend.device.manager.service.implementation.crud.MasterServiceImplementation;
import com.example.backend.device.manager.service.implementation.filtering.ByMasterAndMessageContentContainingPaginationAndFilteringServiceImplementation;
import com.example.backend.loggers.abstracts.ConfigLogger;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControlSignalResponseServiceImplementationConfig {
    private final ControlSignalResponseRepository controlSignalResponseRepository;
    private final ControlSignalResponseNotFoundExceptionBuilder builder;

    private final Logger logger = LoggerFactory.getLogger(ControlSignalResponseServiceImplementationConfig.class);

    private final MasterServiceImplementation<ControlSignal, ControlSignalResponse, ControlSignalNotFoundException> controlSignalServiceImplementation;

    public ControlSignalResponseServiceImplementationConfig(ControlSignalResponseRepository controlSignalResponseRepository, ControlSignalResponseNotFoundExceptionBuilder builder, MasterServiceImplementation<ControlSignal, ControlSignalResponse, ControlSignalNotFoundException> controlSignalServiceImplementation) {
        this.controlSignalResponseRepository = controlSignalResponseRepository;
        this.builder = builder;
        this.controlSignalServiceImplementation = controlSignalServiceImplementation;
    }

    @Bean
    DependentServiceImplementation<ControlSignalResponse, ControlSignal, ControlSignalResponseNotFoundException, ControlSignalNotFoundException> controlSignalResponseServiceImplementation() {
        DependentServiceImplementation<ControlSignalResponse, ControlSignal, ControlSignalResponseNotFoundException, ControlSignalNotFoundException> result = new DependentServiceImplementation<>(controlSignalResponseRepository, builder, controlSignalServiceImplementation);
        ConfigLogger.configBeanCreationLog(logger, result, "ControlSignalResponseCrudServiceImplementation");
        return result;
    }

    @Bean
    ByMasterAndMessageContentContainingPaginationAndFilteringServiceImplementation<ControlSignalResponse> controlSignalResponseFilteringServiceImplementation() {
        ByMasterAndMessageContentContainingPaginationAndFilteringServiceImplementation<ControlSignalResponse> result = new ByMasterAndMessageContentContainingPaginationAndFilteringServiceImplementation<>(controlSignalResponseRepository);
        ConfigLogger.configBeanCreationLog(logger, result, "ControlSignalResponseFilteringServiceImplementation");
        return result;
    }
}
