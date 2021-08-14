package com.example.hubservice.device.manager.service.interfaces.filtering;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BasePaginationAndFilteringInterface<T> {
    Page<T> findAll(Pageable pageable);
    Page<T> findAllByNameContaining(String name, Pageable pageable);
}
