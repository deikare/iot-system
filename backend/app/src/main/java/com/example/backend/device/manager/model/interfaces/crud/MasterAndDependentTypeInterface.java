package com.example.backend.device.manager.model.interfaces.crud;

public interface MasterAndDependentTypeInterface<B, D, M> extends MasterTypeInterface<B, D>, DependentTypeInterface<B, M> {
}
