package com.example.backend.device.manager.model.interfaces.crud;

public interface DependentTypeInterface<B, M> extends BaseTypeInterface<B> {
    void setMaster(M master);
}
