package com.example.backend.device.manager.repositories;

import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.DeviceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//remember to implement in-place all methods from own filtering interface
@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    Page<Device> findByNameContaining(String name, Pageable pageable);
    Page<Device> findByHub_Id(String id, Pageable pageable);
    Page<Device> findByNameContainingAndHub_Id(String name, String id, Pageable pageable);
    Page<Device> findByDeviceType(DeviceType deviceType, Pageable pageable);
    Page<Device> findByNameContainingAndDeviceType(String name, DeviceType deviceType, Pageable pageable);
    Page<Device> findByDeviceTypeAndHub_Id(DeviceType deviceType, String id, Pageable pageable);
    Page<Device> findByNameContainingAndDeviceTypeAndHub_Id(String name, DeviceType deviceType, String id, Pageable pageable);
}
