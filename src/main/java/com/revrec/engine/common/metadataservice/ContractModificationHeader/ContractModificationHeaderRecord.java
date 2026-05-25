package com.revrec.engine.domain.metadataservice.ContractModificationHeader;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Row mapped from TiDB table `contractModificationHeader`.
 */
public record ContractModificationHeaderRecord(
        Integer id,
        String initialContractModificationDuration,
        String revisionContractModificationDuration,
        String initialContractModificationSspDateMethod,
        String revisionContractModificationSspDateMethod,
        Integer createdPeriodId,
        Integer updatedPeriodId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String createdBy,
        String updatedBy
) implements ContractModificationHeader, Serializable {
    public static final String TABLE_NAME = "contractModificationHeader";
}
