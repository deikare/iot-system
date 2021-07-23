package com.example.backend.device.config.model;

import javax.persistence.*;

@Entity
public class ControlSignalResponse {
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
}
