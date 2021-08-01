package com.example.backend.device.manager.service.wrapper;

import com.example.backend.device.manager.controllers.exceptions.hub.HubNotFoundException;
import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.repositories.HubRepository;
import com.example.backend.device.manager.service.implementation.MasterServiceImplementation;
import org.springframework.stereotype.Service;

@Service
public class HubServiceImplementationWrapper {
    private final MasterServiceImplementation<Hub, Device, HubRepository, HubNotFoundException> hubServiceImplementation;

    public HubServiceImplementationWrapper(MasterServiceImplementation<Hub, Device, HubRepository, HubNotFoundException> hubServiceImplementation) {
        this.hubServiceImplementation = hubServiceImplementation;
    }
}
