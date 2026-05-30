package com.revrec.engine.cache.metadata;

import java.util.Optional;

/**
 * Cache for metadata records with tenant isolation.
 * Key format: revrec:metadata:{tableName}:{tenantId}:{id}
 */
public interface MetadataCache {

    <T> Optional<T> get(String tableName, String tenantId, String id, Class<T> type);

    void put(String tableName, String tenantId, String id, Object record);

    void evict(String tableName, String tenantId, String id);

    int getRecordCount();

    void clear(String tenantId);
}
