package com.example.backend.device.manager.service.configs.control.response;

import com.example.backend.device.manager.controllers.exceptions.ControlSignalNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.ControlSignalResponseNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.builders.ControlSignalResponseNotFoundExceptionBuilder;
import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.ControlSignalResponse;
import com.example.backend.device.manager.repositories.ControlSignalResponseRepository;
import com.example.backend.device.manager.service.implementation.crud.DependentServiceImplementation;
import com.example.backend.device.manager.service.implementation.crud.MasterServiceImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControlSignalResponseServiceImplementationConfig {
    private final ControlSignalResponseRepository controlSignalResponseRepository;
    private final ControlSignalResponseNotFoundExceptionBuilder builder;

    private final MasterServiceImplementation<ControlSignal, ControlSignalResponse, ControlSignalNotFoundException> controlSignalServiceImplementation;

    public ControlSignalResponseServiceImplementationConfig(ControlSignalResponseRepository controlSignalResponseRepository, ControlSignalResponseNotFoundExceptionBuilder builder, MasterServiceImplementation<ControlSignal, ControlSignalResponse, ControlSignalNotFoundException> controlSignalServiceImplementation) {
        this.controlSignalResponseRepository = controlSignalResponseRepository;
        this.builder = builder;
        this.controlSignalServiceImplementation = controlSignalServiceImplementation;
    }


    @Bean
    DependentServiceImplementation<ControlSignalResponse, ControlSignal, ControlSignalResponseNotFoundException, ControlSignalNotFoundException> controlSignalResponseServiceImplementation() {
        return new DependentServiceImplementation<>(controlSignalResponseRepository, builder, controlSignalServiceImplementation);
    }
}
