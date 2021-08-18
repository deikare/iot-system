package com.example.backend.device.manager.service.configs.control.signal;

import com.example.backend.device.manager.controllers.exceptions.ControlSignalNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.DeviceNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.builders.ControlSignalNotFoundExceptionBuilder;
import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.ControlSignalResponse;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.repositories.ControlSignalRepository;
import com.example.backend.device.manager.service.configs.control.response.ControlSignalResponseServiceImplementationConfig;
import com.example.backend.device.manager.service.implementation.crud.DependentServiceImplementation;
import com.example.backend.device.manager.service.implementation.crud.MasterAndDependentServiceImplementation;
import com.example.backend.device.manager.service.implementation.crud.MasterServiceImplementation;
import com.example.backend.device.manager.service.implementation.filtering.ByMasterAndMessageContentContainingPaginationAndFilteringServiceImplementation;
import com.example.backend.utilities.loggers.abstracts.ConfigLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControlSignalServiceImplementationConfig {
    private final ControlSignalRepository controlSignalRepository;
    private final ControlSignalNotFoundExceptionBuilder builder;

    private final MasterServiceImplementation<ControlSignal, ControlSignalResponse, Long, ControlSignalNotFoundException> controlSignalMasterServiceImplementation;
    private final DependentServiceImplementation<ControlSignal, Device, Long, Long, ControlSignalNotFoundException, DeviceNotFoundException> controlSignalDependentServiceImplementation;

    private final Logger logger = LoggerFactory.getLogger(ControlSignalResponseServiceImplementationConfig.class);

    public ControlSignalServiceImplementationConfig(ControlSignalRepository controlSignalRepository, ControlSignalNotFoundExceptionBuilder builder, MasterServiceImplementation<ControlSignal, ControlSignalResponse, Long, ControlSignalNotFoundException> controlSignalMasterServiceImplementation, DependentServiceImplementation<ControlSignal, Device, Long, Long, ControlSignalNotFoundException, DeviceNotFoundException> controlSignalDependentServiceImplementation) {
        this.controlSignalRepository = controlSignalRepository;
        this.builder = builder;
        this.controlSignalMasterServiceImplementation = controlSignalMasterServiceImplementation;
        this.controlSignalDependentServiceImplementation = controlSignalDependentServiceImplementation;
    }


    @Bean
    MasterAndDependentServiceImplementation<ControlSignal, ControlSignalResponse, Device, Long, Long, ControlSignalNotFoundException, DeviceNotFoundException> controlSignalMasterAndDependentServiceImplementation() {
        MasterAndDependentServiceImplementation<ControlSignal, ControlSignalResponse, Device, Long, Long,ControlSignalNotFoundException, DeviceNotFoundException> result = new MasterAndDependentServiceImplementation<>(controlSignalRepository, builder, controlSignalMasterServiceImplementation, controlSignalDependentServiceImplementation);
        ConfigLogger.produceConfigBeanCreationLog(logger, result, "ControlSignalCrudServiceImplementation");
        return result;
    }

    @Bean
    ByMasterAndMessageContentContainingPaginationAndFilteringServiceImplementation<ControlSignal, Long, Long> controlSignalFilteringServiceImplementation() {
        ByMasterAndMessageContentContainingPaginationAndFilteringServiceImplementation<ControlSignal, Long, Long> result = new ByMasterAndMessageContentContainingPaginationAndFilteringServiceImplementation<>(controlSignalRepository);
        ConfigLogger.produceConfigBeanCreationLog(logger, result, "ControlSignalFilteringServiceImplementation");
        return result;
    }
}
