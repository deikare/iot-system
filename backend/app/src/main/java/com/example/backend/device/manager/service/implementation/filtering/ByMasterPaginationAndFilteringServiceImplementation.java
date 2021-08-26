package com.example.backend.device.manager.service.implementation.filtering;

import com.example.backend.device.manager.service.interfaces.filtering.ByMasterPaginationAndFilteringInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ByMasterPaginationAndFilteringServiceImplementation<B, K, K_M> extends BasePaginationAndFilteringServiceImplementation<B, K> implements ByMasterPaginationAndFilteringInterface<B, K, K_M> {
    private final ByMasterPaginationAndFilteringInterface<B, K, K_M> repository;

    public ByMasterPaginationAndFilteringServiceImplementation(ByMasterPaginationAndFilteringInterface<B, K, K_M> repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public Page<B> findAllByMaster_Id(K_M id, Pageable pageable) {
        return repository.findAllByMaster_Id(id, pageable);
    }

    @Override
    public Page<B> findAllByNameContainingAndMaster_Id(String name, K_M id, Pageable pageable) {
        return repository.findAllByNameContainingAndMaster_Id(name, id, pageable);
    }
}
