package com.example.backend.device.manager.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Properties;

//T - base, S - dependent
public interface BaseService<B> {
    B addObject(B t);
    Page<B> getAllObjects(Pageable pageable);
    B findObjectById(Long id);
    B updateObjectById(Long id, Properties patch);
    B updateObject(B patchedObject, Properties patch);
    void deleteObjectById(Long id);
    void deleteAllObjects();
}
