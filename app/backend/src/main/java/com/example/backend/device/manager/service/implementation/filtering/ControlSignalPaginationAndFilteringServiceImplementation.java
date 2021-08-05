package com.example.backend.device.manager.service.implementation.filtering;

import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.repositories.ControlSignalRepository;
import com.example.backend.device.manager.service.interfaces.filtering.ByMasterAndMessageContainingPaginationAndFilteringInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ControlSignalPaginationAndFilteringServiceImplementation implements ByMasterAndMessageContainingPaginationAndFilteringInterface<ControlSignal> {
    private final ControlSignalRepository repository;

    public ControlSignalPaginationAndFilteringServiceImplementation(ControlSignalRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<ControlSignal> findByNameContaining(String name, Pageable pageable) {
        return repository.findByNameContaining(name, pageable);
    }

    @Override
    public Page<ControlSignal> findByMessageContentContaining(String messageContent, Pageable pageable) {
        return repository.findByMessageContentContaining(messageContent, pageable);
    }

    @Override
    public Page<ControlSignal> findByNameContainingAndMessageContentContaining(String name, String messageContent, Pageable pageable) {
        return repository.findByNameContainingAndMessageContentContaining(name, messageContent, pageable);
    }

    @Override
    public Page<ControlSignal> findByMessageContentContainingAndMaster_Id(String messageContent, Long id, Pageable pageable) {
        return repository.findByMessageContentContainingAndDevice_Id(messageContent, id, pageable);
    }

    @Override
    public Page<ControlSignal> findByNameContainingAndMessageContentContainingAndMaster_Id(String name, String messageContent, Long id, Pageable pageable) {
        return repository.findByNameContainingAndMessageContentContainingAndDevice_Id(name, messageContent, id, pageable);
    }

    @Override
    public Page<ControlSignal> findByMaster_Id(Long id, Pageable pageable) {
        return repository.findByDevice_Id(id, pageable);
    }

    @Override
    public Page<ControlSignal> findByNameContainingAndMaster_Id(String name, Long id, Pageable pageable) {
        return repository.findByNameContainingAndDevice_Id(name, id, pageable);
    }
}
