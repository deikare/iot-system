package com.example.backend.device.manager.service.implementation;

import com.example.backend.device.manager.controllers.exceptions.hub.HubNotFoundException;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.repositories.HubRepository;
import com.example.backend.device.manager.service.implementation.MasterServiceImplementation;
import org.springframework.stereotype.Service;

@Service
public class HubServiceImplementation2 {
    private final MasterServiceImplementation<Hub, Device, HubRepository, HubNotFoundException> hubServiceImplementation;

    public HubServiceImplementation2(MasterServiceImplementation<Hub, Device, HubRepository, HubNotFoundException> hubServiceImplementation) {
        this.hubServiceImplementation = hubServiceImplementation;
    }
}
