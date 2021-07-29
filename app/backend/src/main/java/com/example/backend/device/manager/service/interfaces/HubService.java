package com.example.backend.device.manager.service.interfaces;

import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HubService {
    Hub addHub(Hub hub);
    Page<Hub> getAllHubs(Pageable pageable);
    Page<Hub> getAllHubsByNameContaining(String name, Pageable pageable);
    Hub findHubById(Long id);
    Hub updateHubNameById(Long id, String name);
    void deleteHubById(Long id);
    Hub addDeviceToDeviceListInHubByHubId(Long hubId, Device device);
    Hub addDeviceToDeviceListInHub(Hub hub, Device device);
    boolean deleteDeviceFromDeviceListInHubByHubId(Long hubId, Device device);
    boolean deleteDeviceFromDeviceListInHub(Hub hub, Device device);
    void deleteAllHubs();
}
