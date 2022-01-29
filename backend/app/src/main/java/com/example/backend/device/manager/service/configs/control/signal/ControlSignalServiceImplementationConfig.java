package com.example.backend.device.manager.service.configs.control.signal;

import com.example.backend.device.manager.controllers.exceptions.ControlSignalNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.DeviceNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.builders.ControlSignalNotFoundExceptionBuilder;
import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.repositories.ControlSignalRepository;
import com.example.backend.device.manager.service.implementation.crud.DependentServiceImplementation;
import com.example.backend.device.manager.service.implementation.crud.MasterServiceImplementation;
import com.example.backend.utilities.loggers.abstracts.ConfigLogger;
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
