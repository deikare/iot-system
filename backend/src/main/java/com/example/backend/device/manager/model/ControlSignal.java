package com.example.backend.device.manager.model;

import com.example.backend.device.manager.kafka.record.interfaces.KafkaRecordInterface;
import com.example.backend.device.manager.model.interfaces.crud.MasterAndDependentTypeInterface;
import com.example.backend.device.manager.model.listeners.implementations.ControlSignalEntityListener;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@EntityListeners(ControlSignalEntityListener.class)
@Entity
public class ControlSignal implements MasterAndDependentTypeInterface<ControlSignal, ControlSignalResponse, Device>, KafkaRecordInterface<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CONTROL_SIGNAL_ID")
    private Long id;

    private String name;

    private String messageContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEVICE_ID")
    private Device device;

    @JsonIgnore
    @OneToMany(mappedBy = "sentControlSignal", cascade = CascadeType.ALL)
    private final List<ControlSignalResponse> responses = new ArrayList<>();

    public ControlSignal(String name, String messageContent, Device device) {
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

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public List<ControlSignalResponse> getResponses() {
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
        Device newDevice = patch.getDevice();
        if (newDevice != null)
            setDevice(newDevice);
    }

    @Override
    public ControlSignal addDependentToDependentsList(ControlSignalResponse dependent) {
        responses.add(dependent);
        return this;
    }

    @Override
    public boolean removeDependentFromDependentsList(ControlSignalResponse dependent) {
        return responses.remove(dependent);
    }
}
