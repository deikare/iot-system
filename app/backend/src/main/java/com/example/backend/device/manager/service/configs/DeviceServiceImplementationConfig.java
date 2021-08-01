package com.example.backend.device.manager.service.configs;

import com.example.backend.device.manager.controllers.exceptions.builders.DeviceNotFoundExceptionBuilder;
import com.example.backend.device.manager.controllers.exceptions.device.DeviceNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.hub.HubNotFoundException;
import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.repositories.DeviceRepository;
import com.example.backend.device.manager.repositories.HubRepository;
import com.example.backend.device.manager.service.implementation.DependentServiceImplementation;
import com.example.backend.device.manager.service.implementation.MasterServiceImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeviceServiceImplementationConfig {
    private final DeviceRepository deviceRepository;
    private final DeviceNotFoundExceptionBuilder builder;
    private final MasterServiceImplementation<Hub, Device, HubRepository, HubNotFoundException> hubServiceImplementation;

    public DeviceServiceImplementationConfig(DeviceRepository deviceRepository, DeviceNotFoundExceptionBuilder builder, MasterServiceImplementation<Hub, Device, HubRepository, HubNotFoundException> hubServiceImplementation) {
        this.deviceRepository = deviceRepository;
        this.builder = builder;
        this.hubServiceImplementation = hubServiceImplementation;
    }

    @Bean
    MasterServiceImplementation<Device, ControlSignal, DeviceRepository, DeviceNotFoundException> masterDeviceServiceImplementation() {
        return new MasterServiceImplementation<Device, ControlSignal, DeviceRepository, DeviceNotFoundException>(deviceRepository, builder);
    }

    @Bean
    DependentServiceImplementation<Device, Hub, DeviceRepository, HubRepository, DeviceNotFoundException, HubNotFoundException> dependentDeviceServiceImplementation() {
        return new DependentServiceImplementation<Device, Hub, DeviceRepository, HubRepository, DeviceNotFoundException, HubNotFoundException>(deviceRepository, builder, hubServiceImplementation);
    }
}
