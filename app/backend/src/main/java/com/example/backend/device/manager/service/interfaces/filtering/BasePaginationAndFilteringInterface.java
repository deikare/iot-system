package com.example.backend.device.manager.service.interfaces.filtering;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BasePaginationAndFilteringInterface<T> {
    Page<T> find(Pageable pageable);
    Page<T> findByNameContaining(String name, Pageable pageable);
}
