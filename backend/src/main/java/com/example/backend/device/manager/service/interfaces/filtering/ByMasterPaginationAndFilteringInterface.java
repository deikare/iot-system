package com.example.backend.device.manager.service.interfaces.filtering;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ByMasterPaginationAndFilteringInterface<B, K, K_M> extends BasePaginationAndFilteringInterface<B, K> {
    Page<B> findAllByMaster_Id(K_M id, Pageable pageable);
    Page<B> findAllByNameContainingAndMaster_Id(String name, K_M id, Pageable pageable);
}
