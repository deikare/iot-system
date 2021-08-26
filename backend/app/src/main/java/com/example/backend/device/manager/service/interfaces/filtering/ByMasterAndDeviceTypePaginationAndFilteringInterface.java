package com.example.backend.device.manager.service.interfaces.filtering;

import com.example.backend.device.manager.model.DeviceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ByMasterAndDeviceTypePaginationAndFilteringInterface<B, K, K_M> extends ByMasterPaginationAndFilteringInterface<B, K, K_M> {
    Page<B> findAllByDeviceType(DeviceType deviceType, Pageable pageable);
    Page<B> findAllByNameContainingAndDeviceType(String name, DeviceType deviceType, Pageable pageable);
    Page<B> findAllByDeviceTypeAndMaster_Id(DeviceType deviceType, K_M id, Pageable pageable);
    Page<B> findAllByNameContainingAndDeviceTypeAndMaster_Id(String name, DeviceType deviceType, K_M id, Pageable pageable);
}
