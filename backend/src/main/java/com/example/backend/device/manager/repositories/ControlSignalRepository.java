package com.example.backend.device.manager.repositories;

import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.service.interfaces.filtering.ByMasterAndMessageContentPaginationAndFilteringInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//remember to implement in-place all of methods from own filtering interface
@Repository
public interface ControlSignalRepository extends JpaRepository<ControlSignal, Long>, ByMasterAndMessageContentPaginationAndFilteringInterface<ControlSignal, Long, Long> {
    Page<ControlSignal> findByNameContaining(String name, Pageable pageable);
    Page<ControlSignal> findByDevice_Id(Long deviceId, Pageable pageable);
    Page<ControlSignal> findByMessageContentContaining(String messageContent, Pageable pageable);
    Page<ControlSignal> findByNameContainingAndMessageContentContaining(String name, String messageContent, Pageable pageable);
    Page<ControlSignal> findByNameContainingAndDevice_Id(String name, Long deviceId, Pageable pageable);
    Page<ControlSignal> findByMessageContentContainingAndDevice_Id(String messageContent, Long deviceId, Pageable pageable);
    Page<ControlSignal> findByNameContainingAndMessageContentContainingAndDevice_Id(String name, String messageContent, Long deviceId, Pageable pageable);

    @Override
    default Page<ControlSignal> findAllByNameContaining(String name, Pageable pageable) {
        return findByNameContaining(name, pageable);
    }

    @Override
    default Page<ControlSignal> findAllByMessageContentContaining(String messageContent, Pageable pageable) {
        return findByMessageContentContaining(messageContent, pageable);
    }

    @Override
    default Page<ControlSignal> findAllByNameContainingAndMessageContentContaining(String name, String messageContent, Pageable pageable) {
        return findByNameContainingAndMessageContentContaining(name, messageContent, pageable);
    }

    @Override
    default Page<ControlSignal> findAllByMessageContentContainingAndMaster_Id(String messageContent, Long id, Pageable pageable) {
        return findByMessageContentContainingAndDevice_Id(messageContent, id, pageable);
    }

    @Override
    default Page<ControlSignal> findAllByNameContainingAndMessageContentContainingAndMaster_Id(String name, String messageContent, Long id, Pageable pageable) {
        return findByNameContainingAndMessageContentContainingAndDevice_Id(name, messageContent, id, pageable);
    }

    @Override
    default Page<ControlSignal> findAllByMaster_Id(Long id, Pageable pageable) {
        return findByDevice_Id(id, pageable);
    }

    @Override
    default Page<ControlSignal> findAllByNameContainingAndMaster_Id(String name, Long id, Pageable pageable) {
        return findByNameContainingAndDevice_Id(name, id, pageable);
    }
}
