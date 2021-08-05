package com.example.backend.device.manager.service.configs.control.signal;

import com.example.backend.device.manager.controllers.exceptions.ControlSignalNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.DeviceNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.builders.ControlSignalNotFoundExceptionBuilder;
import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.ControlSignalResponse;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.repositories.ControlSignalRepository;
import com.example.backend.device.manager.repositories.DeviceRepository;
import com.example.backend.device.manager.service.implementation.crud.DependentServiceImplementation;
import com.example.backend.device.manager.service.implementation.crud.MasterServiceImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControlSignalServiceImplementationAuxiliaryConfig {
    private final ControlSignalRepository controlSignalRepository;
    private final ControlSignalNotFoundExceptionBuilder builder;

    private final MasterServiceImplementation<Device, ControlSignal, DeviceRepository, DeviceNotFoundException> deviceMasterServiceImplementation;

    public ControlSignalServiceImplementationAuxiliaryConfig(ControlSignalRepository controlSignalRepository, ControlSignalNotFoundExceptionBuilder builder, MasterServiceImplementation<Device, ControlSignal, DeviceRepository, DeviceNotFoundException> deviceMasterServiceImplementation) {
        this.controlSignalRepository = controlSignalRepository;
        this.builder = builder;
        this.deviceMasterServiceImplementation = deviceMasterServiceImplementation;
    }

    @Bean
    MasterServiceImplementation<ControlSignal, ControlSignalResponse, ControlSignalRepository, ControlSignalNotFoundException> controlSignalMasterServiceImplementation() {
        return new MasterServiceImplementation<>(controlSignalRepository, builder);
    }

    @Bean
    DependentServiceImplementation<ControlSignal, Device, ControlSignalRepository, DeviceRepository, ControlSignalNotFoundException, DeviceNotFoundException> controlSignalDependentServiceImplementation() {
        return new DependentServiceImplementation<>(controlSignalRepository, builder, deviceMasterServiceImplementation);
    }
}
