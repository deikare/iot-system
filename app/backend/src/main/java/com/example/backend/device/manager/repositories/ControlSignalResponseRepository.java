package com.example.backend.device.manager.repositories;

import com.example.backend.device.manager.model.ControlSignalResponse;
import com.example.backend.device.manager.service.interfaces.filtering.ByMasterAndMessageContentPaginationAndFilteringInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//remember to implement in-place all of methods from own filtering interface
@Repository
public interface ControlSignalResponseRepository extends JpaRepository<ControlSignalResponse, Long>, ByMasterAndMessageContentPaginationAndFilteringInterface<ControlSignalResponse> {
    Page<ControlSignalResponse> findByNameContaining(String name, Pageable pageable);
    Page<ControlSignalResponse> findBySentControlSignal_Id(Long id, Pageable pageable);
    Page<ControlSignalResponse> findByMessageContentContaining(String messageContent, Pageable pageable);
    Page<ControlSignalResponse> findByNameContainingAndMessageContentContaining(String name, String messageContent, Pageable pageable);
    Page<ControlSignalResponse> findByMessageContentContainingAndSentControlSignal_Id(String messageContent, Long id, Pageable pageable);
    Page<ControlSignalResponse> findByNameContainingAndSentControlSignal_Id(String name, Long id, Pageable pageable);
    Page<ControlSignalResponse> findByNameContainingAndMessageContentContainingAndSentControlSignal_Id(String name, String messageContent, Long id, Pageable pageable);

    @Override
    default Page<ControlSignalResponse> findAllByNameContaining(String name, Pageable pageable) {
        return findByNameContaining(name, pageable);
    }

    @Override
    default Page<ControlSignalResponse> findAllByMessageContentContaining(String messageContent, Pageable pageable) {
        return findByMessageContentContaining(messageContent, pageable);
    }

    @Override
    default Page<ControlSignalResponse> findAllByNameContainingAndMessageContentContaining(String name, String messageContent, Pageable pageable) {
        return findByNameContainingAndMessageContentContaining(name, messageContent, pageable);
    }

    @Override
    default Page<ControlSignalResponse> findAllByMessageContentContainingAndMaster_Id(String messageContent, Long id, Pageable pageable) {
        return findByMessageContentContainingAndSentControlSignal_Id(messageContent, id, pageable);
    }

    @Override
    default Page<ControlSignalResponse> findAllByNameContainingAndMessageContentContainingAndMaster_Id(String name, String messageContent, Long id, Pageable pageable) {
        return findByNameContainingAndMessageContentContainingAndSentControlSignal_Id(name, messageContent, id, pageable);
    }

    @Override
    default Page<ControlSignalResponse> findAllByMaster_Id(Long id, Pageable pageable) {
        return findBySentControlSignal_Id(id, pageable);
    }

    @Override
    default Page<ControlSignalResponse> findAllByNameContainingAndMaster_Id(String name, Long id, Pageable pageable) {
        return findByNameContainingAndSentControlSignal_Id(name, id, pageable);
    }
}
