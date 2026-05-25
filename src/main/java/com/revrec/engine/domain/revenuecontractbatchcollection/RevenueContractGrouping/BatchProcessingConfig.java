package com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping;

/**
 * Configuration for batch processing and streaming
 */
public class BatchProcessingConfig {
    public static final int DEFAULT_BATCH_SIZE = 1000;
    public static final int DEFAULT_FETCH_SIZE = 5000;
    public static final int DEFAULT_THREAD_POOL_SIZE = 4;

    private int batchSize;
    private int fetchSize;
    private int threadPoolSize;

    public BatchProcessingConfig() {
        this.batchSize = DEFAULT_BATCH_SIZE;
        this.fetchSize = DEFAULT_FETCH_SIZE;
        this.threadPoolSize = DEFAULT_THREAD_POOL_SIZE;
    }

    public BatchProcessingConfig(int batchSize, int fetchSize, int threadPoolSize) {
        this.batchSize = batchSize;
        this.fetchSize = fetchSize;
        this.threadPoolSize = threadPoolSize;
    }

    public int getBatchSize() { return batchSize; }
    public int getFetchSize() { return fetchSize; }
    public int getThreadPoolSize() { return threadPoolSize; }
}
