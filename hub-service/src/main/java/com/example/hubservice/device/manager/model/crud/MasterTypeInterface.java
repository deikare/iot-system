package com.example.hubservice.device.manager.model.crud;

public interface MasterTypeInterface<B, D> extends BaseTypeInterface<B> {
    B addDependentToDependentsList(D dependent);
    boolean removeDependentFromDependentsList(D dependent);
}
