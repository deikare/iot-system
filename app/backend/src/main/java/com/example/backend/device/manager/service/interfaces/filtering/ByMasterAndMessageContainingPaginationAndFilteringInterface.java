package com.example.backend.device.manager.service.interfaces.filtering;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ByMasterAndMessageContainingPaginationAndFilteringInterface<T> extends ByMasterPaginationAndFilteringInterface<T> {
    Page<T> findByMessageContentContaining(String messageContent, Pageable pageable);
    Page<T> findByNameContainingAndMessageContentContaining(String name, String messageContent, Pageable pageable);
    Page<T> findByMessageContentContainingAndMaster_Id(String messageContent, Long id, Pageable pageable);
    Page<T> findByNameContainingAndMessageContentContainingAndMaster_Id(String name, String messageContent, Long id, Pageable pageable);
}
