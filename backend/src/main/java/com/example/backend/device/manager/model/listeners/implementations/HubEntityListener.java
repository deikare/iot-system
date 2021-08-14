package com.example.backend.device.manager.model.listeners.implementations;

import com.example.backend.device.manager.model.Hub;
import com.example.backend.device.manager.model.listeners.generic.implementations.EntityListenerImplementation;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class HubEntityListener {
    @Autowired
    private EntityListenerImplementation<Long, Hub> hubListener;

    @PostPersist
    public void postPersist(Hub hub) {
        hubListener.postPersist(hub);
    }

    @PostUpdate
    public void postUpdate(Hub hub) {
        hubListener.postUpdate(hub);
    }

    @PostRemove
    public void postRemove(Hub hub) {
        hubListener.postRemove(hub);
    }
}
