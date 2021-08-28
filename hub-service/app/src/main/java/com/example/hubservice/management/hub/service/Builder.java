package com.example.hubservice.management.hub.service;

public interface Builder<T, K> {
    T newObject(K id); //Builder / Abstract Factory pattern
}
