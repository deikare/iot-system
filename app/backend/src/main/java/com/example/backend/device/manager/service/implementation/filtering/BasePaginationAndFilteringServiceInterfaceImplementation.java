package com.example.backend.device.manager.service.implementation.filtering;

import com.example.backend.device.manager.service.interfaces.filtering.BasePaginationAndFilteringInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public class BasePaginationAndFilteringServiceInterfaceImplementation<B> implements BasePaginationAndFilteringInterface<B> {
    private final JpaRepository<B, Long> repository;

    public BasePaginationAndFilteringServiceInterfaceImplementation(JpaRepository<B, Long> repository) {
        this.repository = repository;
    }

    @Override
    public Page<B> find(Pageable pageable) {
        return null;
    }

    @Override
    public Page<B> findByNameContaining(String name, Pageable pageable) {
        return null;
    }
}
