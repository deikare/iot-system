package com.example.hubservice.management.hub.model.crud;

public interface BaseTypeInterface<B> {
    B update(B patch);
    B deepCopy();
}
