package com.example.hubservice.device.manager.service.interfaces.crud;

public interface MasterServiceInterface<B, D> extends BaseServiceInterface<B> {
    B addDependentToListInObjectById(Long objectId, D dependent);
    B addDependentToListInObject(B object, D dependent);
    boolean deleteDependentFromListInObjectById(Long objectId, D dependent);
    boolean deleteDependentFromListInObject(B object, D dependent);
}
