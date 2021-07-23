package com.example.backend.device.config.repositories;

import com.example.backend.device.config.model.Device;
import com.example.backend.device.config.model.DeviceType;
import com.example.backend.device.config.model.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    Page<Device> findDeviceByNameContaining(String text, Pageable pageable);
    Page<Device> findDeviceByHub(Hub hub, Pageable pageable);
    Page<Device> findDeviceByDeviceType(DeviceType deviceType, Pageable pageable);
}
