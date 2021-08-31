package com.example.hubservice.management.hub.respositories;

import com.example.hubservice.management.hub.model.Hub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HubRepository extends JpaRepository<Hub, String> {
}
