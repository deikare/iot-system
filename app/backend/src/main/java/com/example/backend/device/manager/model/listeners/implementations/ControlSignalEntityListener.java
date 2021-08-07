package com.example.backend.device.manager.model.listeners.implementations;

import com.example.backend.device.manager.model.ControlSignal;
import com.example.backend.device.manager.model.listeners.generic.implementations.EntityListenerImplementation;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class ControlSignalEntityListener {
    @Autowired
    private EntityListenerImplementation<Long, ControlSignal> controlSignalListenerImplementation;

    @PostPersist
    public void postPersist(ControlSignal controlSignal) {
        controlSignalListenerImplementation.postPersist(controlSignal);
    }

    @PostUpdate
    public void postUpdate(ControlSignal controlSignal) {
        controlSignalListenerImplementation.postUpdate(controlSignal);
    }

    @PostRemove
    public void postRemove(ControlSignal controlSignal) {
        controlSignalListenerImplementation.postRemove(controlSignal);
    }
}
