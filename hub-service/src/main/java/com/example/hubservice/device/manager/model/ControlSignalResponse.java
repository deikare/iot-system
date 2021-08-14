package com.example.hubservice.device.manager.model;

import com.example.backend.device.manager.kafka.record.interfaces.KafkaRecordInterface;
import com.example.backend.device.manager.model.interfaces.crud.DependentTypeInterface;
import com.example.backend.device.manager.model.listeners.implementations.ControlSignalResponseEntityListener;

import javax.persistence.*;

@Entity
public class ControlSignalResponse implements DependentTypeInterface<ControlSignalResponse, ControlSignal>, KafkaRecordInterface<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CONTROL_SIGNAL_RESPONSE_ID")
    private Long id;

    private String name;

    private String messageContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTROL_SIGNAL_ID")
    private ControlSignal sentControlSignal;

    public ControlSignalResponse(String name, String messageContent, ControlSignal sentControlSignal) {
        this.name = name;
        this.messageContent = messageContent;
        this.sentControlSignal = sentControlSignal;
    }

    public ControlSignalResponse() {

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

    public ControlSignal getSentControlSignal() {
        return sentControlSignal;
    }

    public void setSentControlSignal(ControlSignal sentControlSignal) {
        this.sentControlSignal = sentControlSignal;
    }

    @Override
    public ControlSignalResponse update(ControlSignalResponse patch) {
        if (patch != null) {
            updateName(patch);
            updateMessageContent(patch);
            updateControlSignal(patch);
        }
        return this;
    }

    private void updateName(ControlSignalResponse patch) {
        String newName = String.valueOf(patch.getName());
        if (newName != null && !newName.isEmpty())
            setName(newName);
    }

    private void updateMessageContent(ControlSignalResponse patch) {
        String newMessageContent = String.valueOf(patch.getMessageContent());
        if (newMessageContent != null && !newMessageContent.isEmpty())
            setName(newMessageContent);
    }

    private void updateControlSignal(ControlSignalResponse patch) {
        ControlSignal newControlSignal = patch.getSentControlSignal();
        if (newControlSignal != null)
            setSentControlSignal(newControlSignal);
    }
}