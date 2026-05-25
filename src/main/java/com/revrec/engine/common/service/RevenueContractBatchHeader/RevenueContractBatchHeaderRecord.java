package com.revrec.engine.domain.service.RevenueContractBatchHeader;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Row mapped from TiDB table `revenueContractBatchHeader`.
 */
public record RevenueContractBatchHeaderRecord(
        Long batchId,
        String name,
        String description,
        Long revenueContractGroupingTemplateId,
        String status,
        Long stageRecordProcessed,
        Long revenueContractRecordProcessed,
        Long createdPeriodId,
        String createdBy,
        LocalDateTime createdAt,
        String updatedBy,
        LocalDateTime updatedAt
) implements RevenueContractBatchHeader, Serializable {
    public static final String TABLE_NAME = "revenueContractBatchHeader";
}
