package com.example.backend.device.manager.repositories;

import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.ControlSignalResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ControlSignalResponseRepository extends JpaRepository<ControlSignalResponse, Long> {
    Page<ControlSignalResponse> findControlSignalResponseByNameContaining(String name, Pageable pageable);
    Page<ControlSignalResponse> findControlSignalResponseBySentControlSignal_Id(Long id, Pageable pageable);
    Page<ControlSignalResponse> findControlSignalResponseByMessageContentContaining(String messageContent, Pageable pageable);
    Page<ControlSignalResponse> findControlSignalResponseByNameContainingAndMessageContentContaining(String name, String messageContent, Pageable pageable);
    Page<ControlSignalResponse> findControlSignalResponseByMessageContentContainingAndSentControlSignal_Id(String messageContent, Long id, Pageable pageable);
    Page<ControlSignalResponse> findControlSignalResponseByNameContainingAndSentControlSignal_Id(String name, Long id, Pageable pageable);
    Page<ControlSignalResponse> findControlSignalResponseByNameContainingAndMessageContentContainingAndSentControlSignal_Id(String name, String messageContent, Long id, Pageable pageable);
}
