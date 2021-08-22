package com.example.backend.device.manager.repositories;

import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.service.interfaces.filtering.BasePaginationAndFilteringInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

//remember to implement in-place all of methods from own filtering interface
@Repository
public interface HubRepository extends JpaRepository<Hub, String>,
        BasePaginationAndFilteringInterface<Hub, String> {
    Page<Hub> findByNameContaining(String name, Pageable pageable);

    @Override
    default Page<Hub> findAllByNameContaining(String name, Pageable pageable) {
        return this.findByNameContaining(name, pageable);
    }
}
