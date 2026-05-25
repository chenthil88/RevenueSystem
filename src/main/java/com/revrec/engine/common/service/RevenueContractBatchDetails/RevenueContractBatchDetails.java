package com.revrec.engine.domain.service.RevenueContractBatchDetails;

import java.time.LocalDateTime;

/**
 * Row shape for {@link RevenueContractBatchDetailsRecord}.
 */
public interface RevenueContractBatchDetails {

    Long id();

    String tenantId();

    Long batchId();

    Long revenueContractId();

    Long stageRecordProcessed();

    Long revenueContractRecordProcessed();

    Boolean processedFlag();

    Long createdPeriodId();

    String createdBy();

    LocalDateTime createdAt();

    String updatedBy();

    LocalDateTime updatedAt();
}
