package com.example.backend.device.manager.model.listeners.generic.interfaces;

public interface EntityListenerInterface <V>{
    public void postPersist(V object);
    public void postUpdate(V object);
    public void postRemove(V object);
}
