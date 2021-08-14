package com.example.backend.device.manager.service;

public interface Builder<T> {
    T newObject(Long id); //Builder / Abstract Factory pattern
}
