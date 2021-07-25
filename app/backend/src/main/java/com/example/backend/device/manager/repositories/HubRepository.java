package com.example.backend.device.manager.repositories;

import com.example.backend.device.manager.model.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HubRepository extends JpaRepository<Hub, Long> {
    Page<Hub> findAllByNameContaining(String name, Pageable pageable);
    Page<Hub> findByNameContaining(String name, Pageable pageable);
}
