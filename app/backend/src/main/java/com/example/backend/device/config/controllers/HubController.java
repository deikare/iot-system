package com.example.backend.device.config.controllers;

import com.example.backend.device.config.model.Hub;
import com.example.backend.device.config.repositories.HubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HubController {
    private final HubRepository hubRepository;

    public HubController(HubRepository hubRepository) {
        this.hubRepository = hubRepository;
    }

    @GetMapping("/hubs")
    List<Hub> all() {
        return null;
    }
}
