package com.revrec.engine.domain.revenuecontractbatchcollection.revenuecontractgrouping;

/**
 * Shared constants for the Revenue Contract Grouping batch collection module.
 */
public final class RevenueContractGroupingConstants {

    public static final String DEFAULT_GROUPING_VALUE = "GROUPING_DEFAULT_VALUE";
    public static final String GROUPING_FIELDS_SEPARATOR = ":";
    public static final String GROUPING_VALUE_SEPARATOR = ":";

    public static final String REV_REC_STAGE_TABLE = "RevRecStage";
    public static final String REV_REC_STAGE_ALIAS = "stg";
    public static final String REVENUE_CONTRACT_REFERENCE_TABLE = "revenueContractReferenceDetails";

    public static final String PROCESSED_FLAG_NEW = "N";
    public static final String PROCESSED_FLAG_ERROR = "E";
    public static final String PROCESSED_FLAG_YES = "Y";

    public static final int DEFAULT_BATCH_SIZE = 1000;
    public static final int DEFAULT_FETCH_SIZE = 5000;
    public static final int DEFAULT_THREAD_POOL_SIZE = 4;
    public static final int DEFAULT_MAX_RETRIES = 3;
    public static final long RETRY_WAIT_MS = 100L;
    public static final int EXECUTOR_SHUTDOWN_TIMEOUT_SECONDS = 60;

    public static final String REV_REC_STAGE_COLUMNS_RESOURCE = "metadata/rev-rec-stage-columns.txt";

    public static final String REFERENCE_LOOKUP_KEY_COLUMN = "key";
    public static final String REFERENCE_LOOKUP_VALUE_COLUMN = "RevenueContractId";
    public static final String DATABASE_REFERENCE_LOOKUP_STRATEGY = "DatabaseReferenceLookup";

    private RevenueContractGroupingConstants() {}
}
