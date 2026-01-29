package com.mindoasis.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class HealthController {

    @GetMapping("/")
    public Map<String, String> rootCheck() {
        return Map.of("status", "Mind Oasis Backend is Running at Root");
    }

    @GetMapping("/api/health")
    public Map<String, String> healthCheck() {
        return Map.of("status", "Health OK");
    }
}
