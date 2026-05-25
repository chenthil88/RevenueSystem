package com.revrec.engine.query.metadata;

import java.util.List;

/**
 * Table definition aligned with {@code db_script.sql}.
 */
public record TableMetadata(String tableName, List<ColumnMetadata> columns) {

    public TableMetadata {
        columns = List.copyOf(columns);
    }

    public static TableMetadata of(String tableName, String... columnNames) {
        return new TableMetadata(
                tableName,
                java.util.Arrays.stream(columnNames).map(ColumnMetadata::new).toList());
    }
}
