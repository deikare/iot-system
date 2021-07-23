package com.example.backend.device.config.repositories;

import com.example.backend.device.config.model.ControlSignal;
import com.example.backend.device.config.model.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ControlSignalRepository extends JpaRepository<ControlSignal, Long> {
    Page<ControlSignal> findControlSignalByNameContains(String text, Pageable pageable);
    Page<ControlSignal> findControlSignalByDevice(Device device, Pageable pageable);
    Page<ControlSignal> findControlSignalByMessageContentContaining(String text, Pageable pageable);
}
