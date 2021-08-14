package com.example.hubservice.device.manager.service.interfaces.filtering;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ByMasterPaginationAndFilteringInterface<T> extends BasePaginationAndFilteringInterface<T> {
    Page<T> findAllByMaster_Id(Long id, Pageable pageable);
    Page<T> findAllByNameContainingAndMaster_Id(String name, Long id, Pageable pageable);
}
