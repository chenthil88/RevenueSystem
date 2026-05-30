package com.revrec.engine.cache.metadata;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Redis-backed metadata cache with tenant isolation.
 * Stores serialized metadata records with TTL for fast access.
 */
@Component
@ConditionalOnProperty(prefix = "revrec.nosql", name = "enabled", havingValue = "true", matchIfMissing = true)
public class RedisMetadataCache implements MetadataCache {

    private static final Logger log = LoggerFactory.getLogger(RedisMetadataCache.class);
    private static final String KEY_PREFIX = "revrec:metadata:";

    private final StringRedisTemplate redis;
    private final ObjectMapper objectMapper;
    private final Duration ttl;
    private volatile int recordCount = 0;

    public RedisMetadataCache(
            StringRedisTemplate redis,
            ObjectMapper objectMapper,
            @Value("${revrec.nosql.row-ttl-seconds:900}") long ttlSeconds) {
        this.redis = redis;
        this.objectMapper = objectMapper;
        this.ttl = Duration.ofSeconds(ttlSeconds);
    }

    static String cacheKey(String tableName, String tenantId, String id) {
        return KEY_PREFIX + tableName + ":" + tenantId + ":" + id;
    }

    @Override
    public <T> Optional<T> get(String tableName, String tenantId, String id, Class<T> type) {
        String json = redis.opsForValue().get(cacheKey(tableName, tenantId, id));
        if (json == null || json.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(objectMapper.readValue(json, type));
        } catch (Exception e) {
            log.warn("Failed to deserialize {}:{}:{}", tableName, tenantId, id, e);
            return Optional.empty();
        }
    }

    @Override
    public void put(String tableName, String tenantId, String id, Object record) {
        try {
            String json = objectMapper.writeValueAsString(record);
            redis.opsForValue().set(cacheKey(tableName, tenantId, id), json, ttl);
            recordCount++;
        } catch (Exception e) {
            log.warn("Failed to serialize {}:{}:{}", tableName, tenantId, id, e);
        }
    }

    @Override
    public void evict(String tableName, String tenantId, String id) {
        redis.unlink(cacheKey(tableName, tenantId, id));
        recordCount--;
    }

    @Override
    public int getRecordCount() {
        return recordCount;
    }

    @Override
    public void clear(String tenantId) {
        String pattern = KEY_PREFIX + "*:" + tenantId + ":*";
        var keys = redis.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redis.unlink(keys);
            recordCount -= keys.size();
        }
    }
}
