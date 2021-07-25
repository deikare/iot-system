package com.example.backend.device.manager.service.implementation;

import com.example.backend.device.manager.controllers.exceptions.HubNotFoundException;
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
        //TODO requires to throw an exception if there is already a hub with specified ID
    }

    @Override
    public Page<Hub> getAllHubs(Pageable pageable) {
        return hubRepository.findAll(pageable);
    }

    @Override
    public Page<Hub> getAllHubsContainingName(String name, Pageable pageable) {
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
}
