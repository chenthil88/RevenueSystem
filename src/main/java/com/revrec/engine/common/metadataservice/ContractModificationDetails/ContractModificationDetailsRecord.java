package com.revrec.engine.domain.metadataservice.ContractModificationDetails;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Row mapped from TiDB table `contractModificationDetails`.
 */
public record ContractModificationDetailsRecord(
        Integer id,
        String ruleName,
        String ruleTreatmentForDistinctPob,
        String ruleTreatmentForNonDistinctPob,
        String ruleCategory,
        Boolean isActive,
        Integer createdPeriodId,
        Integer updatedPeriodId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String createdBy
) implements ContractModificationDetails, Serializable {
    public static final String TABLE_NAME = "contractModificationDetails";
}
