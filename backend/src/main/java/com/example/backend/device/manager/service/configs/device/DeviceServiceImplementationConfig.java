package com.example.backend.device.manager.service.configs.device;

import com.example.backend.device.manager.controllers.exceptions.ControlSignalNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.ControlSignalResponseNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.builders.DeviceNotFoundExceptionBuilder;
import com.example.backend.device.manager.controllers.exceptions.DeviceNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.HubNotFoundException;
import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.ControlSignalResponse;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.repositories.DeviceRepository;
import com.example.backend.device.manager.service.configs.control.response.ControlSignalResponseServiceImplementationConfig;
import com.example.backend.device.manager.service.implementation.crud.DependentServiceImplementation;
import com.example.backend.device.manager.service.implementation.crud.MasterAndDependentServiceImplementation;
import com.example.backend.device.manager.service.implementation.crud.MasterServiceImplementation;
import com.example.backend.device.manager.service.implementation.filtering.ByMasterAndMessageContentContainingPaginationAndFilteringServiceImplementation;
import com.example.backend.device.manager.service.implementation.filtering.ByMasterPaginationAndFilteringServiceImplementation;
import com.example.backend.loggers.abstracts.ConfigLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeviceServiceImplementationConfig {
    private final DeviceRepository deviceRepository;
    private final DeviceNotFoundExceptionBuilder builder;

    private final DependentServiceImplementation<Device, Hub, DeviceNotFoundException, HubNotFoundException> deviceDependentServiceImplementation;
    private final MasterServiceImplementation<Device, ControlSignal, DeviceNotFoundException> deviceMasterServiceImplementation;

    private final Logger logger = LoggerFactory.getLogger(ControlSignalResponseServiceImplementationConfig.class);

    public DeviceServiceImplementationConfig(DeviceRepository deviceRepository, DeviceNotFoundExceptionBuilder builder, DependentServiceImplementation<Device, Hub, DeviceNotFoundException, HubNotFoundException> deviceDependentServiceImplementation, MasterServiceImplementation<Device, ControlSignal, DeviceNotFoundException> deviceMasterServiceImplementation) {
        this.deviceRepository = deviceRepository;
        this.builder = builder;
        this.deviceDependentServiceImplementation = deviceDependentServiceImplementation;
        this.deviceMasterServiceImplementation = deviceMasterServiceImplementation;
    }

    // and this one produces final singleton - Master and Dependent implementation
    @Bean
    MasterAndDependentServiceImplementation<Device, ControlSignal, Hub, DeviceNotFoundException, HubNotFoundException> deviceMasterAndDependentServiceImplementation() {
        MasterAndDependentServiceImplementation<Device, ControlSignal, Hub, DeviceNotFoundException, HubNotFoundException> result = new MasterAndDependentServiceImplementation<>(deviceRepository, builder, deviceMasterServiceImplementation, deviceDependentServiceImplementation);
        ConfigLogger.configBeanCreationLog(logger, result, "DeviceCrudServiceImplementation");
        return result;
    }

    @Bean
    ByMasterPaginationAndFilteringServiceImplementation<Device> deviceFilteringServiceImplementation() {
        ByMasterPaginationAndFilteringServiceImplementation<Device> result = new ByMasterPaginationAndFilteringServiceImplementation<>(deviceRepository);
        ConfigLogger.configBeanCreationLog(logger, result, "DeviceFilteringServiceImplementation");
        return result;
    }
}
