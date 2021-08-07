package com.example.backend.device.manager.model;

import com.example.backend.device.manager.kafka.record.interfaces.KafkaRecordInterface;
import com.example.backend.device.manager.model.interfaces.crud.MasterAndDependentTypeInterface;
import com.example.backend.device.manager.model.properties.ControlSignalProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
    public ControlSignal update(Properties patch) {
        updateName(patch);
        updateMessageContent(patch);
        updateDevice(patch);
        return this;
    }

    private void updateName(Properties patch) {
        String newName = String.valueOf(patch.get(ControlSignalProperties.NAME));
        if (!newName.isEmpty())
            setName(newName);
    }

    private void updateMessageContent(Properties patch) {
        String newMessageContent = String.valueOf(patch.get(ControlSignalProperties.MESSAGE));
        if (!newMessageContent.isEmpty())
            setName(newMessageContent);
    }

    private void updateDevice(Properties patch) {
        Device newDevice = (Device) patch.get(ControlSignalProperties.DEVICE);
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
