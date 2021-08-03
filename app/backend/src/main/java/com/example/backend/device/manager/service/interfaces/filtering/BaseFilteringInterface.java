package com.example.backend.device.manager.service.interfaces.filtering;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BaseFilteringInterface<T> {
    Page<T> findByNameContaining(String name, Pageable pageable);
}
