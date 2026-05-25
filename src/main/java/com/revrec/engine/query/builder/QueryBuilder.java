package com.revrec.engine.query.builder;

import com.revrec.engine.query.core.DatabaseProfile;
import com.revrec.engine.query.dialect.DialectRegistry;
import com.revrec.engine.query.dialect.SqlDialect;
import com.revrec.engine.query.metadata.ColumnMetadata;
import com.revrec.engine.query.metadata.TableMetadata;
import com.revrec.engine.query.spec.CompiledQuery;
import com.revrec.engine.query.spec.OrderBy;
import com.revrec.engine.query.spec.QueryPredicate;
import com.revrec.engine.query.spec.QuerySpec;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public final class QueryBuilder {

    private static final String LIMIT_PARAM = "qe_limit";
    private static final String OFFSET_PARAM = "qe_offset";

    private final DialectRegistry dialectRegistry;

    public QueryBuilder(DialectRegistry dialectRegistry) {
        this.dialectRegistry = dialectRegistry;
    }

    public CompiledQuery compile(QuerySpec spec, DatabaseProfile profile) {
        SqlDialect dialect = dialectRegistry.require(profile);
        TableMetadata table = spec.table();
        List<String> columns = resolveSelectColumns(spec, table);

        var sql = new StringBuilder("SELECT ");
        appendColumnList(sql, dialect, columns);
        sql.append(" FROM ").append(dialect.quoteIdentifier(table.tableName()));

        Map<String, Object> params = new LinkedHashMap<>();
        if (!spec.predicates().isEmpty()) {
            sql.append(" WHERE ");
            appendPredicates(sql, dialect, spec.predicates(), params);
        }
        if (!spec.orderBy().isEmpty()) {
            sql.append(" ORDER BY ");
            appendOrderBy(sql, dialect, spec.orderBy());
        }
        if (spec.limit().isPresent()) {
            int offset = spec.offset().orElse(0);
            params.put(LIMIT_PARAM, spec.limit().get());
            params.put(OFFSET_PARAM, offset);
            sql.append(dialect.limitOffsetClause(LIMIT_PARAM, OFFSET_PARAM));
        }

        return new CompiledQuery(sql.toString(), Map.copyOf(params));
    }

    private static List<String> resolveSelectColumns(QuerySpec spec, TableMetadata table) {
        if (!spec.selectColumns().isEmpty()) {
            return spec.selectColumns();
        }
        return table.columns().stream().map(ColumnMetadata::name).toList();
    }

    private static void appendColumnList(StringBuilder sql, SqlDialect dialect, List<String> columns) {
        for (int i = 0; i < columns.size(); i++) {
            if (i > 0) {
                sql.append(", ");
            }
            sql.append(dialect.quoteIdentifier(columns.get(i)));
        }
    }

    private static void appendPredicates(
            StringBuilder sql,
            SqlDialect dialect,
            List<QueryPredicate> predicates,
            Map<String, Object> params) {
        for (int i = 0; i < predicates.size(); i++) {
            if (i > 0) {
                sql.append(" AND ");
            }
            QueryPredicate predicate = predicates.get(i);
            sql.append(dialect.quoteIdentifier(predicate.column()))
                    .append(" = :")
                    .append(predicate.paramName());
            params.put(predicate.paramName(), predicate.value());
        }
    }

    private static void appendOrderBy(StringBuilder sql, SqlDialect dialect, List<OrderBy> orderBy) {
        for (int i = 0; i < orderBy.size(); i++) {
            if (i > 0) {
                sql.append(", ");
            }
            OrderBy clause = orderBy.get(i);
            sql.append(dialect.quoteIdentifier(clause.column()));
            sql.append(clause.ascending() ? " ASC" : " DESC");
        }
    }
}
