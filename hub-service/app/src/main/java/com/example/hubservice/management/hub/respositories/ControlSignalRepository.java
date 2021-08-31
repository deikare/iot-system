package com.example.hubservice.management.hub.respositories;

import com.example.hubservice.management.hub.model.ControlSignal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ControlSignalRepository extends JpaRepository<ControlSignal, Long> {
}
