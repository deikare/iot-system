package com.example.hubservice.management.hub.service.configs.control.signal;

import com.example.hubservice.management.hub.exceptions.ControlSignalNotFoundException;
import com.example.hubservice.management.hub.exceptions.DeviceNotFoundException;
import com.example.hubservice.management.hub.exceptions.builders.ControlSignalNotFoundExceptionBuilder;
import com.example.hubservice.management.hub.model.ControlSignal;
import com.example.hubservice.management.hub.model.Device;
import com.example.hubservice.management.hub.respositories.ControlSignalRepository;
import com.example.hubservice.management.hub.service.implementation.crud.DependentServiceImplementation;
import com.example.hubservice.management.hub.service.implementation.crud.MasterServiceImplementation;
import com.example.hubservice.utilities.loggers.abstracts.ConfigLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControlSignalServiceImplementationConfig {
    private final ControlSignalRepository controlSignalRepository;
    private final ControlSignalNotFoundExceptionBuilder builder;

    private final MasterServiceImplementation<Device, ControlSignal, Long, DeviceNotFoundException> deviceMasterServiceImplementation;

    private final Logger logger = LoggerFactory.getLogger(ControlSignalServiceImplementationConfig.class);

    public ControlSignalServiceImplementationConfig(ControlSignalRepository controlSignalRepository, ControlSignalNotFoundExceptionBuilder builder, MasterServiceImplementation<Device, ControlSignal, Long, DeviceNotFoundException> deviceMasterServiceImplementation) {
        this.controlSignalRepository = controlSignalRepository;
        this.builder = builder;
        this.deviceMasterServiceImplementation = deviceMasterServiceImplementation;
    }


    @Bean
    DependentServiceImplementation<ControlSignal, Device, Long, Long, ControlSignalNotFoundException, DeviceNotFoundException> controlSignalDependentServiceImplementation() {
        DependentServiceImplementation<ControlSignal, Device, Long, Long, ControlSignalNotFoundException, DeviceNotFoundException> result = new DependentServiceImplementation<>(controlSignalRepository, builder, deviceMasterServiceImplementation);
        ConfigLogger.produceConfigBeanCreationLog(logger, result, "ControlSignalDependentAuxiliaryServiceImplementation");
        return result;
    }
}
