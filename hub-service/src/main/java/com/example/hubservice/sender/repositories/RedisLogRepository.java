package com.example.hubservice.sender.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface RedisLogRepository extends CrudRepository<RedisLogRepository, Instant> {
}
