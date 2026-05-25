package com.revrec.engine.query.core;

/**
 * Routes queries to the correct {@link javax.sql.DataSource} / JDBC template.
 */
public enum DataSourceKey {
    /** Primary OLTP store (TiDB / MySQL protocol). */
    PRIMARY_OLTP,
    /** In-process batch working set (DuckDB). */
    BATCH_TEMP
}
