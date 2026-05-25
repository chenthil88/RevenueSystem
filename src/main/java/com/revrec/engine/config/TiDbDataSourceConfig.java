package com.revrec.engine.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Single TiDB {@link DataSource} (MySQL protocol) shared application-wide via this bean and the
 * {@link NamedParameterJdbcTemplate} / {@link PlatformTransactionManager} registered here.
 */
@Configuration
public class TiDbDataSourceConfig {

    public static final String BEAN_NAME = "tidbDataSource";

    @Bean(name = BEAN_NAME)
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.tidb")
    public DataSource tidbDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    @Primary
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(
            @Qualifier(BEAN_NAME) DataSource tidbDataSource) {
        return new NamedParameterJdbcTemplate(tidbDataSource);
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(@Qualifier(BEAN_NAME) DataSource tidbDataSource) {
        return new DataSourceTransactionManager(tidbDataSource);
    }
}
