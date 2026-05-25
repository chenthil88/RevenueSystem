package com.revrec.engine.query.executor;

import com.revrec.engine.query.core.DataSourceKey;
import com.revrec.engine.query.router.ConnectionRouter;
import com.revrec.engine.query.spec.CompiledQuery;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public final class QueryExecutor {

    private final ConnectionRouter connectionRouter;

    public QueryExecutor(ConnectionRouter connectionRouter) {
        this.connectionRouter = connectionRouter;
    }

    public <T> List<T> query(DataSourceKey key, CompiledQuery query, RowMapper<T> rowMapper) {
        return connectionRouter.jdbc(key).query(query.sql(), query.parameters(), rowMapper);
    }

    public <T> Optional<T> queryOne(DataSourceKey key, CompiledQuery query, RowMapper<T> rowMapper) {
        List<T> rows = query(key, query, rowMapper);
        if (rows.isEmpty()) {
            return Optional.empty();
        }
        if (rows.size() > 1) {
            throw new IllegalStateException("Expected at most one row, got " + rows.size());
        }
        return Optional.of(rows.getFirst());
    }

    public int update(DataSourceKey key, CompiledQuery query) {
        return connectionRouter.jdbc(key).update(query.sql(), query.parameters());
    }
}
