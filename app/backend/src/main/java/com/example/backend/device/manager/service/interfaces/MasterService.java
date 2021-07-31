package com.example.backend.device.manager.service.interfaces;

public interface MasterService<B, D> {
    B addDependentToListInObjectById(Long objectId, D dependent);
    B addDependentToListInObject(B object, D dependent);
    boolean deleteDependentFromListInObjectById(Long objectId, D dependent);
    boolean deleteDependentFromListInObject(B object, D dependent);
}
