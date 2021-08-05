package com.example.backend.device.manager.repositories;

import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.ControlSignalResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ControlSignalResponseRepository extends JpaRepository<ControlSignalResponse, Long> {
    Page<ControlSignalResponse> findByNameContaining(String name, Pageable pageable);
    Page<ControlSignalResponse> findBySentControlSignal_Id(Long id, Pageable pageable);
    Page<ControlSignalResponse> findByMessageContentContaining(String messageContent, Pageable pageable);
    Page<ControlSignalResponse> findByNameContainingAndMessageContentContaining(String name, String messageContent, Pageable pageable);
    Page<ControlSignalResponse> findByMessageContentContainingAndSentControlSignal_Id(String messageContent, Long id, Pageable pageable);
    Page<ControlSignalResponse> findByNameContainingAndSentControlSignal_Id(String name, Long id, Pageable pageable);
    Page<ControlSignalResponse> findByNameContainingAndMessageContentContainingAndSentControlSignal_Id(String name, String messageContent, Long id, Pageable pageable);
}
