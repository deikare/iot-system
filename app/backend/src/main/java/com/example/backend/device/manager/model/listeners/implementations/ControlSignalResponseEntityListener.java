package com.example.backend.device.manager.model.listeners.implementations;

import com.example.backend.device.manager.model.ControlSignalResponse;
import com.example.backend.device.manager.model.listeners.generic.implementations.EntityListenerImplementation;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

public class ControlSignalResponseEntityListener {
    @Autowired
    private EntityListenerImplementation<Long, ControlSignalResponse> controlSignalResponseListenerImplementation;

    @PostPersist
    public void postPersist(ControlSignalResponse controlSignalResponse) {
        controlSignalResponseListenerImplementation.postPersist(controlSignalResponse);
    }

    @PostUpdate
    public void postUpdate(ControlSignalResponse controlSignalResponse) {
        controlSignalResponseListenerImplementation.postUpdate(controlSignalResponse);
    }

    @PostRemove
    public void postRemove(ControlSignalResponse controlSignalResponse) {
        controlSignalResponseListenerImplementation.postRemove(controlSignalResponse);
    }
}
