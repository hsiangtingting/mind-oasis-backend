package com.mindoasis.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")

public class HealthController{

    @GetMapping("/health")
    public Map<String, String> healthCheck() {
        return Map.of("status", "Mind Oasis Backend is Running");

    }
}
