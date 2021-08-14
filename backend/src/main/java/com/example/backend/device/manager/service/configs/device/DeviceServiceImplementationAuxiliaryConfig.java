package com.example.backend.device.manager.service.configs.device;

import com.example.backend.device.manager.controllers.exceptions.DeviceNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.HubNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.builders.DeviceNotFoundExceptionBuilder;
import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.repositories.DeviceRepository;
import com.example.backend.device.manager.service.implementation.crud.DependentServiceImplementation;
import com.example.backend.device.manager.service.implementation.crud.MasterServiceImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeviceServiceImplementationAuxiliaryConfig {
    private final DeviceRepository deviceRepository;
    private final DeviceNotFoundExceptionBuilder builder;

    // this bean is masterServiceImplementation of higher service implementation - needed to produce current level Dependent service implementation
    private final MasterServiceImplementation<Hub, Device, HubNotFoundException> hubServiceImplementation;

    public DeviceServiceImplementationAuxiliaryConfig(DeviceRepository deviceRepository, DeviceNotFoundExceptionBuilder builder, MasterServiceImplementation<Hub, Device, HubNotFoundException> hubServiceImplementation) {
        this.deviceRepository = deviceRepository;
        this.builder = builder;
        this.hubServiceImplementation = hubServiceImplementation;
    }


    //those beans initialize auxiliary singletons - both for Master and Dependent implementations used in Wrapper implementation
    @Bean
    MasterServiceImplementation<Device, ControlSignal, DeviceNotFoundException> deviceMasterServiceImplementation() {
        return new MasterServiceImplementation<>(deviceRepository, builder);
    }

    @Bean
    DependentServiceImplementation<Device, Hub, DeviceNotFoundException, HubNotFoundException> deviceDependentServiceImplementation() {
        return new DependentServiceImplementation<>(deviceRepository, builder, hubServiceImplementation);
    }
}
