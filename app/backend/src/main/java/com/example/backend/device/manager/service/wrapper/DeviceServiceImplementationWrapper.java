package com.example.backend.device.manager.service.wrapper;

import com.example.backend.device.manager.controllers.exceptions.DeviceNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.HubNotFoundException;
import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.repositories.DeviceRepository;
import com.example.backend.device.manager.repositories.HubRepository;
import com.example.backend.device.manager.service.implementation.DependentServiceImplementation;
import com.example.backend.device.manager.service.implementation.MasterServiceImplementation;
import org.springframework.stereotype.Service;

@Service
public class DeviceServiceImplementationWrapper {
    private final MasterServiceImplementation<Device, ControlSignal, DeviceRepository, DeviceNotFoundException> masterDeviceServiceImplementation;
    private final DependentServiceImplementation<Device, Hub, DeviceRepository, HubRepository, DeviceNotFoundException, HubNotFoundException> dependentDeviceServiceImplementation;

    public DeviceServiceImplementationWrapper(MasterServiceImplementation<Device, ControlSignal, DeviceRepository, DeviceNotFoundException> masterDeviceServiceImplementation,
                                              DependentServiceImplementation<Device, Hub, DeviceRepository, HubRepository, DeviceNotFoundException, HubNotFoundException> dependentDeviceServiceImplementation) {
        this.masterDeviceServiceImplementation = masterDeviceServiceImplementation;
        this.dependentDeviceServiceImplementation = dependentDeviceServiceImplementation;
    }
}
