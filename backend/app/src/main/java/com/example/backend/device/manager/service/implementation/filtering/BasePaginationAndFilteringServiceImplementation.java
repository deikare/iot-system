package com.example.backend.device.manager.service.implementation.filtering;

import com.example.backend.device.manager.service.interfaces.filtering.BasePaginationAndFilteringInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class BasePaginationAndFilteringServiceImplementation<B, K> implements BasePaginationAndFilteringInterface<B, K> {
    private final BasePaginationAndFilteringInterface<B, K> repository;

    public BasePaginationAndFilteringServiceImplementation(BasePaginationAndFilteringInterface<B, K> repository) {
        this.repository = repository;
    }

    @Override
    public Page<B> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<B> findAllByNameContaining(String name, Pageable pageable) {
        return repository.findAllByNameContaining(name, pageable);
    }
}
