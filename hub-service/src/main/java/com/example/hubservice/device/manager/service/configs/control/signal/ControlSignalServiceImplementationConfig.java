package com.example.hubservice.device.manager.service.configs.control.signal;

import com.example.backend.device.manager.controllers.exceptions.ControlSignalNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.DeviceNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.builders.ControlSignalNotFoundExceptionBuilder;
import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.ControlSignalResponse;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.repositories.ControlSignalRepository;
import com.example.backend.device.manager.service.implementation.crud.DependentServiceImplementation;
import com.example.backend.device.manager.service.implementation.crud.MasterAndDependentServiceImplementation;
import com.example.backend.device.manager.service.implementation.crud.MasterServiceImplementation;
import com.example.backend.device.manager.service.implementation.filtering.ByMasterAndMessageContentContainingPaginationAndFilteringServiceImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControlSignalServiceImplementationConfig {
    private final ControlSignalRepository controlSignalRepository;
    private final ControlSignalNotFoundExceptionBuilder builder;

    private final MasterServiceImplementation<ControlSignal, ControlSignalResponse, ControlSignalNotFoundException> controlSignalMasterServiceImplementation;
    private final DependentServiceImplementation<ControlSignal, Device, ControlSignalNotFoundException, DeviceNotFoundException> controlSignalDependentServiceImplementation;

    public ControlSignalServiceImplementationConfig(ControlSignalRepository controlSignalRepository, ControlSignalNotFoundExceptionBuilder builder, MasterServiceImplementation<ControlSignal, ControlSignalResponse, ControlSignalNotFoundException> controlSignalMasterServiceImplementation, DependentServiceImplementation<ControlSignal, Device, ControlSignalNotFoundException, DeviceNotFoundException> controlSignalDependentServiceImplementation) {
        this.controlSignalRepository = controlSignalRepository;
        this.builder = builder;
        this.controlSignalMasterServiceImplementation = controlSignalMasterServiceImplementation;
        this.controlSignalDependentServiceImplementation = controlSignalDependentServiceImplementation;
    }

    @Bean
    MasterAndDependentServiceImplementation<ControlSignal, ControlSignalResponse, Device, ControlSignalNotFoundException, DeviceNotFoundException> controlSignalMasterAndDependentServiceImplementation() {
        return new MasterAndDependentServiceImplementation<>(controlSignalRepository, builder, controlSignalMasterServiceImplementation, controlSignalDependentServiceImplementation);
    }

    @Bean
    ByMasterAndMessageContentContainingPaginationAndFilteringServiceImplementation<ControlSignal> controlSignalFilteringServiceImplementation() {
        return new ByMasterAndMessageContentContainingPaginationAndFilteringServiceImplementation<>(controlSignalRepository);
    }
}
