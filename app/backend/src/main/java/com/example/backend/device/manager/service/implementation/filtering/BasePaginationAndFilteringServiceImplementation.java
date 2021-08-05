package com.example.backend.device.manager.service.implementation.filtering;

import com.example.backend.device.manager.service.interfaces.filtering.BasePaginationAndFilteringInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BasePaginationAndFilteringServiceImplementation<B> implements BasePaginationAndFilteringInterface<B> {
    private final BasePaginationAndFilteringInterface<B> repository;

    public BasePaginationAndFilteringServiceImplementation(BasePaginationAndFilteringInterface<B> repository) {
        this.repository = repository;
    }

//    private R repository;

    @Override
    public Page<B> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<B> findAllByNameContaining(String name, Pageable pageable) {
        return repository.findAllByNameContaining(name, pageable);
    }
}
