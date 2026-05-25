package com.revrec.engine.query.spec;

import com.revrec.engine.query.metadata.TableMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Database-agnostic query intent compiled by {@link com.revrec.engine.query.builder.QueryBuilder}.
 */
public final class QuerySpec {

    private final TableMetadata table;
    private final List<String> selectColumns;
    private final List<QueryPredicate> predicates;
    private final List<OrderBy> orderBy;
    private final Optional<Integer> limit;
    private final Optional<Integer> offset;

    private QuerySpec(Builder builder) {
        this.table = builder.table;
        this.selectColumns = List.copyOf(builder.selectColumns);
        this.predicates = List.copyOf(builder.predicates);
        this.orderBy = List.copyOf(builder.orderBy);
        this.limit = builder.limit;
        this.offset = builder.offset;
    }

    public TableMetadata table() {
        return table;
    }

    public List<String> selectColumns() {
        return selectColumns;
    }

    public List<QueryPredicate> predicates() {
        return predicates;
    }

    public List<OrderBy> orderBy() {
        return orderBy;
    }

    public Optional<Integer> limit() {
        return limit;
    }

    public Optional<Integer> offset() {
        return offset;
    }

    public static Builder forTable(TableMetadata table) {
        return new Builder(table);
    }

    public static final class Builder {
        private final TableMetadata table;
        private final List<String> selectColumns = new ArrayList<>();
        private final List<QueryPredicate> predicates = new ArrayList<>();
        private final List<OrderBy> orderBy = new ArrayList<>();
        private Optional<Integer> limit = Optional.empty();
        private Optional<Integer> offset = Optional.empty();

        private Builder(TableMetadata table) {
            this.table = table;
        }

        public Builder select(String... columns) {
            selectColumns.clear();
            selectColumns.addAll(List.of(columns));
            return this;
        }

        public Builder where(QueryPredicate predicate) {
            predicates.add(predicate);
            return this;
        }

        public Builder orderBy(OrderBy clause) {
            orderBy.add(clause);
            return this;
        }

        public Builder limit(int limit) {
            this.limit = Optional.of(limit);
            return this;
        }

        public Builder offset(int offset) {
            this.offset = Optional.of(offset);
            return this;
        }

        public QuerySpec build() {
            return new QuerySpec(this);
        }
    }
}
