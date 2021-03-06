package com.example.hubservice.management.hub.service.interfaces.crud;

import java.util.List;

public interface BaseServiceInterface<B, K> {
    B addObject(B t);
    List<B> getAllObjects();
    long count();
    B findObjectById(K id);
    boolean isPresent(K id);
    B updateObjectById(K id, B patch);
    void deleteObjectById(K id);
    B deleteObjectByIdAndReturnDeletedObject(K id);
    void deleteAllObjects();
    List<B> deleteAllObjectsAndReturnThemListed();
}
