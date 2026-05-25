package com.revrec.engine.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * In-process DuckDB for temporary batch working sets. Inject
 * {@code @Qualifier(BatchDuckDbConfig.BEAN_NAME)} or {@code batchDuckNamedJdbcTemplate} explicitly.
 */
@Configuration
@EnableConfigurationProperties(BatchDuckDbProperties.class)
public class BatchDuckDbConfig {

    public static final String BEAN_NAME = "batchDuckDataSource";

    @Bean(name = BEAN_NAME)
    public DataSource batchDuckDataSource(BatchDuckDbProperties properties) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(properties.getJdbcUrl());
        ds.setDriverClassName("org.duckdb.DuckDBDriver");
        ds.setPoolName("duckdb-batch-pool");
        ds.setMaximumPoolSize(1);
        return ds;
    }

    @Bean
    public NamedParameterJdbcTemplate batchDuckNamedJdbcTemplate(
            @Qualifier(BEAN_NAME) DataSource batchDuckDataSource) {
        return new NamedParameterJdbcTemplate(batchDuckDataSource);
    }
}
