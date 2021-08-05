package com.example.backend.device.manager.service.implementation.filtering;

import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.repositories.DeviceRepository;
import com.example.backend.device.manager.service.interfaces.filtering.ByMasterPaginationAndFilteringInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class DevicePaginationAndFilteringServiceImplementation implements ByMasterPaginationAndFilteringInterface<Device> {
    private final DeviceRepository repository;

    public DevicePaginationAndFilteringServiceImplementation(DeviceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Device> findByNameContaining(String name, Pageable pageable) {
        return repository.findByNameContaining(name, pageable);
    }

    @Override
    public Page<Device> findByMaster_Id(Long id, Pageable pageable) {
        return repository.findByHub_Id(id, pageable);
    }

    @Override
    public Page<Device> findByNameContainingAndMaster_Id(String name, Long id, Pageable pageable) {
        return repository.findByNameContainingAndHub_Id(name, id, pageable);
    }
}
