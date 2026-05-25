package com.revrec.engine.query;

import com.revrec.engine.query.builder.QueryBuilder;
import com.revrec.engine.query.core.DataSourceKey;
import com.revrec.engine.query.executor.QueryExecutor;
import com.revrec.engine.query.router.ConnectionRouter;
import com.revrec.engine.query.spec.CompiledQuery;
import com.revrec.engine.query.spec.QuerySpec;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

/**
 * Facade for dialect-aware query compilation and routed JDBC execution.
 */
@Service
public final class QueryEngineService {

    private final QueryBuilder queryBuilder;
    private final QueryExecutor queryExecutor;
    private final ConnectionRouter connectionRouter;

    public QueryEngineService(
            QueryBuilder queryBuilder, QueryExecutor queryExecutor, ConnectionRouter connectionRouter) {
        this.queryBuilder = queryBuilder;
        this.queryExecutor = queryExecutor;
        this.connectionRouter = connectionRouter;
    }

    public CompiledQuery compile(QuerySpec spec, DataSourceKey key) {
        return queryBuilder.compile(spec, connectionRouter.profile(key));
    }

    public <T> List<T> query(QuerySpec spec, DataSourceKey key, RowMapper<T> rowMapper) {
        CompiledQuery compiled = compile(spec, key);
        return queryExecutor.query(key, compiled, rowMapper);
    }

    public <T> Optional<T> queryOne(QuerySpec spec, DataSourceKey key, RowMapper<T> rowMapper) {
        CompiledQuery compiled = compile(spec, key);
        return queryExecutor.queryOne(key, compiled, rowMapper);
    }

    public int update(CompiledQuery query, DataSourceKey key) {
        return queryExecutor.update(key, query);
    }
}
