package com.example.backend.device.manager.service.implementation.filtering;

import com.example.backend.device.manager.model.DeviceType;
import com.example.backend.device.manager.service.interfaces.filtering.ByMasterAndDeviceTypePaginationAndFilteringInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ByMasterAndDeviceTypePaginationAndFilteringServiceImplementation<B, K, K_M> extends ByMasterPaginationAndFilteringServiceImplementation<B, K, K_M> implements ByMasterAndDeviceTypePaginationAndFilteringInterface<B, K, K_M> {
    private final ByMasterAndDeviceTypePaginationAndFilteringInterface<B, K, K_M> repository;

    public ByMasterAndDeviceTypePaginationAndFilteringServiceImplementation(ByMasterAndDeviceTypePaginationAndFilteringInterface<B, K, K_M> repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public Page<B> findAllByDeviceType(DeviceType deviceType, Pageable pageable) {
        return repository.findAllByDeviceType(deviceType, pageable);
    }

    @Override
    public Page<B> findAllByNameContainingAndDeviceType(String name, DeviceType deviceType, Pageable pageable) {
        return repository.findAllByNameContainingAndDeviceType(name, deviceType, pageable);
    }

    @Override
    public Page<B> findAllByDeviceTypeAndMaster_Id(DeviceType deviceType, K_M id, Pageable pageable) {
        return repository.findAllByDeviceTypeAndMaster_Id(deviceType, id, pageable);
    }

    @Override
    public Page<B> findAllByNameContainingAndDeviceTypeAndMaster_Id(String name, DeviceType deviceType, K_M id, Pageable pageable) {
        return repository.findAllByNameContainingAndDeviceTypeAndMaster_Id(name, deviceType, id, pageable);
    }
}
