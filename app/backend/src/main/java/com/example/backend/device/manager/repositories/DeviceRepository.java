package com.example.backend.device.manager.repositories;

import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.DeviceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    Page<Device> findAllByNameContaining(String name, Pageable pageable);
    Page<Device> findAllByHub_Id(Long id, Pageable pageable);
    Page<Device> findDeviceByDeviceType(DeviceType deviceType, Pageable pageable);
}
