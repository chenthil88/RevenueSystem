package com.revrec.engine.integration.nosql;

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
 * Stores serialized table rows in Redis for fast reads ({@code findByIdCached}).
 */
@Component
@ConditionalOnProperty(prefix = "revrec.nosql", name = "enabled", havingValue = "true", matchIfMissing = true)
public class RedisNoSqlRecordServer implements NoSqlRecordServer {

    private static final Logger log = LoggerFactory.getLogger(RedisNoSqlRecordServer.class);

    private final StringRedisTemplate redis;
    private final ObjectMapper objectMapper;
    private final Duration ttl;

    public RedisNoSqlRecordServer(
            StringRedisTemplate redis,
            ObjectMapper objectMapper,
            @Value("${revrec.nosql.row-ttl-seconds:900}") long ttlSeconds) {
        this.redis = redis;
        this.objectMapper = objectMapper;
        this.ttl = Duration.ofSeconds(ttlSeconds);
    }

    static String cacheKey(String tableName, String id) {
        return "revrec:row:" + tableName + ":" + id;
    }

    @Override
    public <T> Optional<T> get(String tableName, String id, Class<T> type) {
        String json = redis.opsForValue().get(cacheKey(tableName, id));
        if (json == null || json.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(objectMapper.readValue(json, type));
        } catch (Exception e) {
            log.warn("Failed to deserialize {} id={}", tableName, id, e);
            return Optional.empty();
        }
    }

    @Override
    public void put(String tableName, String id, Object record) {
        try {
            String json = objectMapper.writeValueAsString(record);
            redis.opsForValue().set(cacheKey(tableName, id), json, ttl);
        } catch (Exception e) {
            log.warn("Failed to serialize {} id={}", tableName, id, e);
        }
    }

    @Override
    public void evict(String tableName, String id) {
        redis.unlink(cacheKey(tableName, id));
    }
}
