package com.example.backend.device.manager.service.interfaces;

public interface MasterServiceInterface<B, D> extends BaseServiceInterface<B> {
    B addDependentToListInObjectById(Long objectId, D dependent);
    B addDependentToListInObject(B object, D dependent);
    boolean deleteDependentFromListInObjectById(Long objectId, D dependent);
    boolean deleteDependentFromListInObject(B object, D dependent);
}
