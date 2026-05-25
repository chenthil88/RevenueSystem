package com.revrec.engine.query.repository;

import com.revrec.engine.query.QueryEngineService;
import com.revrec.engine.query.core.DataSourceKey;
import com.revrec.engine.query.metadata.TableMetadata;
import com.revrec.engine.query.spec.QueryPredicate;
import com.revrec.engine.query.spec.QuerySpec;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;

/**
 * Base repository using {@link QueryEngineService} for dialect-safe SELECTs.
 */
public abstract class AbstractTableRepository<R, ID> implements TableRepository<R, ID> {

    private final QueryEngineService queryEngine;
    private final TableMetadata table;
    private final DataSourceKey dataSourceKey;
    private final RowMapper<R> rowMapper;
    private final String idColumn;

    protected AbstractTableRepository(
            QueryEngineService queryEngine,
            TableMetadata table,
            DataSourceKey dataSourceKey,
            RowMapper<R> rowMapper,
            String idColumn) {
        this.queryEngine = queryEngine;
        this.table = table;
        this.dataSourceKey = dataSourceKey;
        this.rowMapper = rowMapper;
        this.idColumn = idColumn;
    }

    @Override
    public Optional<R> findById(ID id) {
        QuerySpec spec = QuerySpec.forTable(table).where(new QueryPredicate.Eq(idColumn, id));
        return queryEngine.queryOne(spec, dataSourceKey, rowMapper);
    }

    @Override
    public List<R> findAll(int limit, int offset) {
        QuerySpec spec = QuerySpec.forTable(table).limit(limit).offset(offset);
        return queryEngine.query(spec, dataSourceKey, rowMapper);
    }

    protected List<R> findByColumn(String column, Object value) {
        QuerySpec spec = QuerySpec.forTable(table).where(new QueryPredicate.Eq(column, value));
        return queryEngine.query(spec, dataSourceKey, rowMapper);
    }
}
