package com.example.backend.device.manager.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "DEVICE_ID")
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HUB_ID")
    private Hub hub;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
    private final List<ControlSignal> controlSignals = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;

    public Device(String name, Hub hub, DeviceType deviceType) {
        this.name = name;
        this.hub = hub;
        this.deviceType = deviceType;
    }

    public Device() {

    }
}
