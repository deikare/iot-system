package com.example.backend.device.manager.service.interfaces.crud;

import java.util.List;

public interface BaseServiceInterface<B> {
    B addObject(B t);
    List<B> getAllObjects();
    B findObjectById(Long id);
    B updateObjectById(Long id, B patch);
    B updateObject(B patchedObject, B patch);
    void deleteObjectById(Long id);
    void deleteAllObjects();
}
