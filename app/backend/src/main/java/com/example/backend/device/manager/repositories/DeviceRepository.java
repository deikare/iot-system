package com.example.backend.device.manager.repositories;

import com.example.backend.device.manager.model.Device;
import com.example.backend.device.manager.model.DeviceType;
import com.example.backend.device.manager.service.interfaces.filtering.ByMasterPaginationAndFilteringInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long>, ByMasterPaginationAndFilteringInterface<Device> {
    Page<Device> findByNameContaining(String name, Pageable pageable);
    Page<Device> findByHub_Id(Long id, Pageable pageable);
    Page<Device> findByNameContainingAndHub_Id(String name, Long id, Pageable pageable);
    Page<Device> findByDeviceType(DeviceType deviceType, Pageable pageable);

    @Override
    default Page<Device> findAllByNameContaining(String name, Pageable pageable) {
        return findByNameContaining(name, pageable);
    }

    @Override
    default Page<Device> findAllByMaster_Id(Long id, Pageable pageable) {
        return findByHub_Id(id, pageable);
    }

    @Override
    default Page<Device> findAllByNameContainingAndMaster_Id(String name, Long id, Pageable pageable) {
        return findByNameContainingAndHub_Id(name, id, pageable);
    }
}
