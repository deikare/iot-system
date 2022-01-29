package com.example.backend.device.manager.repositories;

import com.example.backend.device.manager.model.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//remember to implement in-place all methods from own filtering interface
@Repository
public interface HubRepository extends JpaRepository<Hub, String> {
    Page<Hub> findByNameContaining(String name, Pageable pageable);
}
