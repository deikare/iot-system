package com.example.backend.device.manager.service.implementation.filtering;

import com.example.backend.device.manager.service.interfaces.filtering.ByMasterAndMessageContentPaginationAndFilteringInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ByMasterAndMessageContentContainingPaginationAndFilteringServiceImplementation<B, K, K_M> extends ByMasterPaginationAndFilteringServiceImplementation<B, K, K_M> implements ByMasterAndMessageContentPaginationAndFilteringInterface<B, K, K_M> {
    private final ByMasterAndMessageContentPaginationAndFilteringInterface<B, K, K_M> repository;

    public ByMasterAndMessageContentContainingPaginationAndFilteringServiceImplementation(ByMasterAndMessageContentPaginationAndFilteringInterface<B, K, K_M> repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public Page<B> findAllByMessageContentContaining(String messageContent, Pageable pageable) {
        return repository.findAllByMessageContentContaining(messageContent, pageable);
    }

    @Override
    public Page<B> findAllByNameContainingAndMessageContentContaining(String name, String messageContent, Pageable pageable) {
        return repository.findAllByNameContainingAndMessageContentContaining(name, messageContent, pageable);
    }

    @Override
    public Page<B> findAllByMessageContentContainingAndMaster_Id(String messageContent, K_M id, Pageable pageable) {
        return repository.findAllByMessageContentContainingAndMaster_Id(messageContent, id, pageable);
    }

    @Override
    public Page<B> findAllByNameContainingAndMessageContentContainingAndMaster_Id(String name, String messageContent, K_M id, Pageable pageable) {
        return repository.findAllByNameContainingAndMessageContentContainingAndMaster_Id(name, messageContent, id, pageable);
    }
}
