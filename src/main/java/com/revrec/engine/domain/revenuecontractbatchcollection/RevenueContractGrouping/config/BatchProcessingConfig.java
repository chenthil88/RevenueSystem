package com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.config;

import com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping.RevenueContractGroupingConstants;

public class BatchProcessingConfig {

    private int batchSize;
    private int fetchSize;
    private int threadPoolSize;

    public BatchProcessingConfig() {
        this.batchSize = RevenueContractGroupingConstants.DEFAULT_BATCH_SIZE;
        this.fetchSize = RevenueContractGroupingConstants.DEFAULT_FETCH_SIZE;
        this.threadPoolSize = RevenueContractGroupingConstants.DEFAULT_THREAD_POOL_SIZE;
    }

    public BatchProcessingConfig(int batchSize, int fetchSize, int threadPoolSize) {
        this.batchSize = batchSize;
        this.fetchSize = fetchSize;
        this.threadPoolSize = threadPoolSize;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public int getFetchSize() {
        return fetchSize;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }
}
