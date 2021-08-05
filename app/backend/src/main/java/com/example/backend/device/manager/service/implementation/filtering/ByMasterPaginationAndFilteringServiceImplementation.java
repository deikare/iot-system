package com.example.backend.device.manager.service.implementation.filtering;

import com.example.backend.device.manager.service.interfaces.filtering.ByMasterPaginationAndFilteringInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ByMasterPaginationAndFilteringServiceImplementation<B> extends BasePaginationAndFilteringServiceImplementation<B> implements ByMasterPaginationAndFilteringInterface<B> {
    private final ByMasterPaginationAndFilteringInterface<B> repository;

    public ByMasterPaginationAndFilteringServiceImplementation(ByMasterPaginationAndFilteringInterface<B> repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public Page<B> findAllByMaster_Id(Long id, Pageable pageable) {
        return repository.findAllByMaster_Id(id, pageable);
    }

    @Override
    public Page<B> findAllByNameContainingAndMaster_Id(String name, Long id, Pageable pageable) {
        return repository.findAllByNameContainingAndMaster_Id(name, id, pageable);
    }
}
