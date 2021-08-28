package com.example.hubservice.management.hub.service.configs.device;

import com.example.hubservice.management.hub.exceptions.DeviceNotFoundException;
import com.example.hubservice.management.hub.exceptions.HubNotFoundException;
import com.example.hubservice.management.hub.exceptions.builders.DeviceNotFoundExceptionBuilder;
import com.example.hubservice.management.hub.model.ControlSignal;
import com.example.hubservice.management.hub.model.Device;
import com.example.hubservice.management.hub.model.Hub;
import com.example.hubservice.management.hub.respositories.DeviceRepository;
import com.example.hubservice.management.hub.service.implementation.crud.DependentServiceImplementation;
import com.example.hubservice.management.hub.service.implementation.crud.MasterAndDependentServiceImplementation;
import com.example.hubservice.management.hub.service.implementation.crud.MasterServiceImplementation;
import com.example.hubservice.utilities.loggers.abstracts.ConfigLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeviceServiceImplementationConfig {
    private final DeviceRepository deviceRepository;
    private final DeviceNotFoundExceptionBuilder builder;

    private final DependentServiceImplementation<Device, Hub, Long, String, DeviceNotFoundException, HubNotFoundException> deviceDependentServiceImplementation;
    private final MasterServiceImplementation<Device, ControlSignal, Long, DeviceNotFoundException> deviceMasterServiceImplementation;

    private final Logger logger = LoggerFactory.getLogger(DeviceServiceImplementationConfig.class);

    public DeviceServiceImplementationConfig(DeviceRepository deviceRepository, DeviceNotFoundExceptionBuilder builder, DependentServiceImplementation<Device, Hub, Long, String, DeviceNotFoundException, HubNotFoundException> deviceDependentServiceImplementation, MasterServiceImplementation<Device, ControlSignal, Long, DeviceNotFoundException> deviceMasterServiceImplementation) {
        this.deviceRepository = deviceRepository;
        this.builder = builder;
        this.deviceDependentServiceImplementation = deviceDependentServiceImplementation;
        this.deviceMasterServiceImplementation = deviceMasterServiceImplementation;
    }


    // and this one produces final singleton - Master and Dependent implementation
    @Bean
    MasterAndDependentServiceImplementation<Device, ControlSignal, Hub, Long, String, DeviceNotFoundException, HubNotFoundException> deviceMasterAndDependentServiceImplementation() {
        MasterAndDependentServiceImplementation<Device, ControlSignal, Hub, Long, String, DeviceNotFoundException, HubNotFoundException> result = new MasterAndDependentServiceImplementation<>(deviceRepository, builder, deviceMasterServiceImplementation, deviceDependentServiceImplementation);
        ConfigLogger.produceConfigBeanCreationLog(logger, result, "DeviceCrudServiceImplementation");
        return result;
    }
}
