package com.example.hubservice.management.hub.model.interfaces.crud;

public interface MasterTypeInterface<B, D> extends BaseTypeInterface<B> {
    B addDependentToDependentsList(D dependent);
    boolean removeDependentFromDependentsList(D dependent);
}
