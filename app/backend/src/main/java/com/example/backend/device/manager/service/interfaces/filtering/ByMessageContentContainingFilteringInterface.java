package com.example.backend.device.manager.service.interfaces.filtering;

import org.springframework.data.domain.Page;

public interface ByMessageContentContainingFilteringInterface<T> extends BaseFilteringInterface<T> {
    Page<T>
}
