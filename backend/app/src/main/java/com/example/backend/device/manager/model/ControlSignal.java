package com.example.backend.device.manager.model;

import com.example.backend.device.manager.kafka.record.interfaces.KafkaRecordInterface;
import com.example.backend.device.manager.model.interfaces.crud.DependentTypeInterface;
import com.example.backend.device.manager.model.interfaces.crud.MasterAndDependentTypeInterface;
import com.example.backend.device.manager.model.listeners.ControlSignalEntityListener;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EntityListeners(ControlSignalEntityListener.class)
@Entity
public class ControlSignal implements DependentTypeInterface<ControlSignal, Device>, KafkaRecordInterface<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "CONTROL_SIGNAL_ID")
    private Long id;

    private String name;

    private String messageContent;

    @JsonIgnore //used to avoid recursive call
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DEVICE_ID")
    private Device device;

    private ControlSignal(Long id, String name, String messageContent, Device device) {
        this.id = id;
        this.name = name;
        this.messageContent = messageContent;
        this.device = device;
    }

    public ControlSignal(String name, String messageContent, Device device) {
        this.name = name;
        this.messageContent = messageContent;
        this.device = device;
    }

    @Override
    public String toString() {
        return "ControlSignal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", messageContent='" + messageContent + '\'' +
                ", device='" + device + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof ControlSignal controlSignal))
            return false;
        return Objects.equals(this.id, controlSignal.id) && Objects.equals(this.name, controlSignal.name)
                && Objects.equals(this.messageContent, controlSignal.messageContent)
                && Objects.equals(this.device, controlSignal.device);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
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
            setMessageContent(newMessageContent);
    }

    private void updateDevice(ControlSignal patch) {
        Device newDevice = patch.getDevice();
        if (newDevice != null)
            setDevice(newDevice);
    }

    @Override
    public void setMaster(Device master) {
        setDevice(master);
        this.device.addDependentToDependentsList(this);
    }
}
