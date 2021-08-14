package com.example.hubservice.device.manager.service.interfaces.filtering;

import com.example.backend.device.manager.model.DeviceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ByMasterAndDeviceTypePaginationAndFilteringInterface<T> extends ByMasterPaginationAndFilteringInterface<T> {
    Page<T> findAllByDeviceType(DeviceType deviceType, Pageable pageable);
    Page<T> findAllByNameContainingAndDeviceType(String name, DeviceType deviceType, Pageable pageable);
    Page<T> findAllByDeviceTypeAndMaster_Id(DeviceType deviceType, Long id, Pageable pageable);
    Page<T> findAllByNameContainingAndDeviceTypeAndMaster_Id(String name, DeviceType deviceType, Long id, Pageable pageable);
}
