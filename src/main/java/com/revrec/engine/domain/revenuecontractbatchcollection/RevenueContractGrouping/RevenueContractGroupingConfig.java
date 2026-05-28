package com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * Spring configuration for revenue contract reference resolution
 * Sets up beans and wiring for the strategy pattern implementation
 */
@Configuration
public class RevenueContractGroupingConfig {

    /**
     * Provide SQL builder as a bean
     */
    @Bean
    public RevenueContractReferenceSqlBuilder revenueContractReferenceSqlBuilder() {
        return new RevenueContractReferenceSqlBuilder();
    }

    /**
     * Provide mapper as a bean
     */
    @Bean
    public RevenueContractReferenceMapper revenueContractReferenceMapper() {
        return new RevenueContractReferenceMapper();
    }

    /**
     * Provide database lookup strategy as the primary implementation
     */
    @Bean
    public RevenueContractReferenceLookupStrategy revenueContractReferenceLookupStrategy(
            NamedParameterJdbcTemplate jdbc,
            RevenueContractReferenceSqlBuilder sqlBuilder,
            RevenueContractReferenceMapper mapper) {
        return new DatabaseReferenceLookupStrategy(jdbc, sqlBuilder, mapper);
    }

    /**
     * Provide batch processing config with sensible defaults
     * Can be overridden via application.properties or constructor
     */
    @Bean
    public BatchProcessingConfig batchProcessingConfig() {
        return new BatchProcessingConfig(
                1000,  // batchSize
                5000,  // fetchSize
                4      // threadPoolSize
        );
    }
}
