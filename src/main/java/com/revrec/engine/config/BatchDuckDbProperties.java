package com.revrec.engine.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "revrec.batch.duckdb")
public class BatchDuckDbProperties {

    /**
     * Example: {@code jdbc:duckdb::memory:revrec_batch} or {@code jdbc:duckdb:/data/batch.duckdb}
     */
    private String jdbcUrl = "jdbc:duckdb::memory:revrec_batch";

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }
}
