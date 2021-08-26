package com.example.backend.device.manager.service.interfaces.filtering;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BasePaginationAndFilteringInterface<B, K> {
    Page<B> findAll(Pageable pageable);
    Page<B> findAllByNameContaining(String name, Pageable pageable);
}
