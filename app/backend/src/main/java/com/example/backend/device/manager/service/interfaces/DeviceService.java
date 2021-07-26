package com.example.backend.device.manager.service.interfaces;

import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeviceService {
    Device addDeviceAndBindItToHubById(Device device, Long hubId);
    Device addDeviceAndBindItToHub(Device device, Hub hub);
    Page<Device> getAllDevices(Pageable pageable);
    Page<Device> getAllDevicesByNameContaining(String name, Pageable pageable);
    Page<Device> getAllDevicesByHubId(Long hubId, Pageable pageable);
    Page<Device> getAllDevicesByNameContainingAndHubId(String name, Long hubId, Pageable pageable);
    void removeDeviceById(Long deviceId);
    boolean removeDevice(Device device);
    Device updateDeviceContentById(Long deviceId, Device newDevice);
    Device updateDeviceContent(Device oldDevice, Device newDevice);
    Device findDeviceById(Long deviceId);
}
