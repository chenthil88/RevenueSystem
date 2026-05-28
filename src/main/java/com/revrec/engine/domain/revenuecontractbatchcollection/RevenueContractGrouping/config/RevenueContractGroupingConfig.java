package com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.config;

import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.RevenueContractGroupingConstants;
import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.reference.DatabaseReferenceLookupStrategy;
import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.reference.RevenueContractReferenceLookupStrategy;
import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.reference.RevenueContractReferenceMapper;
import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.reference.RevenueContractReferenceSqlBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class RevenueContractGroupingConfig {

    @Bean
    public RevenueContractReferenceSqlBuilder revenueContractReferenceSqlBuilder() {
        return new RevenueContractReferenceSqlBuilder();
    }

    @Bean
    public RevenueContractReferenceMapper revenueContractReferenceMapper() {
        return new RevenueContractReferenceMapper();
    }

    @Bean
    public RevenueContractReferenceLookupStrategy revenueContractReferenceLookupStrategy(
            NamedParameterJdbcTemplate jdbc,
            RevenueContractReferenceSqlBuilder sqlBuilder,
            RevenueContractReferenceMapper mapper) {
        return new DatabaseReferenceLookupStrategy(jdbc, sqlBuilder, mapper);
    }

    @Bean
    public BatchProcessingConfig batchProcessingConfig() {
        return new BatchProcessingConfig(
                RevenueContractGroupingConstants.DEFAULT_BATCH_SIZE,
                RevenueContractGroupingConstants.DEFAULT_FETCH_SIZE,
                RevenueContractGroupingConstants.DEFAULT_THREAD_POOL_SIZE);
    }
}
