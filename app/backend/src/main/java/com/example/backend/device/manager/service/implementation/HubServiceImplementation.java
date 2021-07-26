package com.example.backend.device.manager.service.implementation;

import com.example.backend.device.manager.controllers.exceptions.hub.HubNotFoundException;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.repositories.HubRepository;
import com.example.backend.device.manager.service.interfaces.HubService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class HubServiceImplementation implements HubService {
    private final HubRepository hubRepository;

    public HubServiceImplementation(HubRepository hubRepository) {
        this.hubRepository = hubRepository;
    }

    @Override
    public Hub addHub(Hub hub) {
        return hubRepository.save(hub);
    }

    @Override
    public Page<Hub> getAllHubs(Pageable pageable) {
        return hubRepository.findAll(pageable);
    }

    @Override
    public Page<Hub> getAllHubsByNameContaining(String name, Pageable pageable) {
        return hubRepository.findAllByNameContaining(name, pageable);
    }

    @Override
    public Hub findHubById(Long id) throws HubNotFoundException {
        return hubRepository.findById(id).orElseThrow(() -> new HubNotFoundException(id));
    }

    @Override
    public Hub updateHubNameById(Long id, String name) throws HubNotFoundException {
        Hub hub = findHubById(id);
        hub.setName(name);
        return hub;
    }

    @Override
    public void deleteHubById(Long id) {
        hubRepository.deleteById(id);
    }

    @Override
    public Hub addDeviceToDeviceListInHubByHubId(Long hubId, Device device) throws HubNotFoundException{
        Hub hub = findHubById(hubId);
        return addDeviceToDeviceListInHub(hub, device);
    }

    @Override
    public Hub addDeviceToDeviceListInHub(Hub hub, Device device) {
        hub.getDevices().add(device);
        return hub;
    }

    @Override
    public boolean deleteDeviceFromDeviceListInHubByHubId(Long hubId, Device device) throws HubNotFoundException{
        Hub hub = findHubById(hubId);
        return hub.removeDeviceFromDeviceList(device);
    }

    @Override
    public boolean deleteDeviceFromDeviceListInHub(Hub hub, Device device) {
        return hub.removeDeviceFromDeviceList(device);
    }
}
