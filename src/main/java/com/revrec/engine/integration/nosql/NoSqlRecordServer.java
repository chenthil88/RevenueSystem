package com.revrec.engine.integration.nosql;

import java.util.Optional;

/**
 * Redis-backed materialization of TiDB row snapshots (JSON per primary key).
 */
public interface NoSqlRecordServer {

    <T> Optional<T> get(String tableName, String id, Class<T> type);

    void put(String tableName, String id, Object record);

    void evict(String tableName, String id);
}
