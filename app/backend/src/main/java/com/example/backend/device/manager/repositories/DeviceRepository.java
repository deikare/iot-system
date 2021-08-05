package com.example.backend.device.manager.repositories;

import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.DeviceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    Page<Device> findByNameContaining(String name, Pageable pageable);
    Page<Device> findByHub_Id(Long id, Pageable pageable);
    Page<Device> findByNameContainingAndHub_Id(String name, Long id, Pageable pageable);
    Page<Device> findByDeviceType(DeviceType deviceType, Pageable pageable);
}
