package com.revrec.engine.integration.nosql;

import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/** No-op implementation when Redis row cache is disabled. */
@Component
@ConditionalOnProperty(prefix = "revrec.nosql", name = "enabled", havingValue = "false")
public class NoOpNoSqlRecordServer implements NoSqlRecordServer {

    @Override
    public <T> Optional<T> get(String tableName, String id, Class<T> type) {
        return Optional.empty();
    }

    @Override
    public void put(String tableName, String id, Object record) {
        // intentionally empty
    }

    @Override
    public void evict(String tableName, String id) {
        // intentionally empty
    }
}
