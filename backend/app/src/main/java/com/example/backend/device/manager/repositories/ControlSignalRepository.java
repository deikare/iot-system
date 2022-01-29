package com.example.backend.device.manager.repositories;

import com.example.backend.device.manager.model.ControlSignal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//remember to implement in-place all methods from own filtering interface
@Repository
public interface ControlSignalRepository extends JpaRepository<ControlSignal, Long> {
    Page<ControlSignal> findByNameContaining(String name, Pageable pageable);
    Page<ControlSignal> findByDevice_Id(Long deviceId, Pageable pageable);
    Page<ControlSignal> findByMessageContentContaining(String messageContent, Pageable pageable);
    Page<ControlSignal> findByNameContainingAndMessageContentContaining(String name, String messageContent, Pageable pageable);
    Page<ControlSignal> findByNameContainingAndDevice_Id(String name, Long deviceId, Pageable pageable);
    Page<ControlSignal> findByMessageContentContainingAndDevice_Id(String messageContent, Long deviceId, Pageable pageable);
    Page<ControlSignal> findByNameContainingAndMessageContentContainingAndDevice_Id(String name, String messageContent, Long deviceId, Pageable pageable);
}
