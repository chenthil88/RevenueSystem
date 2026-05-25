package com.revrec.engine.domain.service.RevenueContractBatchDetails;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Row mapped from TiDB table `revenueContractBatchDetails`.
 */
public record RevenueContractBatchDetailsRecord(
        Long id,
        Long batchId,
        Long revenueContractId,
        Long stageRecordProcessed,
        Long revenueContractRecordProcessed,
        Boolean processedFlag,
        Long createdPeriodId,
        String createdBy,
        LocalDateTime createdAt,
        String updatedBy,
        LocalDateTime updatedAt
) implements RevenueContractBatchDetails, Serializable {
    public static final String TABLE_NAME = "revenueContractBatchDetails";
}
