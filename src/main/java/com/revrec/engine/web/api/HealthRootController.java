package com.revrec.engine.web.api;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthRootController {

    @GetMapping("/")
    public Map<String, String> root() {
        return Map.of("service", "rev-rec-service", "status", "ok");
    }
}
