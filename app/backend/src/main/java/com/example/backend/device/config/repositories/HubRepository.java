package com.example.backend.device.config.repositories;

import com.example.backend.device.config.model.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface HubRepository extends JpaRepository<Hub, Long> {
    Page<Hub> findByNameContaining(String text, Pageable pageable);
}
