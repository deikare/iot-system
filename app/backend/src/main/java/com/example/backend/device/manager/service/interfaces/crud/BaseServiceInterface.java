package com.example.backend.device.manager.service.interfaces.crud;

import java.util.List;
import java.util.Properties;

public interface BaseServiceInterface<B> {
    B addObject(B t);
    List<B> getAllObjects();
    B findObjectById(Long id);
    B updateObjectById(Long id, Properties patch);
    B updateObject(B patchedObject, Properties patch);
    void deleteObjectById(Long id);
    void deleteAllObjects();
}
