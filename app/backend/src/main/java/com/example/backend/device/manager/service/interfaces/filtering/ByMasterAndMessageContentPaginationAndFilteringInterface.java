package com.example.backend.device.manager.service.interfaces.filtering;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ByMasterAndMessageContentPaginationAndFilteringInterface<T> extends ByMasterPaginationAndFilteringInterface<T> {
    Page<T> findAllByMessageContentContaining(String messageContent, Pageable pageable);
    Page<T> findAllByNameContainingAndMessageContentContaining(String name, String messageContent, Pageable pageable);
    Page<T> findAllByMessageContentContainingAndMaster_Id(String messageContent, Long id, Pageable pageable);
    Page<T> findAllByNameContainingAndMessageContentContainingAndMaster_Id(String name, String messageContent, Long id, Pageable pageable);
}
