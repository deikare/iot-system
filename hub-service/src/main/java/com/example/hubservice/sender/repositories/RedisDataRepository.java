package com.example.hubservice.sender.repositories;

import com.example.hubservice.sender.model.InfluxDataPojo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface RedisDataRepository extends CrudRepository<InfluxDataPojo, Instant> {
}
