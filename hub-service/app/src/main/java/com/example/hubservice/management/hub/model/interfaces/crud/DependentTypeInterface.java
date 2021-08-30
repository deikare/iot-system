package com.example.hubservice.management.hub.model.interfaces.crud;

public interface DependentTypeInterface<B, M> extends BaseTypeInterface<B> {
    void setMaster(M master);
}
