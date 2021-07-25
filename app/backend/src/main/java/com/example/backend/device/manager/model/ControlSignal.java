package com.example.backend.device.manager.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ControlSignal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CONTROL_SIGNAL_ID")
    private Long id;

    private String name;

    private String messageContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEVICE_ID")
    private Device device;

    @OneToMany(mappedBy = "sentControlSignal", cascade = CascadeType.ALL)
    private final List<ControlSignalResponse> responses = new ArrayList<>();

    public ControlSignal(String name, String messageContent, Device device) {
        this.name = name;
        this.messageContent = messageContent;
        this.device = device;
    }


    public ControlSignal() {

    }
}
