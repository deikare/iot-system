package com.example.backend.device.manager.service.implementation.old;

import com.example.backend.device.manager.controllers.exceptions.DeviceNotFoundException;
import com.example.backend.device.manager.controllers.exceptions.HubNotFoundException;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.repositories.DeviceRepository;
import com.example.backend.device.manager.service.interfaces.old.DeviceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DeviceServiceImplementation implements DeviceService {
    private final DeviceRepository deviceRepository;
    private final HubServiceImplementation2 hubServiceImplementation;

    public DeviceServiceImplementation(DeviceRepository deviceRepository, HubServiceImplementation2 hubServiceImplementation) {
        this.deviceRepository = deviceRepository;
        this.hubServiceImplementation = hubServiceImplementation;
    }

    @Override
    public Device addDeviceAndBindItToHubById(Device device, Long hubId) throws HubNotFoundException {
        Hub hub = hubServiceImplementation.findHubById(hubId);
        return addDeviceAndBindItToHub(device, hub);
    }
    @Override
    public Device addDeviceAndBindItToHub(Device device, Hub hub) {
        Device addedDevice = addDevice(device);
        //addedDevice.setHub(hub);
        hubServiceImplementation.addDeviceToDeviceListInHub(hub, addedDevice);
        return addedDevice;
    }

    @Override
    public Device addDevice(Device device) {
        return deviceRepository.save(device);
    }

    @Override
    public Page<Device> getAllDevices(Pageable pageable) {
        return deviceRepository.findAll(pageable);
    }

    @Override
    public Page<Device> getAllDevicesByNameContaining(String name, Pageable pageable) {
        return deviceRepository.findAllByNameContaining(name, pageable);
    }

    @Override
    public Page<Device> getAllDevicesByHubId(Long hubId, Pageable pageable) {
        return deviceRepository.findAllByHub_Id(hubId, pageable);
    }

    @Override
    public Page<Device> getAllDevicesByNameContainingAndHubId(String name, Long hubId, Pageable pageable) {
        return deviceRepository.findAllByNameContainingAndHub_Id(name, hubId, pageable);
    }

    @Override
    public void deleteDeviceById(Long deviceId) throws DeviceNotFoundException {
        Device device = findDeviceById(deviceId);
        deleteDevice(device);
    }

    @Override
    public boolean deleteDevice(Device device) {
        Hub hub = device.getHub();
        boolean result = hubServiceImplementation.deleteDeviceFromDeviceListInHub(hub, device);
        deviceRepository.delete(device);
        return result;
    }

    @Override
    public Device updateDeviceContentById(Long deviceId, Device newDevice) throws DeviceNotFoundException {
        Device oldDevice = findDeviceById(deviceId);
        return updateDeviceContent(oldDevice, newDevice);
    }

    @Override
    public Device updateDeviceContent(Device oldDevice, Device newDevice) {
        Hub hubOfOldDevice = oldDevice.getHub();
        Hub hubOfNewDevice = newDevice.getHub();

        Device result;
        if (!(hubOfOldDevice.equals(hubOfNewDevice))) {
            deleteDevice(oldDevice);
            addDeviceAndBindItToHub(newDevice, hubOfNewDevice);
            result = newDevice;
        }
        else {
            oldDevice.setDeviceType(newDevice.getDeviceType());
            oldDevice.setName(newDevice.getName());
            result = oldDevice;
        }

        return result;
    }

    @Override
    public Device findDeviceById(Long deviceId) throws DeviceNotFoundException {
        return deviceRepository.findById(deviceId).orElseThrow(() -> new DeviceNotFoundException(deviceId));
    }

    @Override
    public void deleteAllDevices() {
        deviceRepository.deleteAll();
    }
}
