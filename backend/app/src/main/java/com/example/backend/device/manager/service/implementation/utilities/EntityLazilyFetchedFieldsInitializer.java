package com.example.backend.device.manager.service.implementation.utilities;

import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.Hub;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class EntityLazilyFetchedFieldsInitializer {
    public List<Hub> generateFetchedHubsBasedOnControlSignals(List<ControlSignal> controlSignals) {
        List<Device> devices = deviceListWithControlsFromControlSignalListBuilder(controlSignals);
        return generateFetchedHubsBasedOnDevices(devices);
    }

    public List<Hub> generateFetchedHubsBasedOnDevices(List<Device> devices) {
        List<Hub> result = hubListWithDevicesFromDeviceListBuilder(devices);
        initializeHubs(result);
        return result;
    }

    public Hub generateFetchedHubBasedOnControlSignal(ControlSignal controlSignal) {
        return generateFetchedHubBasedOnDevice(controlSignal.getDevice());
    }

    public Hub generateFetchedHubBasedOnDevice(Device device) {
        Hub result = device.getHub();
        initializeHub(result);
        return result;
    }

    public void initializeHubs(List<Hub> hubs) {
        hubs.parallelStream().forEach(this::initializeHub);
    }

    public void initializeHub(Hub hub) {
        Hibernate.initialize(hub.getDevices());
        for (Device device : hub.getDevices())
            Hibernate.initialize(device.getControlSignals());
    }

    private List<Hub> hubListWithDevicesFromDeviceListBuilder(List<Device> devices) {
        return devices.stream()
                .map(Device::getHub)
                .distinct()
                .toList();
    }

    private List<Device> deviceListWithControlsFromControlSignalListBuilder(List<ControlSignal> controlSignals) {
        return controlSignals.stream()
                .map(ControlSignal::getDevice)
                .distinct()
                .toList();
    }
}
