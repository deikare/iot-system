package com.example.backend.device.manager.service.interfaces;

import com.example.backend.device.manager.model.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HubService {
    Hub addHub(Hub hub);
    Page<Hub> getAllHubs(Pageable pageable);
    Page<Hub> getAllHubsContainingName(String name, Pageable pageable);
    Hub findHubById(Long id);
    Hub updateHubNameById(Long id, String name);
    void deleteHubById(Long id);
    // TODO getDevicesWithPaginationAndFiltering

}
