package com.example.backend.device.manager.service.interfaces.crud;

public interface MasterServiceInterface<B, D, K> extends BaseServiceInterface<B, K> {
    B addDependentToListInObjectById(K objectId, D dependent);
    B addDependentToListInObject(B object, D dependent);
    boolean deleteDependentFromListInObjectById(K objectId, D dependent);
    boolean deleteDependentFromListInObject(B object, D dependent);
}
