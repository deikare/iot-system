package com.example.backend.device.manager.service.interfaces.filtering;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ByMasterAndMessageContentPaginationAndFilteringInterface<B, K, K_M> extends ByMasterPaginationAndFilteringInterface<B, K, K_M> {
    Page<B> findAllByMessageContentContaining(String messageContent, Pageable pageable);
    Page<B> findAllByNameContainingAndMessageContentContaining(String name, String messageContent, Pageable pageable);
    Page<B> findAllByMessageContentContainingAndMaster_Id(String messageContent, K_M id, Pageable pageable);
    Page<B> findAllByNameContainingAndMessageContentContainingAndMaster_Id(String name, String messageContent, K_M id, Pageable pageable);
}
