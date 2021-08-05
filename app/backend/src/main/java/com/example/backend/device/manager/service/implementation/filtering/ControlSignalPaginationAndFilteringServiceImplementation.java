package com.example.backend.device.manager.service.implementation.filtering;

import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.repositories.ControlSignalRepository;
import com.example.backend.device.manager.service.interfaces.filtering.ByMasterAndMessageContentPaginationAndFilteringInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ControlSignalPaginationAndFilteringServiceImplementation implements ByMasterAndMessageContentPaginationAndFilteringInterface<ControlSignal> {
    private final ControlSignalRepository repository;

    public ControlSignalPaginationAndFilteringServiceImplementation(ControlSignalRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<ControlSignal> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Page<ControlSignal> findAllByNameContaining(String name, Pageable pageable) {
        return repository.findByNameContaining(name, pageable);
    }

    @Override
    public Page<ControlSignal> findAllByMessageContentContaining(String messageContent, Pageable pageable) {
        return repository.findByMessageContentContaining(messageContent, pageable);
    }

    @Override
    public Page<ControlSignal> findAllByNameContainingAndMessageContentContaining(String name, String messageContent, Pageable pageable) {
        return repository.findByNameContainingAndMessageContentContaining(name, messageContent, pageable);
    }

    @Override
    public Page<ControlSignal> findAllByMessageContentContainingAndMaster_Id(String messageContent, Long id, Pageable pageable) {
        return repository.findByMessageContentContainingAndDevice_Id(messageContent, id, pageable);
    }

    @Override
    public Page<ControlSignal> findAllByNameContainingAndMessageContentContainingAndMaster_Id(String name, String messageContent, Long id, Pageable pageable) {
        return repository.findByNameContainingAndMessageContentContainingAndDevice_Id(name, messageContent, id, pageable);
    }

    @Override
    public Page<ControlSignal> findAllByMaster_Id(Long id, Pageable pageable) {
        return repository.findByDevice_Id(id, pageable);
    }

    @Override
    public Page<ControlSignal> findAllByNameContainingAndMaster_Id(String name, Long id, Pageable pageable) {
        return repository.findByNameContainingAndDevice_Id(name, id, pageable);
    }
}
