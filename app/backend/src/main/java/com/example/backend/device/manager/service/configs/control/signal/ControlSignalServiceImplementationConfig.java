package com.example.backend.device.manager.service.configs.control.signal;

import com.example.backend.device.manager.controllers.exceptions.ControlSignalNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.DeviceNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.HubNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.builders.ControlSignalNotFoundExceptionBuilder;
import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.ControlSignalResponse;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.repositories.ControlSignalRepository;
import com.example.backend.device.manager.repositories.DeviceRepository;
import com.example.backend.device.manager.repositories.HubRepository;
import com.example.backend.device.manager.service.implementation.DependentServiceImplementation;
import com.example.backend.device.manager.service.implementation.MasterAndDependentServiceImplementation;
import com.example.backend.device.manager.service.implementation.MasterServiceImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControlSignalServiceImplementationConfig {
    private final ControlSignalRepository controlSignalRepository;
    private final ControlSignalNotFoundExceptionBuilder builder;

    private final MasterServiceImplementation<ControlSignal, ControlSignalResponse, ControlSignalRepository, ControlSignalNotFoundException> controlSignalMasterServiceImplementation;
    private final DependentServiceImplementation<ControlSignal, Device, ControlSignalRepository, DeviceRepository, ControlSignalNotFoundException, DeviceNotFoundException> controlSignalDependentServiceImplementation;

    public ControlSignalServiceImplementationConfig(ControlSignalRepository controlSignalRepository, ControlSignalNotFoundExceptionBuilder builder, DependentServiceImplementation<ControlSignal, Device, ControlSignalRepository, DeviceRepository, ControlSignalNotFoundException, DeviceNotFoundException> controlSignalDependentServiceImplementation, MasterServiceImplementation<ControlSignal, ControlSignalResponse, ControlSignalRepository, ControlSignalNotFoundException> controlSignalMasterServiceImplementation) {
        this.controlSignalRepository = controlSignalRepository;
        this.builder = builder;
        this.controlSignalDependentServiceImplementation = controlSignalDependentServiceImplementation;
        this.controlSignalMasterServiceImplementation = controlSignalMasterServiceImplementation;
    }

    @Bean
    MasterAndDependentServiceImplementation<ControlSignal, ControlSignalResponse, Device, ControlSignalRepository, DeviceRepository, ControlSignalNotFoundException, DeviceNotFoundException> controlSignalMasterAndDependentServiceImplementation() {
        return new MasterAndDependentServiceImplementation<>(controlSignalRepository, builder, controlSignalMasterServiceImplementation, controlSignalDependentServiceImplementation);
    }
}
