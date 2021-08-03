package com.example.backend.device.manager.repositories;

import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ControlSignalRepository extends JpaRepository<ControlSignal, Long> {
    Page<ControlSignal> findControlSignalByNameContaining(String name, Pageable pageable);
    Page<ControlSignal> findControlSignalByDevice_Id(Long deviceId, Pageable pageable);
    Page<ControlSignal> findControlSignalByMessageContentContaining(String messageContent, Pageable pageable);
    Page<ControlSignal> findControlSignalByNameContainingAndMessageContentContaining(String name, String messageContent, Pageable pageable);
    Page<ControlSignal> findControlSignalByNameContainingAndDevice_Id(String name, Long deviceId, Pageable pageable);
    Page<ControlSignal> findControlSignalByMessageContentContainingAndDevice_Id(String messageContent, Long deviceId, Pageable pageable);
    Page<ControlSignal> findControlSignalByNameContainingAndMessageContentContainingAndDevice_Id(String name, String messageContent, Long deviceId, Pageable pageable);
}
