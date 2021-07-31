package com.example.backend.device.manager.service.interfaces;

public interface MasterTypeInterface<B, D> extends BaseTypeInterface<B> {
    B addDependentToDependentsList(D dependent);
    boolean removeDependentFromDependentsList(D dependent);
}
