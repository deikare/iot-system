package com.example.backend.device.manager.repositories;

import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.DeviceType;
import com.example.backend.device.manager.service.interfaces.filtering.ByMasterAndDeviceTypePaginationAndFilteringInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//remember to implement in-place all methods from own filtering interface
@Repository
public interface DeviceRepository extends JpaRepository<Device, Long>,
        ByMasterAndDeviceTypePaginationAndFilteringInterface<Device, Long, String> {
    Page<Device> findByNameContaining(String name, Pageable pageable);
    Page<Device> findByHub_Id(String id, Pageable pageable);
    Page<Device> findByNameContainingAndHub_Id(String name, String id, Pageable pageable);
    Page<Device> findByDeviceType(DeviceType deviceType, Pageable pageable);
    Page<Device> findByNameContainingAndDeviceType(String name, DeviceType deviceType, Pageable pageable);
    Page<Device> findByDeviceTypeAndHub_Id(DeviceType deviceType, String id, Pageable pageable);
    Page<Device> findByNameContainingAndDeviceTypeAndHub_Id(String name, DeviceType deviceType, String id, Pageable pageable);

    @Override
    default Page<Device> findAllByNameContaining(String name, Pageable pageable) {
        return findByNameContaining(name, pageable);
    }

    @Override
    default Page<Device> findAllByMaster_Id(String id, Pageable pageable) {
        return findByHub_Id(id, pageable);
    }

    @Override
    default Page<Device> findAllByNameContainingAndMaster_Id(String name, String id, Pageable pageable) {
        return findByNameContainingAndHub_Id(name, id, pageable);
    }

    @Override
    default Page<Device> findAllByDeviceType(DeviceType deviceType, Pageable pageable) {
        return findByDeviceType(deviceType, pageable);
    }

    @Override
    default Page<Device> findAllByNameContainingAndDeviceType(String name, DeviceType deviceType, Pageable pageable) {
        return findByNameContainingAndDeviceType(name, deviceType, pageable);
    }

    @Override
    default Page<Device> findAllByDeviceTypeAndMaster_Id(DeviceType deviceType, String id, Pageable pageable) {
        return findByDeviceTypeAndHub_Id(deviceType, id, pageable);
    }

    @Override
    default Page<Device> findAllByNameContainingAndDeviceTypeAndMaster_Id(String name, DeviceType deviceType, String id, Pageable pageable) {
        return findByNameContainingAndDeviceTypeAndHub_Id(name, deviceType, id, pageable);
    }
}
