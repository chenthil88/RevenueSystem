package com.revrec.engine.cache.metadata;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API for on-demand metadata cache loading.
 */
@RestController
@RequestMapping("/api/metadata")
public class MetadataLoadController {

    private final MetadataLoadService metadataLoadService;

    public MetadataLoadController(MetadataLoadService metadataLoadService) {
        this.metadataLoadService = metadataLoadService;
    }

    /**
     * Trigger metadata load for current tenant.
     * TenantId should be provided via X-Tenant-Id header.
     *
     * @param tenantId tenant identifier from header
     * @return load result with count and duration
     */
    @PostMapping("/load-cache")
    public ResponseEntity<Map<String, Object>> loadCache(@RequestHeader("X-Tenant-Id") String tenantId) {
        TenantContext.setTenantId(tenantId);
        try {
            var result = metadataLoadService.loadMetadata();
            Map<String, Object> response = new HashMap<>();
            response.put("cached", result.cached);
            response.put("duration", result.durationMs + "ms");
            response.put("tenantId", result.tenantId);
            return ResponseEntity.ok(response);
        } finally {
            TenantContext.clear();
        }
    }
}
