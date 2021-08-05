package com.example.backend.device.manager.model.interfaces;

public interface MasterTypeInterface<B, D> extends BaseTypeInterface<B> {
    B addDependentToDependentsList(D dependent);
    boolean removeDependentFromDependentsList(D dependent);
}
