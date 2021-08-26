package com.example.backend.device.manager.service.configs.control.response;

import com.example.backend.device.manager.controllers.exceptions.ControlSignalNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.ControlSignalResponseNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.builders.ControlSignalResponseNotFoundExceptionBuilder;
import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.ControlSignalResponse;
import com.example.backend.device.manager.repositories.ControlSignalResponseRepository;
import com.example.backend.device.manager.service.implementation.crud.DependentServiceImplementation;
import com.example.backend.device.manager.service.implementation.crud.MasterServiceImplementation;
import com.example.backend.device.manager.service.implementation.filtering.ByMasterAndMessageContentContainingPaginationAndFilteringServiceImplementation;
import com.example.backend.utilities.loggers.abstracts.ConfigLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControlSignalResponseServiceImplementationConfig {
    private final ControlSignalResponseRepository controlSignalResponseRepository;
    private final ControlSignalResponseNotFoundExceptionBuilder builder;

    private final Logger logger = LoggerFactory.getLogger(ControlSignalResponseServiceImplementationConfig.class);

    private final MasterServiceImplementation<ControlSignal, ControlSignalResponse, Long, ControlSignalNotFoundException> controlSignalServiceImplementation;

    public ControlSignalResponseServiceImplementationConfig(ControlSignalResponseRepository controlSignalResponseRepository, ControlSignalResponseNotFoundExceptionBuilder builder, MasterServiceImplementation<ControlSignal, ControlSignalResponse, Long, ControlSignalNotFoundException> controlSignalServiceImplementation) {
        this.controlSignalResponseRepository = controlSignalResponseRepository;
        this.builder = builder;
        this.controlSignalServiceImplementation = controlSignalServiceImplementation;
    }


    @Bean
    DependentServiceImplementation<ControlSignalResponse, ControlSignal, Long, Long, ControlSignalResponseNotFoundException, ControlSignalNotFoundException> controlSignalResponseServiceImplementation() {
        DependentServiceImplementation<ControlSignalResponse, ControlSignal, Long, Long, ControlSignalResponseNotFoundException, ControlSignalNotFoundException> result = new DependentServiceImplementation<>(controlSignalResponseRepository, builder, controlSignalServiceImplementation);
        ConfigLogger.produceConfigBeanCreationLog(logger, result, "ControlSignalResponseCrudServiceImplementation");
        return result;
    }

    @Bean
    ByMasterAndMessageContentContainingPaginationAndFilteringServiceImplementation<ControlSignalResponse, Long, Long> controlSignalResponseFilteringServiceImplementation() {
        ByMasterAndMessageContentContainingPaginationAndFilteringServiceImplementation<ControlSignalResponse, Long, Long> result = new ByMasterAndMessageContentContainingPaginationAndFilteringServiceImplementation<>(controlSignalResponseRepository);
        ConfigLogger.produceConfigBeanCreationLog(logger, result, "ControlSignalResponseFilteringServiceImplementation");
        return result;
    }
}
