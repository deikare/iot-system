package com.example.backend.device.manager.model.interfaces.crud;

public interface BaseTypeInterface<B> {
    B update(B patch);

    Long getId();
    void setId(Long id);
}
