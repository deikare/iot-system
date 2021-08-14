package com.example.hubservice.device.manager.model;

import com.example.backend.device.manager.kafka.record.interfaces.KafkaRecordInterface;
import com.example.backend.device.manager.model.interfaces.crud.MasterAndDependentTypeInterface;
import com.example.backend.device.manager.model.listeners.implementations.ControlSignalEntityListener;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ControlSignal implements MasterAndDependentTypeInterface<ControlSignal, com.example.backend.device.manager.model.ControlSignalResponse, com.example.backend.device.manager.model.Device>, KafkaRecordInterface<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CONTROL_SIGNAL_ID")
    private Long id;

    private String name;

    private String messageContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEVICE_ID")
    private com.example.backend.device.manager.model.Device device;

    @JsonIgnore
    @OneToMany(mappedBy = "sentControlSignal", cascade = CascadeType.ALL)
    private final List<com.example.backend.device.manager.model.ControlSignalResponse> responses = new ArrayList<>();

    public ControlSignal(String name, String messageContent, com.example.backend.device.manager.model.Device device) {
        this.name = name;
        this.messageContent = messageContent;
        this.device = device;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public com.example.backend.device.manager.model.Device getDevice() {
        return device;
    }

    public void setDevice(com.example.backend.device.manager.model.Device device) {
        this.device = device;
    }

    public List<com.example.backend.device.manager.model.ControlSignalResponse> getResponses() {
        return responses;
    }

    public ControlSignal() {

    }

    @Override
    public ControlSignal update(ControlSignal patch) {
        if (patch != null) {
            updateName(patch);
            updateMessageContent(patch);
            updateDevice(patch);
        }
        return this;
    }

    private void updateName(ControlSignal patch) {
        String newName = String.valueOf(patch.getName());
        if (newName != null && !newName.isEmpty())
            setName(newName);
    }

    private void updateMessageContent(ControlSignal patch) {
        String newMessageContent = String.valueOf(patch.getMessageContent());
        if (newMessageContent != null && !newMessageContent.isEmpty())
            setName(newMessageContent);
    }

    private void updateDevice(ControlSignal patch) {
        com.example.backend.device.manager.model.Device newDevice = patch.getDevice();
        if (newDevice != null)
            setDevice(newDevice);
    }

    @Override
    public ControlSignal addDependentToDependentsList(com.example.backend.device.manager.model.ControlSignalResponse dependent) {
        responses.add(dependent);
        return this;
    }

    @Override
    public boolean removeDependentFromDependentsList(com.example.backend.device.manager.model.ControlSignalResponse dependent) {
        return responses.remove(dependent);
    }
}
