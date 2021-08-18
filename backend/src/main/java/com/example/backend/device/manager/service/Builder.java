package com.example.backend.device.manager.service;

public interface Builder<T, K> {
    T newObject(K id); //Builder / Abstract Factory pattern
}
