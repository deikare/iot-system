package com.example.hubservice.management.hub.service.configs.device;

import com.example.hubservice.management.hub.exceptions.DeviceNotFoundException;
import com.example.hubservice.management.hub.exceptions.HubNotFoundException;
import com.example.hubservice.management.hub.exceptions.builders.DeviceNotFoundExceptionBuilder;
import com.example.hubservice.management.hub.model.ControlSignal;
import com.example.hubservice.management.hub.model.Device;
import com.example.hubservice.management.hub.model.Hub;
import com.example.hubservice.management.hub.respositories.DeviceRepository;
import com.example.hubservice.management.hub.service.implementation.crud.DependentServiceImplementation;
import com.example.hubservice.management.hub.service.implementation.crud.MasterServiceImplementation;
import com.example.hubservice.utilities.loggers.abstracts.ConfigLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeviceServiceImplementationAuxiliaryConfig {
    private final DeviceRepository deviceRepository;
    private final DeviceNotFoundExceptionBuilder builder;

    // this bean is masterServiceImplementation of higher service implementation - needed to produce current level Dependent service implementation
    private final MasterServiceImplementation<Hub, Device, String, HubNotFoundException> hubServiceImplementation;

    private final Logger logger = LoggerFactory.getLogger(DeviceServiceImplementationAuxiliaryConfig.class);

    public DeviceServiceImplementationAuxiliaryConfig(DeviceRepository deviceRepository, DeviceNotFoundExceptionBuilder builder, MasterServiceImplementation<Hub, Device, String, HubNotFoundException> hubServiceImplementation) {
        this.deviceRepository = deviceRepository;
        this.builder = builder;
        this.hubServiceImplementation = hubServiceImplementation;
    }


    //those beans initialize auxiliary singletons - both for Master and Dependent implementations used in Wrapper implementation
    @Bean
    MasterServiceImplementation<Device, ControlSignal, Long, DeviceNotFoundException> deviceMasterServiceImplementation() {
        MasterServiceImplementation<Device, ControlSignal, Long, DeviceNotFoundException> result = new MasterServiceImplementation<>(deviceRepository, builder);
        ConfigLogger.produceConfigBeanCreationLog(logger, result, "DeviceMasterAuxiliaryServiceImplementation");
        return result;
    }

    @Bean
    DependentServiceImplementation<Device, Hub, Long, String, DeviceNotFoundException, HubNotFoundException> deviceDependentServiceImplementation() {
        DependentServiceImplementation<Device, Hub, Long, String, DeviceNotFoundException, HubNotFoundException> result = new DependentServiceImplementation<>(deviceRepository, builder, hubServiceImplementation);
        ConfigLogger.produceConfigBeanCreationLog(logger, result, "DeviceDependentAuxiliaryServiceImplementation");
        return result;
    }
}
