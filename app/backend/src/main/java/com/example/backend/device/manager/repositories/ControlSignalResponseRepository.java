package com.example.backend.device.manager.repositories;

import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.ControlSignalResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ControlSignalResponseRepository extends JpaRepository<ControlSignalResponse, Long> {
    Page<ControlSignalResponse> findControlSignalResponseByNameContains(String text, Pageable pageable);
    Page<ControlSignalResponse> findControlSignalResponseBySentControlSignal(ControlSignal sentControlSignal, Pageable pageable);
    Page<ControlSignalResponse> findControlSignalByMessageContentContaining(String text, Pageable pageable);
    //Page<ControlSignalResponse> fi
}
