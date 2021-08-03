package com.example.backend.device.manager.service.configs.device;

import com.example.backend.device.manager.controllers.exceptions.builders.DeviceNotFoundExceptionBuilder;
import com.example.backend.device.manager.controllers.exceptions.DeviceNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.HubNotFoundException;
import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.repositories.DeviceRepository;
import com.example.backend.device.manager.repositories.HubRepository;
import com.example.backend.device.manager.service.implementation.DependentServiceImplementation;
import com.example.backend.device.manager.service.implementation.MasterAndDependentServiceImplementation;
import com.example.backend.device.manager.service.implementation.MasterServiceImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeviceServiceImplementationConfig {
    private final DeviceRepository deviceRepository;
    private final DeviceNotFoundExceptionBuilder builder;

    private final DependentServiceImplementation<Device, Hub, DeviceRepository, HubRepository, DeviceNotFoundException, HubNotFoundException> deviceDependentServiceImplementation;
    private final MasterServiceImplementation<Device, ControlSignal, DeviceRepository, DeviceNotFoundException> deviceMasterServiceImplementation;

    public DeviceServiceImplementationConfig(DeviceRepository deviceRepository, DeviceNotFoundExceptionBuilder builder, DependentServiceImplementation<Device, Hub, DeviceRepository, HubRepository, DeviceNotFoundException, HubNotFoundException> deviceDependentServiceImplementation, MasterServiceImplementation<Device, ControlSignal, DeviceRepository, DeviceNotFoundException> deviceMasterServiceImplementation) {
        this.deviceRepository = deviceRepository;
        this.builder = builder;

        this.deviceDependentServiceImplementation = deviceDependentServiceImplementation;
        this.deviceMasterServiceImplementation = deviceMasterServiceImplementation;
    }

    // and this one produces final singleton - Master and Dependent implementation
    @Bean
    MasterAndDependentServiceImplementation<Device, ControlSignal, Hub, DeviceRepository, HubRepository, DeviceNotFoundException, HubNotFoundException> deviceMasterAndDependentServiceImplementation() {
        return new MasterAndDependentServiceImplementation<>(deviceRepository, builder, deviceMasterServiceImplementation, deviceDependentServiceImplementation);
    }
}
