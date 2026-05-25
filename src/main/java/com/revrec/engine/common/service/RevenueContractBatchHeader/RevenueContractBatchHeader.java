package com.revrec.engine.domain.service.RevenueContractBatchHeader;

import java.time.LocalDateTime;

/**
 * Row shape for {@link RevenueContractBatchHeaderRecord}.
 */
public interface RevenueContractBatchHeader {

    Long batchId();

    String tenantId();

    String name();

    String description();

    Long revenueContractGroupingTemplateId();

    String status();

    Long stageRecordProcessed();

    Long revenueContractRecordProcessed();

    Long createdPeriodId();

    String createdBy();

    LocalDateTime createdAt();

    String updatedBy();

    LocalDateTime updatedAt();
}
