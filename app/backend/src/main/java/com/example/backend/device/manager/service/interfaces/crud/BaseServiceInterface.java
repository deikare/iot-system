package com.example.backend.device.manager.service.interfaces.crud;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Properties;

//T - base, S - dependent
public interface BaseServiceInterface<B> {
    B addObject(B t);
    List<B> getAllObjects();
    B findObjectById(Long id);
    B updateObjectById(Long id, Properties patch);
    B updateObject(B patchedObject, Properties patch);
    void deleteObjectById(Long id);
    void deleteAllObjects();
}
