package com.example.backend.device.manager.service.configs.control.signal;

import com.example.backend.device.manager.controllers.exceptions.ControlSignalNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.ControlSignalResponseNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.DeviceNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.builders.ControlSignalNotFoundExceptionBuilder;
import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.ControlSignalResponse;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.repositories.ControlSignalRepository;
import com.example.backend.device.manager.service.configs.control.response.ControlSignalResponseServiceImplementationConfig;
import com.example.backend.device.manager.service.implementation.crud.DependentServiceImplementation;
import com.example.backend.device.manager.service.implementation.crud.MasterServiceImplementation;
import com.example.backend.loggers.abstracts.ConfigLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControlSignalServiceImplementationAuxiliaryConfig {
    private final ControlSignalRepository controlSignalRepository;
    private final ControlSignalNotFoundExceptionBuilder builder;

    private final MasterServiceImplementation<Device, ControlSignal, DeviceNotFoundException> deviceMasterServiceImplementation;

    private final Logger logger = LoggerFactory.getLogger(ControlSignalServiceImplementationAuxiliaryConfig.class);


    public ControlSignalServiceImplementationAuxiliaryConfig(ControlSignalRepository controlSignalRepository, ControlSignalNotFoundExceptionBuilder builder, MasterServiceImplementation<Device, ControlSignal, DeviceNotFoundException> deviceMasterServiceImplementation) {
        this.controlSignalRepository = controlSignalRepository;
        this.builder = builder;
        this.deviceMasterServiceImplementation = deviceMasterServiceImplementation;
    }

    @Bean
    MasterServiceImplementation<ControlSignal, ControlSignalResponse, ControlSignalNotFoundException> controlSignalMasterServiceImplementation() {
        MasterServiceImplementation<ControlSignal, ControlSignalResponse, ControlSignalNotFoundException> result = new MasterServiceImplementation<>(controlSignalRepository, builder);
        ConfigLogger.configBeanCreationLog(logger, result, "ControlSignalMasterAuxiliaryServiceImplementation");
        return result;
    }

    @Bean
    DependentServiceImplementation<ControlSignal, Device, ControlSignalNotFoundException, DeviceNotFoundException> controlSignalDependentServiceImplementation() {
        DependentServiceImplementation<ControlSignal, Device, ControlSignalNotFoundException, DeviceNotFoundException> result = new DependentServiceImplementation<>(controlSignalRepository, builder, deviceMasterServiceImplementation);
        ConfigLogger.configBeanCreationLog(logger, result, "ControlSignalDependentAuxiliaryServiceImplementation");
        return result;
    }
}
